package rs.emulate.editor.workspace.resource.extensions.annotations

import rs.emulate.editor.workspace.resource.Resource
import kotlin.reflect.KClass

annotation class SupportedResources(vararg val types: KClass<out Resource>)
