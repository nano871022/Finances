package co.japl.android.myapplication.finanzas.bussiness.interfaces

import android.app.Activity
import android.content.Intent

interface IGoogleSignInService {

    fun getConnection():Any?
    fun logoutAndOnComplete(task: () -> Unit)

    fun login(requestCode:Int, resultCode:Int, data: Intent?):String?

    fun check():Boolean

    fun getAccount(): Any?

    fun isGoogleDriveGranted(): Boolean
    fun isEmailAccessGranted(): Boolean
    fun isSmsAccessGranted(): Boolean

    fun requestPermissions(activity: Activity)
    fun requestSmsPermission(activity: Activity)
}