package rs.emulate.editor

import javafx.collections.FXCollections
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import rs.emulate.editor.event.ResourceLoadedEvent
import rs.emulate.editor.resource.Resource
import rs.emulate.editor.resource.ResourceStore
import rs.emulate.editor.ui.ResourceContentModelViewer
import rs.emulate.editor.ui.ResourceContentViewerFactory
import tornadofx.*

data class TabData(val view: ResourceContentModelViewer<*>, val resource: Resource)

@Component
class EditorController : Controller() {
    @Autowired private lateinit var  resourceContentViewerFactories: List<ResourceContentViewerFactory<*, *>>
    @Autowired private lateinit var resourceStore: ResourceStore

    val resourcesOpened = FXCollections.observableArrayList<Resource>()!!

    var activeResourceValue: Resource? by property<Resource>()
    val activeResource get() = getProperty(EditorController::activeResourceValue)

    fun setup() {
        subscribe<ResourceLoadedEvent> {
            val tabContainer = workspace.tabContainer
            val resource = it.resource

            val openTab = tabContainer.tabs.firstOrNull { it.userData is TabData && (it.userData as TabData).resource == resource }
            if (openTab != null) {
                tabContainer.selectionModel.select(openTab)
            } else {
                val contentModel = it.resource.createContentModel(resourceStore)
                val contentViewerFactory = resourceContentViewerFactories.first { it.supports(contentModel::class) }
                val contentViewer = contentViewerFactory.create(contentModel)
                workspace.dock(contentViewer)

                val selectedTab = tabContainer.selectionModel.selectedItem

                selectedTab.userData = TabData(contentViewer, resource)
                selectedTab.text = it.resource.name()

                resourcesOpened.add(it.resource)
                activeResourceValue = it.resource
            }
        }

        activeResource.onChange { resource ->
            if (resource != null) {
                val tabContainer = workspace.tabContainer
                val tabSelection = tabContainer.selectionModel
                val tab = tabContainer.tabs.first { it.userData is TabData && (it.userData as TabData).resource == resource }

                tabSelection.select(tab)
                (tab.userData as TabData).view.onActivate()
            }
        }
    }
}