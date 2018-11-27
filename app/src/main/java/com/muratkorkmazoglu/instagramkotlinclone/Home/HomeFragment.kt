package com.muratkorkmazoglu.instagramkotlinclone.Home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.muratkorkmazoglu.instagramkotlinclone.R
import com.muratkorkmazoglu.instagramkotlinclone.utils.BottomNavigatinViewHelper
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {
    private val activity_no = 0;

    lateinit var fragment_view:View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragment_view = inflater?.inflate(R.layout.fragment_home, container, false);

        return fragment_view;


    }
    fun setupNavigationView() {
        var bottomNavView=fragment_view.bottomNavigationView
        BottomNavigatinViewHelper.setupBottomNavigationView(bottomNavView);
        BottomNavigatinViewHelper.setupNavigation(activity!!, bottomNavView);
        var menu = bottomNavView.menu;
        var menuItem = menu.getItem(activity_no);
        menuItem.isChecked = true;
    }

    override fun onResume() {
        super.onResume()
        setupNavigationView()
    }
}