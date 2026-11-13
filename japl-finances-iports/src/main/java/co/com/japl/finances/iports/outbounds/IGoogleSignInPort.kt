package co.com.japl.finances.iports.outbounds

import android.app.Activity
import android.content.Intent

interface IGoogleSignInPort {
    fun logoutAndOnComplete(onComplete: () -> Unit)
    fun login(resultCode: Int, dataCode: Int, data: Intent): String
    fun check(): Boolean
    fun getAccount(): Any?
    fun isGoogleDriveGranted(): Boolean
    fun isEmailAccessGranted(): Boolean
    fun isSmsAccessGranted(): Boolean
    fun getConnection(): Any?
    fun requestPermissions(activity: Activity)
    fun requestSmsPermission(activity: Activity)
}
