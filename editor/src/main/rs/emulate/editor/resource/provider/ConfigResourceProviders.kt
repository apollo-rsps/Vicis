package rs.emulate.editor.resource.provider

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import rs.emulate.editor.resource.ConfigurationResource
import rs.emulate.editor.resource.ConfigurationResourceProperty
import rs.emulate.editor.resource.ResourceIdentifier
import rs.emulate.legacy.IndexedFileSystem
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.legacy.config.DefinitionSupplier
import rs.emulate.legacy.config.MutableConfigDefinition
import rs.emulate.legacy.config.SerializableProperty
import rs.emulate.legacy.config.item.DefaultItemDefinition
import rs.emulate.legacy.config.item.ItemDefinition
import rs.emulate.legacy.config.npc.DefaultNpcDefinition
import rs.emulate.legacy.config.npc.NpcDefinition
import rs.emulate.legacy.config.npc.NpcProperty

typealias ConfigResourcePropertyMapper = (SerializableProperty<*>) -> ConfigurationResourceProperty<*>?

abstract class ConfigResourceProvider<out T : MutableConfigDefinition> : ResourceProvider<ConfigurationResource<T>> {
    companion object {
        const val CONFIG_INDEX = 0
        const val CONFIG_ARCHIVE_ID = 2
    }

    @Autowired lateinit var cache: IndexedFileSystem

    override val lazyLoads: Boolean = false

    private val definitionSupplier: DefinitionSupplier<T>
    private val definitionCache = mutableListOf<T>()
    private val nameMapper: (T) -> String
    private val propertyMapper: ConfigResourcePropertyMapper
    private val modelProvider: ((T) -> List<Int>?)?

    constructor(
        definitionSupplier: DefinitionSupplier<T>,
        nameMapper: (T) -> String = { it.id.toString() },
        propertyMapper: ConfigResourcePropertyMapper = { null },
        modelProvider: ((T) -> List<Int>?)? = null
    ) {
        this.definitionSupplier = definitionSupplier
        this.nameMapper = nameMapper
        this.propertyMapper = propertyMapper
        this.modelProvider = modelProvider
    }

    private fun ensureCache() {
        if (definitionCache.isEmpty()) {
            val archive = cache.getArchive(CONFIG_INDEX, CONFIG_ARCHIVE_ID)
            val decoder = ConfigDecoder<T>(archive, definitionSupplier)
            val result = decoder.decode()
            definitionCache.addAll(result)
        }
    }

    override fun provide(identifier: ResourceIdentifier): ResourceProviderResult<ConfigurationResource<T>> {
        ensureCache()

        when (identifier) {
            is ResourceIdentifier.ConfigEntry -> {
                val definition = definitionCache.getOrNull(identifier.id) ?: return ResourceProviderResult.NotFound()

                val name = nameMapper(definition)
                val properties = definition.properties.sortedBy { it.key }.mapNotNull { propertyMapper(it.value) }

                return ResourceProviderResult.Found(
                    ConfigurationResource(
                        "${resourceName().removeSuffix("s")}: $name [${identifier.id}]",
                        identifier,
                        definitionCache[identifier.id],
                        properties,
                        modelProvider
                    )
                )
            }
            else -> return ResourceProviderResult.NotSupported()
        }
    }

    override fun listAll(): List<ResourceIdentifier> {
        ensureCache()

        return definitionCache.map {
            ResourceIdentifier.ConfigEntry(definitionSupplier.name, it.id)
        }
    }
}


const val BASIC_INFORMATION = "Basic Information"
const val ACTION_INFORMATION = "Actions"

fun <T : Any> property(
    property: SerializableProperty<*>,
    name: String,
    category: String
): ConfigurationResourceProperty<T> {
    return ConfigurationResourceProperty(name, property.type(), property.value as T, category)
}

fun npcNameMapper(): (NpcDefinition) -> String = { it.name().value }

fun npcPropertyMapper(): ConfigResourcePropertyMapper = {
    when (it.type()) {
        NpcProperty.NAME -> property<String>(it, "Name", BASIC_INFORMATION)
        NpcProperty.DESCRIPTION -> property<String>(it, "Description", BASIC_INFORMATION)
        NpcProperty.CLICKABLE -> property<Boolean>(it, "Clickable", ACTION_INFORMATION)

        else -> null
    }
}

fun npcDefinitionSuppler() = DefinitionSupplier.create<NpcDefinition>(
    "npc",
    NpcDefinition::class.java,
    DefaultNpcDefinition::class.java
)

@Component
class NpcConfigResourceProvider : ConfigResourceProvider<NpcDefinition>(
    npcDefinitionSuppler(),
    npcNameMapper(),
    npcPropertyMapper(),
    { it.models().value?.toList() }
) {
    override fun resourceName(): String {
        return RESOURCE_NAME
    }

    companion object {
        const val RESOURCE_NAME = "NPCs"
    }
}

fun itemNameMapper(): (ItemDefinition) -> String = { it.name.value }

fun itemPropertyMapper(): ConfigResourcePropertyMapper = {
    ConfigurationResourceProperty(it.name, it.type(), it.value.toString())
}

fun itemDefinitionSupplier() = DefinitionSupplier.create<ItemDefinition>(
    "obj",
    ItemDefinition::class.java,
    DefaultItemDefinition::class.java
)

@Component
class ItemConfigResourceProvider : ConfigResourceProvider<ItemDefinition>(
    itemDefinitionSupplier(),
    itemNameMapper(),
    itemPropertyMapper(),
    { listOf(it.modelId.value) }
) {
    override fun resourceName(): String {
        return RESOURCE_NAME
    }

    companion object {
        const val RESOURCE_NAME = "Items"
    }
}
