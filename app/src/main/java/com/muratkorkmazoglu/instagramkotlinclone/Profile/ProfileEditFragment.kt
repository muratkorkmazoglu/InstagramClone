package com.muratkorkmazoglu.instagramkotlinclone.Profile


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.muratkorkmazoglu.instagramkotlinclone.Model.Users

import com.muratkorkmazoglu.instagramkotlinclone.R
import com.muratkorkmazoglu.instagramkotlinclone.utils.EventbusDataEvents
import com.muratkorkmazoglu.instagramkotlinclone.utils.UniversalImageLoader
import com.nostra13.universalimageloader.core.ImageLoader
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import kotlinx.android.synthetic.main.fragment_profile_edit.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class ProfileEditFragment : Fragment() {


    lateinit var gelenKullaniciBilgileri: Users

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        setupKullaniciBilgileri(view)


        view.imgClose.setOnClickListener {
            activity?.onBackPressed();
        }

        return view;
    }

    private fun setupKullaniciBilgileri(view: View?) {
        view!!.etProfileName.setText(gelenKullaniciBilgileri.name_surname)
        view!!.etUserName.setText(gelenKullaniciBilgileri.user_name)
        if (!gelenKullaniciBilgileri.user_detail!!.biography.isNullOrEmpty()) {
            view!!.etBiography.setText(gelenKullaniciBilgileri!!.user_detail!!.biography)
        }
        if (!gelenKullaniciBilgileri.user_detail!!.webSite.isNullOrEmpty()){
            view!!.etWebSite.setText(gelenKullaniciBilgileri!!.user_detail!!.webSite)

        }
        UniversalImageLoader.setImage(gelenKullaniciBilgileri!!.user_detail!!.profilePicture.toString(), view.circleProfileImage, view.pbProfileImage, "")

    }

    @Subscribe(sticky = true)
    internal fun onKullaniciBilgileriEvent(kullaniciBilgileri: EventbusDataEvents.KullaniciBilgileriniGonder) {
        gelenKullaniciBilgileri = kullaniciBilgileri.kullanici!!
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }

}
