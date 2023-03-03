package co.japl.android.myapplication.finanzas.bussiness.interfaces

import java.io.File
import java.lang.Exception

interface ServiceListener {
    fun loggedIn()
    fun fileDownloaded(file:File)
    fun cancelled(info:String)
    fun handleError(exception: Exception)
}