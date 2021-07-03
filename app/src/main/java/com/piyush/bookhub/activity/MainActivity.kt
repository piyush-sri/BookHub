package com.piyush.bookhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.piyush.bookhub.R
import com.piyush.bookhub.fragment.AboutAppFragment
import com.piyush.bookhub.fragment.FavouritesFragment
import com.piyush.bookhub.fragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*
import com.piyush.bookhub.fragment.DashboardFragment as DashboardFragment

class MainActivity : AppCompatActivity() {

   lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView

    var previousMenuItem :MenuItem?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.draweraLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)


        toolbar = findViewById(R.id.toolBar)
        frameLayout = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationalView)
        setUpToolbar()

        openDashboard()

        /// so that error two is fix or to get the dashboard page at begining




        val actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        ) //this line define the function of the hamburg icon
        drawerLayout.addDrawerListener(actionBarDrawerToggle)   // this is the line of code that make the hamburger icon functional
        actionBarDrawerToggle.syncState() // when the drawer is outside we click hamburg icon the navigation drawer comes in and the hamburg icon changes to backarrow icon


       navigationView.setNavigationItemSelectedListener {
           if(previousMenuItem!=null){
               previousMenuItem?.isChecked =false
           }
           it.isCheckable = true
           it.isChecked= true
           previousMenuItem = it

           when(it.itemId){
               R.id.dashboard -> {
                    openDashboard()
                   drawerLayout.closeDrawers()
               }
               R.id.favourites ->{
                   supportFragmentManager.beginTransaction().replace(
                       R.id.frame,
                       FavouritesFragment()
                   ).addToBackStack("Favourites").commit()
                   supportActionBar?.title="Favourites"
                   drawerLayout.closeDrawers()

               }
               R.id.profile ->{
                   supportFragmentManager.beginTransaction().replace(
                       R.id.frame,
                       ProfileFragment()
                   ).addToBackStack("Profile").commit()
                   supportActionBar?.title="Profile"
                   drawerLayout.closeDrawers()
               }
               R.id.aboutApp ->{
                   supportFragmentManager.beginTransaction().replace(
                       R.id.frame,
                       AboutAppFragment()
                   ).addToBackStack("About App").commit()
                   supportActionBar?.title="About App"//this helps in naming the titlebar with the corresponding name on opening it
                   drawerLayout.closeDrawers()
               }
           }
           return@setNavigationItemSelectedListener true
       }

    }



    fun setUpToolbar(){
        setSupportActionBar(toolBar)
        supportActionBar?.title="Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if( id ==android.R.id.home){   //id.home is used to extract the id of the hameburger icon
                drawerLayout.openDrawer(GravityCompat.START)//this will make the drawer open from the leftside
            }
        return super.onOptionsItemSelected(item)
    }
    fun openDashboard(){
        val fragment = DashboardFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame,fragment)
        transaction.commit()
        supportActionBar?.title = "Dashboard"
        navigationView.setCheckedItem(R.id.dashboard)

    }

   /*  override fun onBackPressed() {
        //super.onBackPressed()// remove this so that the backpressed button dont show default behaviour
        val fragment = supportFragmentManager.findFragmentById(R.id.frame)

       //var dashboardFragment = DashboardFragment()
       while (fragment as Boolean){
           if(fragment == DashboardFragment()){
               openDashboard()
           }
           else{
              // super.onBackPressed()
           }

       }
    }*/
}
