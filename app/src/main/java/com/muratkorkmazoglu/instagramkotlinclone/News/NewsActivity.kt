package com.muratkorkmazoglu.instagramkotlinclone.News

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.muratkorkmazoglu.instagramkotlinclone.R
import com.muratkorkmazoglu.instagramkotlinclone.utils.BottomNavigatinViewHelper
import kotlinx.android.synthetic.main.activity_home.*

class NewsActivity : AppCompatActivity() {

    private val activity_no=3;
    private val TAG="NewsActivity";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //setupNavigationView();
    }

/*    fun setupNavigationView(){
        BottomNavigatinViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigatinViewHelper.setupNavigation(this,bottomNavigationView);
        var menu=bottomNavigationView.menu;
        var menuItem=menu.getItem(activity_no);
        menuItem.setChecked(true);
    }*/
}
