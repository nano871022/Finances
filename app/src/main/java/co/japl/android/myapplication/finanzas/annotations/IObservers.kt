package co.japl.android.myapplication.finanzas.annotations

import co.com.japl.ui.interfaces.ISMSObserver
import dagger.MapKey
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class IObservers(val value:KClass<out ISMSObserver>)
