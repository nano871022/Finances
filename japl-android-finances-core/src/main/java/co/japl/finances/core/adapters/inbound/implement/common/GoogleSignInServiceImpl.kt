package co.japl.finances.core.adapters.inbound.implement.common

import android.app.Activity
import android.content.Intent
import co.com.japl.finances.iports.inbounds.common.IGoogleSignInService
import co.com.japl.finances.iports.outbounds.IGoogleSignInPort
import javax.inject.Inject

class GoogleSignInServiceImpl @Inject constructor(
    private val googleSignInPort: IGoogleSignInPort,
) : IGoogleSignInService {

    override fun logoutAndOnComplete(onComplete: () -> Unit) {
        googleSignInPort.logoutAndOnComplete(onComplete)
    }

    override fun login(resultCode: Int, dataCode: Int, data: Intent): String {
        return googleSignInPort.login(resultCode, dataCode, data)
    }

    override fun check(): Boolean {
        return googleSignInPort.check()
    }

    override fun getAccount(): Any? {
        return googleSignInPort.getAccount()
    }

    override fun isGoogleDriveGranted(): Boolean {
        return googleSignInPort.isGoogleDriveGranted()
    }

    override fun isEmailAccessGranted(): Boolean {
        return googleSignInPort.isEmailAccessGranted()
    }

    override fun isSmsAccessGranted(): Boolean {
        return googleSignInPort.isSmsAccessGranted()
    }

    override fun getConnection(): Any? {
        return googleSignInPort.getConnection()
    }

    override fun requestPermissions(activity: Activity) {
        googleSignInPort.requestPermissions(activity)
    }

    override fun requestSmsPermission(activity: Activity) {
        googleSignInPort.requestSmsPermission(activity)
    }
}
