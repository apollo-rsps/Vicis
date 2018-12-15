package rs.emulate.editor.utils.formfx

import com.dooapp.fxform.builder.FXFormBuilder
import kotlin.reflect.full.createInstance

fun createFormBuilder(): FXFormBuilder<*> = FXFormBuilder::class.createInstance()
