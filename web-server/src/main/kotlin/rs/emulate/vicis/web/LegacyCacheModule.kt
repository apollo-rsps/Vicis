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
import io.ktor.server.request.*
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
import rs.emulate.legacy.graphics.sprite.SpriteDecoder
import rs.emulate.legacy.model.Model
import rs.emulate.legacy.model.ModelDecoder
import rs.emulate.legacy.widget.Widget
import rs.emulate.legacy.widget.WidgetDecoder
import rs.emulate.util.compression.gunzip
import rs.emulate.vicis.web.storage.LegacyConfigStorageAdapter
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*
import javax.imageio.ImageIO


val HTTP_DATE_FORMAT: SimpleDateFormat
    get() = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("GMT")
    }

fun ApplicationRequest.ifModifiedSince(): Date? {
    return headers[HttpHeaders.LastModified]?.let { HTTP_DATE_FORMAT.parse(it) }
}

inline fun <reified T : Definition> NormalOpenAPIRoute.configRoute(
    fs: IndexedFileSystem,
    name: String,
    decoder: ConfigDecoder<T>
) {
    val storageAdapter = LegacyConfigStorageAdapter<T>(fs, name, decoder)

    route("/config/${name}") {
        get<Unit, List<T>> {
            val request = pipeline.context.request
            val response = pipeline.context.response

            // TODO: CRC table based e-tags?
            val hasClientCacheExpired =
                request.ifModifiedSince()?.before(storageAdapter.lastModifiedTime) ?: false

            if (hasClientCacheExpired) {
                response.status(HttpStatusCode.NotModified)
            } else {
                storageAdapter
                    .loadAll()
                    .mapBoth(
                        { collection ->
                            response.header(HttpHeaders.LastModified, storageAdapter.lastModifiedTime.toInstant())
                            respond(collection)
                        },
                        { response.status(HttpStatusCode.ServiceUnavailable) }
                    )
            }
        }
    }
}

data class ModelParam(@PathParam("Unique ID of the model") val id: Int)

fun Application.legacyCacheModule() {
    val fsPath = environment.config.tryGetString("vicis.cache") ?: error("No cache path provided")
    val fs = IndexedFileSystem(Paths.get(fsPath), AccessMode.READ_WRITE)

    routing {
        get("/sprites/{container}/{id}") {
            val sprites = SpriteDecoder.create(fs, call.parameters["container"] ?: error("No container provided")).decode()
            val sprite = sprites[call.parameters["id"]?.toIntOrNull() ?: error("No sprite ID provided")]

            val image = BufferedImage(sprite.width, sprite.height, BufferedImage.TYPE_INT_ARGB)
            val imageRaster = (image.raster.dataBuffer as DataBufferInt).data
            System.arraycopy(sprite.raster, 0, imageRaster, 0, imageRaster.size)

            call.respondOutputStream(ContentType.Image.PNG) {
                ImageIO.write(image, "PNG", this)
            }
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

        route("/widgets") {
            get<Unit, List<Widget>>() {
                val widgetArchive = fs.getArchive(0, 3)
                val widgets = WidgetDecoder(widgetArchive).decode()

                respond(widgets)
            }
        }

        configRoute(fs, "loc", LocationDefinitionDecoder)
        configRoute(fs, "obj", ObjectDefinitionDecoder)
        configRoute(fs, "npc", NpcDefinitionDecoder)
        configRoute(fs, "seq", SequenceDefinitionDecoder)
        configRoute(fs, "spotanim", SpotAnimationDefinitionDecoder)
        configRoute(fs, "idk", IdentikitDefinitionDecoder)
        configRoute(fs, "flo", FloorDefinitionDecoder)
        configRoute(fs, "varbit", VarbitDefinitionDecoder)
        configRoute(fs, "varp", VarpDefinitionDecoder)
    }
}