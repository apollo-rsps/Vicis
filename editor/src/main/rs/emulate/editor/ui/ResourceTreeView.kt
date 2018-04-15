package rs.emulate.editor.ui

import javafx.scene.control.TreeItem
import javafx.scene.layout.Priority.ALWAYS
import rs.emulate.editor.event.ResourceLoadedEvent
import rs.emulate.editor.resource.Resource
import rs.emulate.editor.resource.ResourceManager
import rs.emulate.editor.resource.provider.ResourceProvider
import rs.emulate.editor.resource.provider.ResourceProviderResult
import tornadofx.*

sealed class ResourceTreeItem {
    data class Category(val provider: ResourceProvider<*>) : ResourceTreeItem()
    data class ResourceIdentifier(
        val provider: ResourceProvider<*>,
        val identifier: rs.emulate.editor.resource.ResourceIdentifier
    ) : ResourceTreeItem()

    data class ResourceItem(val resource: Resource) : ResourceTreeItem()
    data class Root(val text: String) : ResourceTreeItem()
}

class ResourceTreeView() : View("Resources") {
    val manager: ResourceManager by di()

    override val root = vbox()

    init {
        with(root) {
            val rootItem: TreeItem<ResourceTreeItem> = TreeItem(ResourceTreeItem.Root("Resources"))

            treeview<ResourceTreeItem>(rootItem) {
                style {
                    vgrow = ALWAYS
                }

                cellFormat {
                    text = when (it) {
                        is ResourceTreeItem.Category -> it.provider.resourceName()
                        is ResourceTreeItem.ResourceIdentifier -> "${it.provider.resourceName().removeSuffix("s")}: ${it.identifier}"
                        is ResourceTreeItem.ResourceItem -> it.resource.name()
                        is ResourceTreeItem.Root -> it.text
                    }
                }

                onUserSelect {
                    when (it) {
                        is ResourceTreeItem.ResourceIdentifier -> runAsync {
                            val result = it.provider.provide(it.identifier)
                            when (result) {
                                is ResourceProviderResult.Found<*> -> fire(ResourceLoadedEvent(result.resource))
                                else -> log.info("Not found -- @todo log a message")
                            }
                        }
                        is ResourceTreeItem.ResourceItem -> {
                            fire(ResourceLoadedEvent(it.resource))
                        }
                    }
                }

                populate { parent ->
                    val value = parent.value

                    when (value) {
                        is ResourceTreeItem.Root -> {
                            manager.providers().map {
                                ResourceTreeItem.Category(it)
                            }
                        }
                        is ResourceTreeItem.Category -> {
                            val provider = value.provider
                            val ids = provider.listAll()

                            if (provider.lazyLoads) {
                                ids.map { ResourceTreeItem.ResourceIdentifier(provider, it) }
                            } else {
                                ids.mapNotNull {
                                    val result = provider.provide(it)
                                    if (result is ResourceProviderResult.Found<*>) {
                                        ResourceTreeItem.ResourceItem(result.resource)
                                    } else {
                                        null
                                    }
                                }
                            }
                        }
                        else -> emptyList()
                    }
                }
            }
        }
    }
}
