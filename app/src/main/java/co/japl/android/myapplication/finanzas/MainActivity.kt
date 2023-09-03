package co.japl.android.myapplication

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import co.japl.android.myapplication.controller.*
import co.japl.android.myapplication.controller.ListSave
import co.japl.android.myapplication.finanzas.bussiness.config.GoogleDriveConfig
import co.japl.android.myapplication.finanzas.bussiness.impl.GoogleDriveService

import co.japl.android.myapplication.finanzas.bussiness.interfaces.ServiceListener
import co.japl.android.myapplication.finanzas.controller.AboutIt
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.Scope
import com.google.android.gms.drive.*
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){
    var drawLayout:DrawerLayout? = null
    var bundle:Bundle?=null
    lateinit var  navController:NavController
    lateinit var  appBarConfiguration:AppBarConfiguration

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
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
        if(!isTablet()) {
            findViewById<DrawerLayout>(R.id.draw_layout).setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
        return NavigationUI.onNavDestinationSelected(item,navController) || super.onOptionsItemSelected(item)
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
        if(isTablet() && navController.previousBackStackEntry == null){
            return false
        }

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    }














}