package co.japl.android.myapplication.finanzas.controller

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.config.GoogleDriveConfig
import co.japl.android.myapplication.finanzas.bussiness.impl.GoogleDriveService
import co.japl.android.myapplication.finanzas.bussiness.impl.GoogleLoginOldService
import co.japl.android.myapplication.finanzas.bussiness.impl.GoogleLoginService
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGoogleLoginService
import com.google.android.gms.common.SignInButton
import java.util.Arrays

class GDriveConnectFragment : Fragment() {

    private lateinit var loginBtn:Button
    private lateinit var logoutBtn:Button
    private lateinit var tvLogInfo:TextView
    private lateinit var readBtn:Button
    private lateinit var uploadBtn:Button
    private lateinit var loggerLL:LinearLayout

    private lateinit var signButton: SignInButton
    private lateinit var service :IGoogleLoginService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_g_drive_connect, container, false)
        //service = GoogleLoginOldService(requireActivity(),9007)
        //service = GoogleLoginService(requireActivity(),1000)
        service = GoogleDriveService(requireActivity(), GoogleDriveConfig(this.tag, arrayListOf("application/vnd.google-apps.drive.file")),100)
        signButton = root.findViewById(R.id.signLogin)
        loginBtn = root.findViewById(R.id.login)
        logoutBtn = root.findViewById(R.id.logout)
        tvLogInfo = root.findViewById(R.id.tvResultGLogIn)
        readBtn = root.findViewById(R.id.read)
        uploadBtn = root.findViewById(R.id.upload)
        loggerLL = root.findViewById(R.id.logged)
        loginBtn.visibility = View.GONE
        loggerLL.visibility = View.GONE
        signButton.visibility = View.GONE

        signButton.setOnClickListener{
            //service.login()
            startActivityForResult(service.getIntent(),service.RC_SIGN_IN)
        }
        logoutBtn.setOnClickListener{
            service.logout()
            signButton.visibility = View.VISIBLE
            loggerLL.visibility = View.GONE
            tvLogInfo.text = ""
        }

        readBtn.setOnClickListener{
            service.read()
        }

        uploadBtn.setOnClickListener{
            service.upload()
        }
        validLogin()
        return root
    }



    private fun validLogin(){
        if(service.check()){
            loggerLL.visibility = View.VISIBLE
            signButton.visibility = View.GONE

            tvLogInfo.text = "${resources.getString(R.string.email_address)}: ${service.getAccount().email}"
        }else{
            loggerLL.visibility = View.GONE
            signButton.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(tag,"Activity $data $requestCode")
        data?.let {
            service.response(requestCode,resultCode,it)
            validLogin()
        }
    }



}