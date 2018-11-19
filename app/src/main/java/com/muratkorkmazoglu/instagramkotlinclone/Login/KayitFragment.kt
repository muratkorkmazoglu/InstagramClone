package com.muratkorkmazoglu.instagramkotlinclone.Login


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.muratkorkmazoglu.instagramkotlinclone.R
import com.muratkorkmazoglu.instagramkotlinclone.utils.EventbusDataEvents
import kotlinx.android.synthetic.main.fragment_kayit.*
import kotlinx.android.synthetic.main.fragment_kayit.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class KayitFragment : Fragment() {

    var telNo = ""
    var verificationId = ""
    var gelenCode = ""
    var gelenEmail = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_kayit, container, false)

        view.etAdSoyad.addTextChangedListener(watcher)
        view.etKullaniciAdi.addTextChangedListener(watcher)
        view.etSifre.addTextChangedListener(watcher)

        return view
    }


    var watcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.length >= 6) {
                if (etAdSoyad.text.toString().length>5 && etKullaniciAdi.text.toString().length>5 && etSifre.text.toString().length>5){
                    btnGiris.isEnabled=true
                    btnGiris.setTextColor(ContextCompat.getColor(activity!!,R.color.beyaz))
                    btnGiris.setBackgroundResource(R.drawable.register_button_aktif)
                }

            } else {
                btnGiris.isEnabled = false
                btnGiris.setTextColor(ContextCompat.getColor(activity!!,R.color.sonukmavi))
                btnGiris.setBackgroundResource(R.drawable.register_button_aktif)
            }
        }

    }

    @Subscribe(sticky = true)
    internal fun onKayitEvent(kayitBilgileri: EventbusDataEvents.KayitBilgileriniGonder) {
        if (kayitBilgileri.emailKayit!!) {
            gelenEmail = kayitBilgileri.email!!
        } else {
            telNo = kayitBilgileri.telNo!!
            verificationId = kayitBilgileri.verificationId!!
            gelenCode = kayitBilgileri.code!!
        }
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
