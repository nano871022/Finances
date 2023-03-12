package co.japl.android.myapplication.finanzas.controller

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.config.GoogleDriveConfig
import co.japl.android.myapplication.finanzas.bussiness.impl.GoogleDriveService
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ServiceListener
import java.io.File

class GDriveConnectFragment : Fragment() , ServiceListener , OnClickListener{

    private lateinit var googleDriveService:GoogleDriveService
    private lateinit var loginBtn:Button
    private lateinit var logoutBtn:Button
    private lateinit var tvLogInfo:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_g_drive_connect, container, false)
        loginBtn = root.findViewById(R.id.login)
        logoutBtn = root.findViewById(R.id.logout)
        tvLogInfo = root.findViewById(R.id.tvResultGLogIn)
        loginBtn.visibility = View.VISIBLE
        logoutBtn.visibility = View.INVISIBLE
        googleLogin()
        return root
    }

    private fun googleLogin(){
        Log.d(this.javaClass.name,"<<<=== googleLogin start")

        this.activity?.let {
            Log.d(this.javaClass.name,"<<<=== googleLogin in parent")
            val config = GoogleDriveConfig("", GoogleDriveService.documentMimeTypes)
            Log.d(this.javaClass.name,"<<<=== googleLogin config")
            googleDriveService = GoogleDriveService(it,config)
            Log.d(this.javaClass.name,"<<<=== googleLogin GoogleDriveService instance")
            googleDriveService.serviceListener = this
            Log.d(this.javaClass.name,"<<<=== googleLogin add listener")
            googleDriveService.checkLoginStatus()
            Log.d(this.javaClass.name,"<<<=== googleLogin checkStatus")
             loginBtn.setOnClickListener{googleDriveService.auth()}
            //start.setOnClickListener{googleDriveService.pickFiles(null)}
            logoutBtn.setOnClickListener{googleDriveService.logout()}
        }
        Log.d(this.javaClass.name,"<<<=== googleLogin finish")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(this.javaClass.name, "onActivityResult $requestCode $resultCode $data")
        //super.onActivityResult(requestCode, resultCode, data)
        googleDriveService.onActivityResults(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun loggedIn() {
        tvLogInfo.text = "Logged"
        Log.d(this.javaClass.name,"Login")
        logoutBtn.visibility = View.VISIBLE
        loginBtn.visibility = View.INVISIBLE
    }

    override fun fileDownloaded(file: File) {
        Log.d(this.javaClass.name,"fileDownLoaded")
    }

    override fun cancelled(info:String) {
        tvLogInfo.text = "Cancelled - $info"

        Log.d(this.javaClass.name,"Cancelled - $info")
        logoutBtn.visibility = View.INVISIBLE
        loginBtn.visibility = View.VISIBLE
    }

    override fun handleError(exception: java.lang.Exception) {
        Log.e(this.javaClass.name,"handeError ${exception.message}")
        tvLogInfo.text = "Error ${exception.message}"
        logoutBtn.visibility = View.INVISIBLE
        loginBtn.visibility = View.VISIBLE
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.login-> googleLogin()
            R.id.logout-> Log.d(this.javaClass.name,"Click on logout functionality does not implemented")
        }
    }
}