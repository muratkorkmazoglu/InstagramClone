package com.muratkorkmazoglu.instagramkotlinclone.Home

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.muratkorkmazoglu.instagramkotlinclone.Login.LoginActivity
import com.muratkorkmazoglu.instagramkotlinclone.R
import com.muratkorkmazoglu.instagramkotlinclone.utils.BottomNavigatinViewHelper
import com.muratkorkmazoglu.instagramkotlinclone.utils.HomePagerAdapter
import com.muratkorkmazoglu.instagramkotlinclone.utils.UniversalImageLoader
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val activity_no = 0;
    private val TAG = "HomeActivity"
    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mAuth= FirebaseAuth.getInstance()
        setupAuthListener()
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
    private fun setupAuthListener() {
        mAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser
                if (user == null) {

                    var intent =
                        Intent(this@HomeActivity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()

                } else {

                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(mAuthListener)
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener)
        }
    }
}
