package com.muratkorkmazoglu.instagramkotlinclone.Profile


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.muratkorkmazoglu.instagramkotlinclone.R
import com.muratkorkmazoglu.instagramkotlinclone.utils.UniversalImageLoader
import com.nostra13.universalimageloader.core.ImageLoader
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import kotlinx.android.synthetic.main.fragment_profile_edit.view.*


class ProfileEditFragment : Fragment() {

    lateinit var circleProfileImageFragment: CircleImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        view.imgClose.setOnClickListener {
            activity?.onBackPressed();
        }
        circleProfileImageFragment = view.findViewById(R.id.circleProfileImage);
        setupProfilePicture();
        return view;
    }



    private fun setupProfilePicture() {
        var imgUrl = "lanace.github.io/images/android_common.jpg";
        UniversalImageLoader.setImage(imgUrl, circleProfileImageFragment, null, "https://");
    }


}
