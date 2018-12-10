package rs.emulate.editor.utils.guice

import com.google.inject.AbstractModule
import com.google.inject.Binder
import com.google.inject.multibindings.MapBinder
import com.google.inject.multibindings.Multibinder

inline fun <reified K, reified V> AbstractModule.newMapBinder(binder: Binder) =
    MapBinder.newMapBinder(binder, K::class.java, V::class.java)

inline fun <reified V> AbstractModule.newSetBinder(binder: Binder) =
    Multibinder.newSetBinder(binder, V::class.java)
