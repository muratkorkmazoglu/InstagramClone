package com.muratkorkmazoglu.instagramkotlinclone.Login


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.firebase.ui.auth.data.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.muratkorkmazoglu.instagramkotlinclone.Model.UserDetails
import com.muratkorkmazoglu.instagramkotlinclone.Model.Users

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
    var emailileKayit = true
    lateinit var mAuth: FirebaseAuth
    lateinit var mRef: DatabaseReference
    lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_kayit, container, false)

        view.etAdSoyad.addTextChangedListener(watcher)
        view.etKullaniciAdi.addTextChangedListener(watcher)
        view.etSifre.addTextChangedListener(watcher)
        progressBar = view.pbKullaniciKayit

        mAuth = FirebaseAuth.getInstance()

        view.tvGirisYap.setOnClickListener {
            var intent = Intent(activity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
        mRef = FirebaseDatabase.getInstance().reference

        view.btnGiris.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            var userNameKullanimdaMi = false

            mRef.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {

                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.getValue() != null) {
                        for (user in p0!!.children) {
                            var okunanKullanici = user.getValue(Users::class.java)
                            if (okunanKullanici!!.user_name.toString().equals(etKullaniciAdi.text.toString())) {
                                Toast.makeText(context, "Kullanıcı Adı Kullanımda", Toast.LENGTH_SHORT).show()
                                userNameKullanimdaMi = true
                                break
                            }
                        }
                        if (userNameKullanimdaMi == false) {
                            var password = view.etSifre.text.toString()
                            var nameSurname = view.etAdSoyad.text.toString()
                            var userName = view.etKullaniciAdi.text.toString();

                            if (emailileKayit) {

                                mAuth.createUserWithEmailAndPassword(gelenEmail, password)
                                    .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                                        override fun onComplete(p0: Task<AuthResult>) {
                                            if (p0!!.isSuccessful) {
                                                var uId = mAuth.currentUser!!.uid
                                                Toast.makeText(activity, "Oturum Açıldı", Toast.LENGTH_SHORT).show()
                                                var kaydedilecekKullaniciDetaylari =
                                                    UserDetails("0", "0", "0", "", "", "")
                                                var kaydedilecekKullanici =
                                                    Users(
                                                        gelenEmail,
                                                        password,
                                                        userName,
                                                        nameSurname,
                                                        "",
                                                        "",
                                                        uId,
                                                        kaydedilecekKullaniciDetaylari
                                                    )

                                                mRef.child("users").child(uId).setValue(kaydedilecekKullanici)
                                                    .addOnCompleteListener(object : OnCompleteListener<Void> {
                                                        override fun onComplete(p0: Task<Void>) {
                                                            if (p0!!.isSuccessful) {
                                                                Toast.makeText(
                                                                    activity,
                                                                    "Kullanıcı Kaydedildi",
                                                                    Toast.LENGTH_SHORT
                                                                )
                                                                    .show()
                                                                progressBar.visibility = View.INVISIBLE
                                                            } else {
                                                                Log.e("HATAAAA-------", p0.exception.toString())
                                                                progressBar.visibility = View.INVISIBLE
                                                                mAuth.currentUser!!.delete()
                                                                    .addOnCompleteListener(object :
                                                                        OnCompleteListener<Void> {
                                                                        override fun onComplete(p0: Task<Void>) {
                                                                            if (p0!!.isSuccessful) {
                                                                                progressBar.visibility = View.INVISIBLE
                                                                                Toast.makeText(
                                                                                    activity,
                                                                                    "Kullanıcı Kaydedilemedi.Tekrar Deneyin",
                                                                                    Toast.LENGTH_SHORT
                                                                                ).show()
                                                                            }
                                                                        }

                                                                    })
                                                            }
                                                        }

                                                    })


                                            } else {
                                                Toast.makeText(activity, "Oturum Açılamadı", Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                    })
                            } else {

                                var password = view.etSifre.text.toString()
                                var sahteEmail = telNo + "@murat.com";

                                mAuth.createUserWithEmailAndPassword(sahteEmail, password)
                                    .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                                        override fun onComplete(p0: Task<AuthResult>) {
                                            if (p0!!.isSuccessful) {
                                                progressBar.visibility = View.INVISIBLE
                                                Toast.makeText(
                                                    activity,
                                                    "Oturum TelNo ile Açıldı. UId :" + mAuth.currentUser!!.uid,
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                                var uId = mAuth.currentUser!!.uid
                                                Toast.makeText(activity, "Oturum Açıldı", Toast.LENGTH_SHORT).show()
                                                var kaydedilecekKullaniciDetaylari =
                                                    UserDetails("0", "0", "0", "", "", "")

                                                var kaydedilecekKullanici =
                                                    Users(
                                                        "",
                                                        password,
                                                        userName,
                                                        nameSurname,
                                                        telNo,
                                                        sahteEmail,
                                                        uId,
                                                        kaydedilecekKullaniciDetaylari
                                                    )

                                                mRef.child("users").child(uId).setValue(kaydedilecekKullanici)
                                                    .addOnCompleteListener(object : OnCompleteListener<Void> {
                                                        override fun onComplete(p0: Task<Void>) {
                                                            if (p0!!.isSuccessful) {
                                                                progressBar.visibility = View.INVISIBLE
                                                                Toast.makeText(
                                                                    activity,
                                                                    "Kullanıcı Kaydedildi",
                                                                    Toast.LENGTH_SHORT
                                                                )
                                                                    .show()

                                                            } else {
                                                                mAuth.currentUser!!.delete()
                                                                    .addOnCompleteListener(object :
                                                                        OnCompleteListener<Void> {
                                                                        override fun onComplete(p0: Task<Void>) {
                                                                            if (p0!!.isSuccessful) {
                                                                                progressBar.visibility = View.INVISIBLE
                                                                                Toast.makeText(
                                                                                    activity,
                                                                                    "Kullanıcı Kaydedilemedi.Tekrar Deneyin",
                                                                                    Toast.LENGTH_SHORT
                                                                                )
                                                                                    .show()
                                                                            }
                                                                        }

                                                                    })
                                                            }
                                                        }

                                                    })


                                            } else {
                                                Toast.makeText(activity, "Oturum Açılamadı", Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                    })

                            }
                        }
                    }
                }

            })

        }

        return view
    }


    var watcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.length >= 6) {
                if (etAdSoyad.text.toString().length > 5 && etKullaniciAdi.text.toString().length > 5 && etSifre.text.toString().length > 5) {
                    btnGiris.isEnabled = true
                    btnGiris.setTextColor(ContextCompat.getColor(activity!!, R.color.beyaz))
                    btnGiris.setBackgroundResource(R.drawable.register_button_aktif)
                }

            } else {
                btnGiris.isEnabled = false
                btnGiris.setTextColor(ContextCompat.getColor(activity!!, R.color.sonukmavi))
                btnGiris.setBackgroundResource(R.drawable.register_button)
            }
        }

    }

    @Subscribe(sticky = true)
    internal fun onKayitEvent(kayitBilgileri: EventbusDataEvents.KayitBilgileriniGonder) {
        if (kayitBilgileri.emailKayit!!) {
            gelenEmail = kayitBilgileri.email!!
            emailileKayit = true
        } else {
            emailileKayit = false
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
