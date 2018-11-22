package com.muratkorkmazoglu.instagramkotlinclone.Profile


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.muratkorkmazoglu.instagramkotlinclone.Model.Users

import com.muratkorkmazoglu.instagramkotlinclone.R
import com.muratkorkmazoglu.instagramkotlinclone.utils.EventbusDataEvents
import com.muratkorkmazoglu.instagramkotlinclone.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.fragment_profile_edit.*

import kotlinx.android.synthetic.main.fragment_profile_edit.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.Exception


class ProfileEditFragment : Fragment() {


    lateinit var gelenKullaniciBilgileri: Users
    val RESIM_SEC = 100
    lateinit var mDatabaseRef: DatabaseReference
    var profilePhotoUri: Uri? = null
    lateinit var mStorageRef: StorageReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        mDatabaseRef = FirebaseDatabase.getInstance().reference
        mStorageRef = FirebaseStorage.getInstance().reference
        setupKullaniciBilgileri(view)

        view.tvFotografiDegistir.setOnClickListener {
            var intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_PICK)
            startActivityForResult(intent, RESIM_SEC)
        }
        view.imgKaydet.setOnClickListener {


            if (profilePhotoUri != null) {

                var dialogYukleniyor = YukleniyorFragment()
                dialogYukleniyor.show(activity!!.supportFragmentManager, "yukleniyorFragment")
                dialogYukleniyor.isCancelable = false

                var uploadTask = mStorageRef.child("users").child(gelenKullaniciBilgileri.user_id.toString())
                    .child(profilePhotoUri!!.lastPathSegment)
                    .putFile(profilePhotoUri!!)
                    .addOnCompleteListener(object : OnCompleteListener<UploadTask.TaskSnapshot> {
                        override fun onComplete(p0: Task<UploadTask.TaskSnapshot>) {
                            if (p0.isSuccessful) {

                                mDatabaseRef.child("users").child(gelenKullaniciBilgileri.user_id).child("user_detail")
                                    .child("profilePicture").setValue(p0!!.getResult().downloadUrl.toString())

                                dialogYukleniyor.dismiss()
                                kullaniciAdiniGuncelle(view, true)
                            }
                        }

                    }).addOnFailureListener(object : OnFailureListener {
                        override fun onFailure(p0: Exception) {
                            kullaniciAdiniGuncelle(view, false)
                        }

                    })


            } else {
                kullaniciAdiniGuncelle(view, null)

            }

        }

        view.imgClose.setOnClickListener {
            activity?.onBackPressed()
        }

        return view
    }

    private fun kullaniciAdiniGuncelle(view: View, profilResmiDegisti: Boolean?) {

        if (!gelenKullaniciBilgileri.user_name.equals(view.etUserName.text.toString())) {
            mDatabaseRef.child("users").orderByChild("user_name")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {

                    }

                    override fun onDataChange(p0: DataSnapshot?) {
                        var usernameKullanimdaMi = false
                        for (ds in p0!!.children) {
                            var okunanKullaniciAdi = ds!!.getValue(Users::class.java)!!.user_name

                            if (okunanKullaniciAdi.equals(view.etUserName.text.toString())) {
                                usernameKullanimdaMi = true

                                profilBilgileriniGuncelle(view, profilResmiDegisti, false)
                            }
                        }
                        if (!usernameKullanimdaMi) {
                            mDatabaseRef.child("users").child(gelenKullaniciBilgileri.user_id).child("user_name")
                                .setValue(view.etUserName.text.toString())
                            profilBilgileriniGuncelle(view, profilResmiDegisti, true)
                        }
                    }

                })
        } else {
            profilBilgileriniGuncelle(view, profilResmiDegisti, null)

        }

    }

    private fun profilBilgileriniGuncelle(view: View, profilResmiDegisti: Boolean?, userNameDegisti: Boolean?) {

        var profilGuncellendiMi: Boolean? = null

        if (!gelenKullaniciBilgileri.name_surname.toString().equals(view.etProfileName.text.toString())) {
            mDatabaseRef.child("users").child(gelenKullaniciBilgileri.user_id).child("name_surname")
                .setValue(view.etProfileName.text.toString())
            profilGuncellendiMi = true
        }
        if (!gelenKullaniciBilgileri.user_detail!!.biography.toString().equals(view.etBiography.text.toString())) {
            mDatabaseRef.child("users").child(gelenKullaniciBilgileri.user_id).child("user_detail")
                .child("biography").setValue(view.etBiography.text.toString())
            profilGuncellendiMi = true
        }
        if (!gelenKullaniciBilgileri.user_detail!!.webSite.toString().equals(view.etWebSite.text.toString())) {
            mDatabaseRef.child("users").child(gelenKullaniciBilgileri.user_id).child("user_detail")
                .child("webSite").setValue(view.etWebSite.text.toString())
            profilGuncellendiMi = true
        }

        if (profilResmiDegisti == null && userNameDegisti == null && profilGuncellendiMi == null) {
            Toast.makeText(context, "Değişiklik Yapılmadı", Toast.LENGTH_SHORT).show()
        } else if (userNameDegisti == false && (profilGuncellendiMi == true || profilResmiDegisti == true)) {
            Toast.makeText(context, "Bilgiler Güncellendi Kullanıcı Adı KULLANIMDA", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(context, "Kullanıcı Güncellendi", Toast.LENGTH_SHORT).show()
            activity!!.onBackPressed()

        }


    }

    private fun setupKullaniciBilgileri(view: View?) {
        view!!.etProfileName.setText(gelenKullaniciBilgileri.name_surname)
        view!!.etUserName.setText(gelenKullaniciBilgileri.user_name)
        if (!gelenKullaniciBilgileri.user_detail!!.biography.isNullOrEmpty()) {
            view!!.etBiography.setText(gelenKullaniciBilgileri!!.user_detail!!.biography)
        }
        if (!gelenKullaniciBilgileri.user_detail!!.webSite.isNullOrEmpty()) {
            view!!.etWebSite.setText(gelenKullaniciBilgileri!!.user_detail!!.webSite)

        }
        UniversalImageLoader.setImage(
            gelenKullaniciBilgileri!!.user_detail!!.profilePicture.toString(),
            view.circleProfileImage,
            view.pbProfileImage,
            ""
        )

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESIM_SEC && resultCode == AppCompatActivity.RESULT_OK && data!!.data != null) {
            profilePhotoUri = data.data
            circleProfileImage.setImageURI(profilePhotoUri)

        }
    }

}
