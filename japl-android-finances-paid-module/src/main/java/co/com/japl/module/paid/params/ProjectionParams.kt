package co.com.japl.module.paid.params

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.com.japl.module.paid.R

class ProjectionParams {
    object PARAMS{
        const val PARAM_ID_PROJECTION = "id"
        const val ARG_DEEPLINK = "android-support-nav:controller:deepLinkIntent"
    }
    companion object {

        fun download(parameters:Bundle):Int{
            parameters.let{
                if(it.containsKey(PARAMS.ARG_DEEPLINK)) {
                    val intent = it.get(PARAMS.ARG_DEEPLINK) as Intent
                    if(Uri.parse(intent.dataString).getQueryParameter(PARAMS.PARAM_ID_PROJECTION) != null){
                        return Uri.parse(intent.dataString).getQueryParameter(PARAMS.PARAM_ID_PROJECTION)!!.toInt()
                    }
                }
                return parameters.getInt(PARAMS.PARAM_ID_PROJECTION)
            }
        }

    }
}