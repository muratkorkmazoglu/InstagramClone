package com.muratkorkmazoglu.instagramkotlinclone.Home

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.muratkorkmazoglu.instagramkotlinclone.R
import com.muratkorkmazoglu.instagramkotlinclone.utils.BottomNavigatinViewHelper
import com.muratkorkmazoglu.instagramkotlinclone.utils.HomePagerAdapter
import com.muratkorkmazoglu.instagramkotlinclone.utils.UniversalImageLoader
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val activity_no = 0;
    private val TAG = "HomeActivity";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initImageLoader()
        setupNavigationView();
        setupHomeViewPager();
    }

    private fun setupHomeViewPager() {
        val homePagerAdapter = HomePagerAdapter(supportFragmentManager)
        homePagerAdapter.AddFragment(CameraFragment());
        homePagerAdapter.AddFragment(HomeFragment());
        homePagerAdapter.AddFragment(MessagesFragment());
        homeViewPager.adapter = homePagerAdapter;
        homeViewPager.currentItem = 1;
    }

    fun setupNavigationView() {
        BottomNavigatinViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigatinViewHelper.setupNavigation(this, bottomNavigationView);
        var menu = bottomNavigationView.menu;
        var menuItem = menu.getItem(activity_no);
        menuItem.isChecked = true;
    }
    private fun initImageLoader() {
        var universalImageLoader = UniversalImageLoader(this)
        ImageLoader.getInstance().init(universalImageLoader.config)
    }
}
