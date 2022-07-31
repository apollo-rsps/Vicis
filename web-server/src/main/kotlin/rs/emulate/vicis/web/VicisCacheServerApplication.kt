package rs.emulate.vicis.web

import com.fasterxml.jackson.annotation.JsonIgnore
import com.papsign.ktor.openapigen.OpenAPIGen
import com.papsign.ktor.openapigen.model.schema.SchemaModel
import com.papsign.ktor.openapigen.modules.ModuleProvider
import com.papsign.ktor.openapigen.openAPIGen
import com.papsign.ktor.openapigen.schema.builder.FinalSchemaBuilder
import com.papsign.ktor.openapigen.schema.builder.SchemaBuilder
import com.papsign.ktor.openapigen.schema.builder.provider.DefaultMapSchemaProvider
import com.papsign.ktor.openapigen.schema.builder.provider.SchemaBuilderProviderModule
import com.papsign.ktor.openapigen.schema.namer.DefaultSchemaNamer
import com.papsign.ktor.openapigen.schema.namer.SchemaNamer
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.conditionalheaders.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.partialcontent.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rs.emulate.legacy.graphics.sprite.Sprite
import kotlin.reflect.KType
import kotlin.reflect.typeOf

interface SpriteSerializationConfig {
    @get:JsonIgnore
    val raster: IntArray
}

fun Application.main() {
    install(DefaultHeaders)
    install(Compression)
    install(CallLogging)
    install(ConditionalHeaders)
    install(PartialContent)
    install(AutoHeadResponse)

    install(CORS) {
        anyHost()
    }

    install(ContentNegotiation) {
        jackson() {
            addMixIn(Sprite::class.java, SpriteSerializationConfig::class.java)
        }
    }

    install(OpenAPIGen) {
        info {
            version = "0.0.1"
            title = "Vicis Cache API"
        }

        replaceModule(DefaultMapSchemaProvider, object : SchemaBuilderProviderModule {
            override fun provide(apiGen: OpenAPIGen, provider: ModuleProvider<*>): List<SchemaBuilder> {
                return listOf(MapSchemaBuilder)
            }
        })

        replaceModule(DefaultSchemaNamer, object : SchemaNamer {
            val regex = Regex("[A-Za-z0-9_.]+")
            override fun get(type: KType): String {
                return type.toString().replace(regex) { it.value.split(".").last() }.replace(Regex(">|<|, "), "_")
            }
        })
    }
    routing {
        get("/openapi.json") {
            call.respond(application.openAPIGen.api.serialize())
        }
        get("/") {
            call.respondRedirect("/swagger-ui/index.html?url=/openapi.json", true)
        }
    }

    legacyCacheModule()
}

private object MapSchemaBuilder : SchemaBuilder {
    override val superType: KType = typeOf<Map<*, *>?>()
    override fun build(
        type: KType,
        builder: FinalSchemaBuilder,
        finalize: (SchemaModel<*>) -> SchemaModel<*>
    ): SchemaModel<*> {
        checkType(type)
        if (type.arguments[0].type != typeOf<String>() && type.arguments[0].type != typeOf<Int>()) error("bad type $type: Only maps with string keys are supported")
        val valueType = type.arguments[1].type ?: error("bad type $type: star projected types are not supported")
        @Suppress("UNCHECKED_CAST")
        return finalize(
            SchemaModel.SchemaModelMap(
                builder.build(valueType) as SchemaModel<Any?>,
                type.isMarkedNullable
            )
        )
    }
}