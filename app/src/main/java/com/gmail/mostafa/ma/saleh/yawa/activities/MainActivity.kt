package com.gmail.mostafa.ma.saleh.yawa.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.gmail.mostafa.ma.saleh.yawa.fragments.MainFragment
import com.gmail.mostafa.ma.saleh.yawa.utilities.Constants
import com.gmail.mostafa.ma.saleh.yawa.utilities.ResourcesUtils
import com.gmail.mostafa.ma.saleh.yawa.utilities.StringUtils

class MainActivity : AppCompatActivity() {

    private val isBackStackEmpty: Boolean
        get() = supportFragmentManager.backStackEntryCount == 0

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(ResourcesUtils.getTheme())
        setupActionBar()
        supportFragmentManager.addOnBackStackChangedListener { updateActionBar(isBackStackEmpty) }
        if (savedInstanceState == null) {
            addHomeFragment()
        }
    }

    private fun addHomeFragment() {
        supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, MainFragment())
                .commit()
    }

    private fun setupActionBar() {
        supportActionBar?.elevation = 0f
        updateActionBar(isBackStackEmpty)
    }

    private fun updateActionBar(forHome: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(!forHome)
        if (forHome) {
            title = StringUtils.formatCurrentDate(Constants.TODAY_DATE_PATTERN)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
