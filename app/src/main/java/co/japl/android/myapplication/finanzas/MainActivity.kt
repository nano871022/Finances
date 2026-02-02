package co.japl.android.myapplication

import android.Manifest
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.Telephony
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import co.com.japl.ui.interfaces.ISMSObserver
import co.japl.android.myapplication.finanzas.controller.*
import co.japl.android.myapplication.finanzas.interfaces.ISMSBoadcastReceiver
import co.japl.android.myapplication.finanzas.modules.EntryPoint

import com.google.android.gms.drive.*
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.util.Arrays
import javax.inject.Inject
import kotlin.IllegalArgumentException

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){
    var drawLayout:DrawerLayout? = null
    var bundle:Bundle?=null
    lateinit var  navController:NavController
    lateinit var  appBarConfiguration:AppBarConfiguration

    @Inject lateinit var smsBroadcastReceiver: ISMSBoadcastReceiver
    @Inject lateinit var subscribers:Map<Class<out ISMSObserver>,@JvmSuppressWildcards ISMSObserver>

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val screen = installSplashScreen()
        setContentView(R.layout.activity_main)
        bundle = savedInstanceState
        val drawLayout =  findViewById<DrawerLayout>(R.id.draw_layout)
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val navHostFragment = navHost as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph,drawLayout)
        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
        findViewById<NavigationView>(R.id.navigation_view).setNavigationItemSelectedListener(this::onNavigationItemSelected)
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController,appBarConfiguration)
        if(isTablet()) {
            drawLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN)
        }
        if(ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                Arrays.asList(Manifest.permission.READ_SMS).toTypedArray(),1)
        }
        if(ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                Arrays.asList(Manifest.permission.RECEIVE_SMS).toTypedArray(),1)
        }

        subscribers?.values?.toList()?.takeIf { it.isNotEmpty() }?.let {
            registerReceiver(
                smsBroadcastReceiver as BroadcastReceiver,
                IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1){
            if(!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                Toast.makeText(applicationContext, "Permissions not granted to SMS", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d(this.javaClass.name,"on navigation item selected $item ")
        NavigationUI.onNavDestinationSelected(item,navController)
        findViewById<DrawerLayout>(R.id.draw_layout).setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d(this.javaClass.name," on create optiones menu $menu")
        menuInflater.inflate(R.menu.setting_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        try {
            if (!isTablet()) {
                findViewById<DrawerLayout>(R.id.draw_layout).setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
            return NavigationUI.onNavDestinationSelected(
                item,
                navController
            ) || super.onOptionsItemSelected(item)
        }catch(e:Exception){
            Log.e(this.javaClass.name,"on options item selected $e")
            return false
        }
    }

    private fun isTablet():Boolean{
        val res = resources.getDimension(R.dimen.open_menu)
        Log.d(this.javaClass.name,"open menu $res")
        if( res != 0F){
            return true
        }
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        try {
            if (isTablet() && navController.previousBackStackEntry == null) {
                return false
            }

            return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
        }catch (e:IllegalArgumentException){
            return false
        }
    }














}