package com.gmail.mostafa.ma.saleh.yawa.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmail.mostafa.ma.saleh.yawa.R
import com.gmail.mostafa.ma.saleh.yawa.utilities.FragmentUtils

class NoInternetFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_no_internet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<View>(R.id.btn_retry_connection).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        FragmentUtils.replaceFragment(fragmentManager, MainFragment.newInstance(), false)
    }

    companion object {
        fun newInstance() = NoInternetFragment()
    }
}
