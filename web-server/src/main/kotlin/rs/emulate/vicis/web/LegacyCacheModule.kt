package rs.emulate.vicis.web

import com.github.michaelbull.result.mapBoth
import com.papsign.ktor.openapigen.annotations.parameters.PathParam
import com.papsign.ktor.openapigen.route.apiRouting
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.get
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rs.emulate.common.config.ConfigDecoder
import rs.emulate.common.config.Definition
import rs.emulate.common.config.floor.FloorDefinitionDecoder
import rs.emulate.common.config.kit.IdentikitDefinitionDecoder
import rs.emulate.common.config.location.LocationDefinitionDecoder
import rs.emulate.common.config.npc.NpcDefinitionDecoder
import rs.emulate.common.config.obj.ObjectDefinitionDecoder
import rs.emulate.common.config.sequence.SequenceDefinitionDecoder
import rs.emulate.common.config.spotanim.SpotAnimationDefinitionDecoder
import rs.emulate.common.config.varbit.VarbitDefinitionDecoder
import rs.emulate.common.config.varp.VarpDefinitionDecoder
import rs.emulate.legacy.AccessMode
import rs.emulate.legacy.IndexedFileSystem
import rs.emulate.legacy.archive.entryHash
import rs.emulate.legacy.graphics.sprite.MediaId
import rs.emulate.legacy.model.Model
import rs.emulate.legacy.model.ModelDecoder
import rs.emulate.util.compression.gunzip
import rs.emulate.vicis.web.storage.*
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import java.nio.file.Paths
import javax.imageio.ImageIO

inline fun <reified T : Definition> NormalOpenAPIRoute.configRoutes(
    fs: IndexedFileSystem,
    name: String,
    decoder: ConfigDecoder<T>
) = entityRoutes("/config/${name}", ConfigStorageAdapter.create(fs, name, decoder))

inline fun <reified IdT, reified EntityT> NormalOpenAPIRoute.entityRoutes(
    path: String,
    storageAdapter: StorageAdapter<IdT, EntityT>
) {
    route(path) {
        get<Unit, List<EntityT>> {
            val request = pipeline.context.request
            val response = pipeline.context.response
            val clientToken = request.headers[HttpHeaders.IfNoneMatch]?.let { StorageCacheToken(it) }

            storageAdapter.getAll(clientToken)
                .mapBoth(
                    { result ->
                        when (result) {
                            is StorageCacheLookup.CacheHit -> response.status(HttpStatusCode.NotModified)
                            is StorageCacheLookup.CacheMiss -> {
                                response.header(HttpHeaders.ETag, result.token.etag)
                                respond(result.value)
                            }
                        }
                    },
                    {
                        response.status(HttpStatusCode.ServiceUnavailable)
                    }
                )
        }
    }
}

data class ModelParam(@PathParam("Unique ID of the model") val id: Int)

fun Application.legacyCacheModule() {
    val fsPath = environment.config.tryGetString("vicis.cache") ?: error("No cache path provided")
    val fs = IndexedFileSystem(Paths.get(fsPath), AccessMode.READ_WRITE)
    val spriteStorage = SpriteStorageAdapter(fs)

    routing {
        get("/sprites/{container}/{id}.png") {
            val clientToken = call.request.headers[HttpHeaders.IfNoneMatch]?.let { StorageCacheToken(it) }
            val mediaString = call.parameters["container"] ?: error("No container provided")
            val mediaId =
                mediaString.toIntOrNull()?.let { MediaId.Id(it) } ?: MediaId.Id(("$mediaString.dat").entryHash())
            val id = call.parameters["id"]?.toIntOrNull() ?: error("No sprite ID provided")

            spriteStorage.get(SpriteId(mediaId, id), clientToken)
                .mapBoth(
                    { result ->
                        when (result) {
                            is StorageCacheLookup.CacheHit -> call.response.status(HttpStatusCode.NotModified)
                            is StorageCacheLookup.CacheMiss -> {

                                val sprite = result.value
                                val image = BufferedImage(sprite.width, sprite.height, BufferedImage.TYPE_INT_ARGB)
                                val imageRaster = (image.raster.dataBuffer as DataBufferInt).data
                                System.arraycopy(sprite.raster, 0, imageRaster, 0, imageRaster.size)

                                call.response.header(HttpHeaders.ETag, result.token.etag)
                                call.respondOutputStream(ContentType.Image.PNG) {
                                    ImageIO.write(image, "PNG", this)
                                }
                            }
                        }
                    },
                    {
                        call.response.status(HttpStatusCode.ServiceUnavailable)
                    }
                )
        }
    }

    apiRouting {
        route("/models/{id}") {
            get<ModelParam, Model> { params ->
                val data = fs[1, params.id]
                val modelData = data.gunzip()
                val model = ModelDecoder(modelData).decode()

                respond(model)
            }
        }

        entityRoutes("/widgets", WidgetStorageAdapter(fs))
        entityRoutes("/sprites", spriteStorage)

        configRoutes(fs, "loc", LocationDefinitionDecoder)
        configRoutes(fs, "obj", ObjectDefinitionDecoder)
        configRoutes(fs, "npc", NpcDefinitionDecoder)
        configRoutes(fs, "seq", SequenceDefinitionDecoder)
        configRoutes(fs, "spotanim", SpotAnimationDefinitionDecoder)
        configRoutes(fs, "idk", IdentikitDefinitionDecoder)
        configRoutes(fs, "flo", FloorDefinitionDecoder)
        configRoutes(fs, "varbit", VarbitDefinitionDecoder)
        configRoutes(fs, "varp", VarpDefinitionDecoder)
    }
}