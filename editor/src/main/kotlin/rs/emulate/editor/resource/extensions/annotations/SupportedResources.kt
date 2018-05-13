package rs.emulate.editor.resource.extensions.annotations

import rs.emulate.editor.resource.Resource
import kotlin.reflect.KClass

annotation class SupportedResources(vararg val types: KClass<out Resource>)
