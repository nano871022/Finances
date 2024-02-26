package co.japl.android.finances.services.interfaces

import java.io.File
import java.lang.Exception

interface ServiceListener {
    fun loggedIn()
    fun fileDownloaded(file:File)
    fun cancelled(info:String)
    fun handleError(exception: Exception)
}