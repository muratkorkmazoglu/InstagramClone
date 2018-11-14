package com.muratkorkmazoglu.instagramkotlinclone.utils

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx


class BottomNavigatinViewHelper {

    companion object {
        fun setupBottomNavigationView(bottomNavigatinViewEx: BottomNavigationViewEx){

            bottomNavigatinViewEx.enableAnimation(false);
            bottomNavigatinViewEx.enableItemShiftingMode(false);
            bottomNavigatinViewEx.enableShiftingMode(false);
            bottomNavigatinViewEx.setTextVisibility(false);

        }
    }
}