package com.muratkorkmazoglu.instagramkotlinclone.Share


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.muratkorkmazoglu.instagramkotlinclone.Home.HomeActivity
import com.muratkorkmazoglu.instagramkotlinclone.Model.Posts
import com.muratkorkmazoglu.instagramkotlinclone.Profile.YukleniyorFragment

import com.muratkorkmazoglu.instagramkotlinclone.R
import com.muratkorkmazoglu.instagramkotlinclone.utils.DosyaIslemleri
import com.muratkorkmazoglu.instagramkotlinclone.utils.EventbusDataEvents
import com.muratkorkmazoglu.instagramkotlinclone.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.fragment_share_next.*
import kotlinx.android.synthetic.main.fragment_share_next.view.*
import kotlinx.android.synthetic.main.fragment_yukleniyor.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.Exception
import java.net.URI


class ShareNextFragment : Fragment() {

    var secilenResimYolu: String? = null
    var dosyaTuruResimMi: Boolean? = null
    lateinit var mAuth: FirebaseAuth
    lateinit var mRef: DatabaseReference
    lateinit var mUser: FirebaseUser
    lateinit var mStorage: StorageReference
    var dialogYukleniyor = CompressAndLoadingFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_share_next, container, false)

        UniversalImageLoader.setImage(secilenResimYolu!!, view.imgSecilenResim, null, "file:/")

        //photoURI = Uri.parse("file://" + secilenDosyaYolu)

        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser!!
        mRef = FirebaseDatabase.getInstance().reference
        mStorage = FirebaseStorage.getInstance().reference


        view.tvIleriButton.setOnClickListener {


            if (dosyaTuruResimMi!!) {
                DosyaIslemleri.compressResimDosya(this, secilenResimYolu)
            } else {
                DosyaIslemleri.compressVideoDosya(this, secilenResimYolu!!)
            }

        }
        view.icBack.setOnClickListener {
            this.activity!!.onBackPressed()
        }

        return view
    }

    fun uploadToStorage(filePath: String?) {

        var photoUri = Uri.parse("file://" + filePath.toString())

        dialogYukleniyor.show(activity!!.supportFragmentManager, "comnpressFragment")
        dialogYukleniyor.isCancelable = false

        var uploadTask = mStorage.child("users").child(mUser.uid).child(photoUri.lastPathSegment).putFile(photoUri)
            .addOnCompleteListener(object : OnCompleteListener<UploadTask.TaskSnapshot> {
                override fun onComplete(p0: Task<UploadTask.TaskSnapshot>) {
                    if (p0.isSuccessful) {
                        dialogYukleniyor.dismiss()
                        veritabanıBilgileriniYaz(p0.getResult().downloadUrl.toString())

                    }
                }

            }).addOnFailureListener(object : OnFailureListener {
                override fun onFailure(p0: Exception) {
                    dialogYukleniyor.dismiss()
                    Toast.makeText(activity, "Hata Oluştu" + p0!!.message, Toast.LENGTH_LONG).show()
                }

            }).addOnProgressListener {
                object : OnProgressListener<UploadTask.TaskSnapshot> {
                    override fun onProgress(p0: UploadTask.TaskSnapshot?) {
                        var progress = 100.0 * (p0!!.bytesTransferred / p0!!.totalByteCount)
                        dialogYukleniyor.tvBilgi.text = "%" + progress.toInt().toString() + "yüklendi..."
                    }

                }
            }

    }

    private fun veritabanıBilgileriniYaz(downloadUrl: String) {
        var postId = mRef.child("posts").child(mUser.uid).push().key
        var yuklenenPost = Posts(mUser.uid, postId, "", etAciklama.text.toString(), downloadUrl)
        mRef.child("posts").child(mUser.uid).child(postId).setValue(yuklenenPost)
        mRef.child("posts").child(mUser.uid).child(postId).child("yuklenme_tarih").setValue(ServerValue.TIMESTAMP)

        var intent= Intent(activity,HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }

    @Subscribe(sticky = true)
    internal fun onSecilenResimEvent(secilenResim: EventbusDataEvents.PaylasilacakResmiGonder) {
        secilenResimYolu = secilenResim.resimYolu!!
        dosyaTuruResimMi = secilenResim.dosyaTipiResimMi
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
