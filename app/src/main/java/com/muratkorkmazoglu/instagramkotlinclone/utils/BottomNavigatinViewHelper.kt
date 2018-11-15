package com.muratkorkmazoglu.instagramkotlinclone.utils

import android.content.Context
import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.muratkorkmazoglu.instagramkotlinclone.Home.HomeActivity
import com.muratkorkmazoglu.instagramkotlinclone.News.NewsActivity
import com.muratkorkmazoglu.instagramkotlinclone.Profile.ProfileActivity
import com.muratkorkmazoglu.instagramkotlinclone.R
import com.muratkorkmazoglu.instagramkotlinclone.Search.SearchActivity
import com.muratkorkmazoglu.instagramkotlinclone.Share.ShareActivity


class BottomNavigatinViewHelper {

    companion object {
        fun setupBottomNavigationView(bottomNavigatinViewEx: BottomNavigationViewEx){

            bottomNavigatinViewEx.enableAnimation(false);
            bottomNavigatinViewEx.enableItemShiftingMode(false);
            bottomNavigatinViewEx.enableShiftingMode(false);
            bottomNavigatinViewEx.setTextVisibility(false);
        }

        fun setupNavigation(context:Context,bottomNavigatinViewEx: BottomNavigationViewEx){

            bottomNavigatinViewEx.onNavigationItemSelectedListener=object : BottomNavigationView.OnNavigationItemSelectedListener{
                override fun onNavigationItemSelected(item: MenuItem): Boolean {

                    when(item.itemId){
                        R.id.ic_home ->{

                            val intent = Intent(context,HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            context.startActivity(intent);
                            return true;
                        }
                        R.id.ic_search -> {
                            val intent = Intent(context,SearchActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            context.startActivity(intent);
                            return true;
                        }
                        R.id.ic_share -> {
                            val intent = Intent(context,ShareActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            context.startActivity(intent);
                            return true;
                        }
                        R.id.ic_profile -> {
                            val intent = Intent(context,ProfileActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            context.startActivity(intent);
                            return true;
                        }
                        R.id.ic_news -> {
                            val intent = Intent(context,NewsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            context.startActivity(intent);
                            return true;
                        }
                    }
                    return false;
                }

            }

        }
    }
}