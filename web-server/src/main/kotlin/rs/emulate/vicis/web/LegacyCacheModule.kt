package rs.emulate.vicis.web

import com.github.michaelbull.result.mapBoth
import com.papsign.ktor.openapigen.annotations.parameters.HeaderParam
import com.papsign.ktor.openapigen.annotations.parameters.PathParam
import com.papsign.ktor.openapigen.route.apiRouting
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.get
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import io.ktor.client.request.*
import io.ktor.client.utils.*
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
import rs.emulate.legacy.model.Model
import rs.emulate.legacy.model.ModelDecoder
import rs.emulate.util.compression.gunzip
import rs.emulate.vicis.web.storage.LegacyConfigStorageAdapter
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*


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

    apiRouting {
        route("/models/{id}") {
            get<ModelParam, Model> { params ->
                val data = fs[1, params.id]
                val modelData = data.gunzip()
                val model = ModelDecoder(modelData).decode()

                respond(model)
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