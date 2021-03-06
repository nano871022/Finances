package co.japl.android.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.allViews
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import co.japl.android.myapplication.controller.ListSave
import co.japl.android.myapplication.controller.QuoteCredit
import co.japl.android.myapplication.controller.QuoteCreditVariable
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    var drawLayout:DrawerLayout? = null
    var bundle:Bundle?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bundle = savedInstanceState

        var toolbar:Toolbar = findViewById(R.id.tool_bar)

        setSupportActionBar(toolbar)

        drawLayout = findViewById(R.id.draw_layout)
        var toggle:ActionBarDrawerToggle = ActionBarDrawerToggle(this,
            drawLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)

        drawLayout?.addDrawerListener(toggle)
        toggle.syncState()

        var navigationView:NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener ( this )

        var menuItem:MenuItem = navigationView.menu.getItem(0)
        onNavigationItemSelected(menuItem)
        menuItem.setCheckable(true)

    }

    override fun onBackPressed(){
        if(supportFragmentManager.backStackEntryCount > 1){
           supportFragmentManager.popBackStack()
        }else{
            finish()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        return when(item.itemId){
            R.id.item_menu_side_quoteCredit -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_initial,QuoteCredit()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
                true
            }
            R.id.item_menu_side_quoteCreditVariable
            -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_initial,QuoteCreditVariable()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
                true
            }
            R.id.item_menu_side_listsave->{
                supportFragmentManager.beginTransaction().replace(R.id.fragment_initial,ListSave()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
                true
            }
            else->{
                Toast.makeText(this,"Invalid Option",Toast.LENGTH_LONG)
                true
            }
        }
    }

}