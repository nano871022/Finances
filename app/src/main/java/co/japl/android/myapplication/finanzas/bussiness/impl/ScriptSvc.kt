package co.japl.android.myapplication.finanzas.bussiness.impl

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.common.api.Api.Client
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.script.Script
import com.google.api.services.script.ScriptScopes
import com.google.api.services.script.model.ExecutionRequest
import org.apache.commons.logging.Log
import org.apache.http.client.methods.RequestBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.util.Arrays


class ScriptService {
    lateinit var service:Script
    lateinit var scriptID:String
    lateinit var function:String
    lateinit var params:List<String>
    lateinit var applicationName:String
    lateinit var creds:HttpRequestInitializer
    lateinit var spreadSheetID:String
    lateinit var sheet: String

    fun init(){
        scriptID = "AKfycbzNmNXD3jfBipV6PzcEczbxCuEfMGn-SX4vtQento9LpIyw1s0F8NjP0gyxjhmKdnN4"
        spreadSheetID = "1OSzisQkpYxBiRy9AlkR1XWGR6ZU7u0NObInbc9vYyRI"
        sheet = "hoja2"
        function = "doGet"
        params = listOf("Hello,world","Next")
        applicationName = "MyApplication"
        creds = HttpRequestInitializer {

        }
    }

    fun load(){

        service = Script.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                                 GsonFactory.getDefaultInstance(),
                                creds)
            .setApplicationName(applicationName)
            .build()
    }

    fun execute():String{
        var request = ExecutionRequest()
            .setFunction(function)
            .setParameters(params)
        val response = service.scripts().run(scriptID,request).execute()

        return response["result"].toString()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun execute2(){
        val url = URL("https://script.google.com/macros/s/$scriptID/exec?sheet=$sheet&spreadSheetID=$spreadSheetID")
        with(url.openConnection() as HttpURLConnection){

            println("$responseCode:: $responseMessage $this")
            inputStream.bufferedReader().use {

                    it.lines().forEach(::println)

            }
        }
    }
}