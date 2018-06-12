package com.gmail.mostafa.ma.saleh.yawa.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.gmail.mostafa.ma.saleh.yawa.R
import com.gmail.mostafa.ma.saleh.yawa.utilities.Constants
import com.gmail.mostafa.ma.saleh.yawa.utilities.ResourcesUtils
import com.gmail.mostafa.ma.saleh.yawa.utilities.StringUtils

class MainActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(ResourcesUtils.getTheme())
        setContentView(R.layout.activity_main)
        setupActionBar()
    }

    private fun setupActionBar() {
        supportActionBar?.elevation = 0f
        updateActionBar()
        supportFragmentManager.addOnBackStackChangedListener { updateActionBar() }
    }

    private fun updateActionBar() {
        val backStackEmpty = supportFragmentManager.backStackEntryCount == 0
        supportActionBar?.setDisplayHomeAsUpEnabled(!backStackEmpty)
        if (backStackEmpty) {
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
