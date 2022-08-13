package co.japl.android.myapplication

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import co.japl.android.myapplication.controller.*
import co.japl.android.myapplication.controller.ListSave
import co.japl.android.myapplication.finanzas.controller.AboutIt
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener , FragmentManager.OnBackStackChangedListener{
    var drawLayout:DrawerLayout? = null
    var bundle:Bundle?=null

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bundle = savedInstanceState
        //setUpToolbar()
        //setUpMenuOption()
        //supportFragmentManager.addOnBackStackChangedListener ( this )
    }

    private fun setUpMenuOption(){
        val navigationView:NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener ( this )
        val menuItem:MenuItem = navigationView.menu.getItem(0)
        onNavigationItemSelected(menuItem)
    }



    @RequiresApi(Build.VERSION_CODES.R)
    fun setUpToolbar(){
        val toolbar:Toolbar = findViewById(R.id.tool_bar)
        setSupportActionBar(toolbar)
        drawLayout = findViewById(R.id.draw_layout)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<NavigationView>(R.id.navigation_view).setupWithNavController(navController)
        val appBarConfig = AppBarConfiguration(navController.graph,drawLayout)
/*



        val toggle:ActionBarDrawerToggle = ActionBarDrawerToggle(this,
            drawLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        toggle.isDrawerIndicatorEnabled = true
        Log.d(this.javaClass.name," width:: ${windowManager.currentWindowMetrics.bounds.width()} height:: ${windowManager.currentWindowMetrics.bounds.height()}")
        drawLayout?.addDrawerListener(toggle)
        if(!isTablet()){
            toggle.syncState()
        }
        toggle.syncState()
*/
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun isTablet():Boolean{
        return windowManager.currentWindowMetrics.bounds.width() >= 600 ||
                windowManager.currentWindowMetrics.bounds.height() >= 600
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         menuInflater.inflate(R.menu.setting_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(this.javaClass.name,"OptionItemSelected $item")
        return when(item.itemId){
            R.id.item_menu_setting_credit_card-> {
                drawLayout?.closeDrawers()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_initial,ListCreditCard()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
                true
            }
            R.id.item_menu_setting_taxes-> {
                drawLayout?.closeDrawers()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_initial,ListTaxCreditCard()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
                true
            }
            R.id.item_menu_setting_about_it->{
                drawLayout?.closeDrawers()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_initial,AboutIt()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
                true
            }
            else -> {
                Toast.makeText(this,"Invalid Option",Toast.LENGTH_LONG).show()
                true
            }

        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        //Log.  d(this.javaClass.name,"navigation ${controller}")
        //controller.let{it.navigate(item.itemId)}
       // return true
        Log.d(this.javaClass.name," $item")
         when(item.itemId){
            R.id.item_menu_side_quoteCredit -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_initial,QuoteCredit()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
            }
            R.id.item_menu_side_quoteCreditVariable
            -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_initial,QuoteCreditVariable()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
            }
            R.id.item_menu_side_listsave->{
                supportFragmentManager.beginTransaction().replace(R.id.fragment_initial,ListSave()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
            }
            R.id.item_menu_side_boughtmade->{
                supportFragmentManager.beginTransaction().replace(R.id.fragment_initial,ListCreditCardQuote()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
            }
            else->{
                Toast.makeText(this,"Invalid Option",Toast.LENGTH_LONG).show()
            }
        }
        supportFragmentManager.clearBackStack("")
        drawLayout?.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackStackChanged() {
        Log.d(this.javaClass.name," On Back Button ${supportFragmentManager.backStackEntryCount}")
        if(supportFragmentManager.backStackEntryCount < 2){
            //supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(false)
            Log.d(this.javaClass.name," Display Home unable ")
        }else{
          //  supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            Log.d(this.javaClass.name," Display Home enable ")
            //(activity as AppCompatActivity)?.supportActionBar?.setDisplayShowHomeEnabled(true)
            //finish()
        }

    }

    override fun onBackPressed(){
        if(drawLayout?.isDrawerOpen(GravityCompat.END) == true){
            Log.d(this.javaClass.name,"close drawable")
            drawLayout?.closeDrawer(GravityCompat.END)
        }else{
            Log.d(this.javaClass.name,"on back presend")
            super.onBackPressed()
        }
        Log.d(this.javaClass.name,"continue")
/*
        if(supportFragmentManager.backStackEntryCount > 1){
            supportFragmentManager.popBackStack()
        }else{
            finish()
        }
        */
    }


}