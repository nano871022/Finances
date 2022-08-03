package co.japl.android.finanzas

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import co.japl.android.finanzas.controller.*
import co.japl.android.finanzas.controller.ListSave
import co.japl.android.finanzas.R
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         menuInflater.inflate(R.menu.setting_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.item_menu_setting_credit_card-> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_initial,ListCreditCard()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
                true
            }
            R.id.item_menu_setting_taxes-> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_initial,ListTaxCreditCard()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
                true
            }
            else -> {
                Toast.makeText(this,"Invalid Option",Toast.LENGTH_LONG).show()
                true
            }
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
            R.id.item_menu_side_boughtmade->{
                supportFragmentManager.beginTransaction().replace(R.id.fragment_initial,ListCreditCardQuote()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
                true
            }
            else->{
                Toast.makeText(this,"Invalid Option",Toast.LENGTH_LONG).show()
                true
            }
        }
    }

}