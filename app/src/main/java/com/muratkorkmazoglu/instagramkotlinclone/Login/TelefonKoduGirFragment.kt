package com.muratkorkmazoglu.instagramkotlinclone.Login

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

import com.muratkorkmazoglu.instagramkotlinclone.R
import com.muratkorkmazoglu.instagramkotlinclone.utils.EventbusDataEvents
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_telefon_kodu_gir.*
import kotlinx.android.synthetic.main.fragment_telefon_kodu_gir.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.concurrent.TimeUnit

class TelefonKoduGirFragment : Fragment() {

    lateinit var progressbar: ProgressBar
    var gelenTelno = ""
    lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var verificationID = ""
    var gelenCode = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_telefon_kodu_gir, container, false)

        view.tvKullaniciNo.setText(gelenTelno)
        progressbar = view.pbOnayKodu
        setupCallback()

        view.btnTelKodIleri.setOnClickListener {

            if (gelenCode.equals(view.etOnayKodu.text.toString())) {
                EventBus.getDefault().postSticky(
                    EventbusDataEvents.KayitBilgileriniGonder(
                        gelenTelno,
                        null,
                        verificationID,
                        gelenCode,
                        false
                    )
                )
                var transaction = activity!!.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.loginContainer, KayitFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            } else {
                Toast.makeText(activity, "Kod Hatalı", Toast.LENGTH_SHORT).show()
            }
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            gelenTelno,
            60,
            TimeUnit.SECONDS,
            this!!.activity!!,
            mCallback
        )

        return view
    }

    private fun setupCallback() {
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                if (!credential.smsCode.isNullOrEmpty())
                    gelenCode = credential.smsCode!!
                progressbar.visibility = View.INVISIBLE
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.e("AUTH - HATA", e.toString())
                progressbar.visibility = View.INVISIBLE
            }

            override fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken) {
                verificationID = verificationId!!
                progressbar.visibility = View.VISIBLE
            }
        }

    }

    @Subscribe(sticky = true)
    internal fun onTelefonNoEvent(kayitBilgileri: EventbusDataEvents.KayitBilgileriniGonder) {
        gelenTelno = kayitBilgileri.telNo!!
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
