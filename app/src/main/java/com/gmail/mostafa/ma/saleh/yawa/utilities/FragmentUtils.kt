package com.gmail.mostafa.ma.saleh.yawa.utilities

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

object FragmentUtils {

    fun replaceFragment(fm: FragmentManager?, f: Fragment, addToBackStack: Boolean = true) = fm?.beginTransaction()?.run {
        replace(android.R.id.content, f)
        if (addToBackStack) {
            addToBackStack(null)
        }
        commit()
    }
}
