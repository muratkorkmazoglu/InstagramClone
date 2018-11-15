package com.muratkorkmazoglu.instagramkotlinclone.Profile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.muratkorkmazoglu.instagramkotlinclone.R
import com.muratkorkmazoglu.instagramkotlinclone.utils.BottomNavigatinViewHelper
import kotlinx.android.synthetic.main.activity_profile_settings.*


class ProfileSettingsActivity : AppCompatActivity() {
    private val activity_no = 4;
    private val TAG = "ProfileSettingsActivity";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)
        setupNavigationView()
        setupToolbar();
        fragmentNavigations();
    }

    private fun fragmentNavigations() {
        tvProfilDuzenleHesapAyarları.setOnClickListener {
            profileSettingsRoot.visibility = View.GONE;
            var transaction = supportFragmentManager.beginTransaction();
            transaction.replace(R.id.profileSettingsContainer, ProfileEditFragment())
            transaction.addToBackStack("editProfileFragmentAdded");
            transaction.commit();
        }
        tvCikisYap.setOnClickListener {
            profileSettingsRoot.visibility = View.GONE;
            var transaction = supportFragmentManager.beginTransaction();
            transaction.replace(R.id.profileSettingsContainer, SignOutFragment())
            transaction.addToBackStack("signOutFragmentAdded");
            transaction.commit();
        }
    }

    private fun setupToolbar() {
        imgBack.setOnClickListener {
            onBackPressed();
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        profileSettingsRoot.visibility = View.VISIBLE;
    }

    fun setupNavigationView() {
        BottomNavigatinViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigatinViewHelper.setupNavigation(this, bottomNavigationView);
        var menu = bottomNavigationView.menu;
        var menuItem = menu.getItem(activity_no);
        menuItem.setChecked(true);
    }
}
