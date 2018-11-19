package com.muratkorkmazoglu.instagramkotlinclone.Login


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.muratkorkmazoglu.instagramkotlinclone.R
import com.muratkorkmazoglu.instagramkotlinclone.utils.EventbusDataEvents
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class EmailGirisYontemi : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_email_giris_yontemi, container, false)
    }

    @Subscribe(sticky = true)
    internal fun onEmailEvent(emailAdres: EventbusDataEvents.EmailGonder) {
        var gelenEmail = emailAdres.email
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
