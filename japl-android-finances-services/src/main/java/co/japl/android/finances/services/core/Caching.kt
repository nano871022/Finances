package co.japl.android.finances.services.core

import android.util.Log
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder

private val cache = CacheBuilder.newBuilder().expireAfterWrite(15, java.util.concurrent.TimeUnit.SECONDS). build<Any,Any>()

fun <T> Caching(key:Any,method:()->T):T{
    return ((cache.getIfPresent(key)?:method.invoke().let{
        it?.let{
            cache.put(key,it)
        }
        return it
    }) as T).also{
        Log.w("Caching","=> caching $key response $it")
    }
}