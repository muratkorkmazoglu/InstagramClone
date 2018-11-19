package com.muratkorkmazoglu.instagramkotlinclone.Profile

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.muratkorkmazoglu.instagramkotlinclone.R
import com.muratkorkmazoglu.instagramkotlinclone.utils.BottomNavigatinViewHelper
import com.muratkorkmazoglu.instagramkotlinclone.utils.UniversalImageLoader

import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    private val activity_no = 4;
    private val TAG = "ProfileActivity";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupNavigationView();
        setupToolbar();
        setupProfilePicture()
    }

    private fun setupToolbar() {
        imgProfileSettings.setOnClickListener {
            var intent = Intent(this, ProfileSettingsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }

        tvProfilDuzenleButton.setOnClickListener {
            profileRoot.visibility = View.GONE;
            var transaction = supportFragmentManager.beginTransaction();
            transaction.replace(R.id.profileContainer, ProfileEditFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }

    private fun setupProfilePicture() {
        var imgUrl = "cdn.vox-cdn.com/thumbor/eVoUeqwkKQ7MFjDCgrPrqJP5ztc=/0x0:2040x1360/1200x800/filters:focal(860x1034:1186x1360)/cdn.vox-cdn.com/uploads/chorus_image/image/59377089/wjoel_180413_1777_android_001.1523625143.jpg";
        UniversalImageLoader.setImage(imgUrl, circleProfileImage, progressBar, "https://");
    }
    override fun onBackPressed() {
        super.onBackPressed()
        profileRoot.visibility = View.VISIBLE;
    }

    fun setupNavigationView() {
        BottomNavigatinViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigatinViewHelper.setupNavigation(this, bottomNavigationView);
        var menu = bottomNavigationView.menu;
        var menuItem = menu.getItem(activity_no);
        menuItem.setChecked(true);
    }
}
