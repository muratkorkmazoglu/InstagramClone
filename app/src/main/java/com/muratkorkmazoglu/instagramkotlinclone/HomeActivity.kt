package com.muratkorkmazoglu.instagramkotlinclone

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.muratkorkmazoglu.instagramkotlinclone.utils.BottomNavigatinViewHelper
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupNavigationView();
    }

    fun setupNavigationView(){
        BottomNavigatinViewHelper.setupBottomNavigationView(bottomNavigationView);
    }
}
