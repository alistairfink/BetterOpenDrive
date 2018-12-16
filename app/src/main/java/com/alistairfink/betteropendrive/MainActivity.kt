package com.alistairfink.betteropendrive

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v4.view.GravityCompat
import com.alistairfink.betteropendrive.helpers.SharedPreferencesClient
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Base64
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.alistairfink.betteropendrive.helpers.ImageUtils.Companion.getCircularBitmap
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
{
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var sharedPreferences = SharedPreferencesClient(this)
        var name = sharedPreferences.getString(SharedPreferenceConstants.Name)
        nav_view.getHeaderView(0).nav_header_name.text = name
        var avatar = sharedPreferences.getString(SharedPreferenceConstants.Avatar)
        var avatarByteArray = Base64.decode(avatar, Base64.DEFAULT)
        var bitmap = BitmapFactory.decodeByteArray(avatarByteArray, 0, avatarByteArray.size)
        var testBitmap = getCircularBitmap(bitmap)

        nav_view.getHeaderView(0).nav_header_imageView.setImageBitmap(testBitmap)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        displayView(R.id.nav_item_folder_browser)
    }

    override fun onPostCreate(savedInstanceState: Bundle?)
    {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?)
    {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean
    {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean
    {
        displayView(item.itemId)
        return true
    }

    fun displayView(viewId: Int) {

        var fragment: Fragment? = null
        var title = getString(R.string.app_name)

        when (viewId) {
            R.id.nav_item_folder_browser ->
            {
                fragment = FolderBrowserFragment.newInstance(0)
                title = "My OpenDrive"
            }
            R.id.nav_item_one ->
            {
                fragment = TestFragment()
                title = "Test"
            }
            R.id.nav_item_two ->
            {
                fragment = OtherFragment()
                title = "Other"
            }
        }

        if (fragment != null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.content_frame, fragment)
            ft.commit()
        }

        // set the toolbar title
        if (supportActionBar != null) {
            supportActionBar!!.setTitle(title)
        }

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
    }
    override fun onBackPressed()
    {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}