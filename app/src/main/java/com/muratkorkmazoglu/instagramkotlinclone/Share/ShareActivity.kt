package com.muratkorkmazoglu.instagramkotlinclone.Share

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.TabLayout
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.muratkorkmazoglu.instagramkotlinclone.Login.LoginActivity
import com.muratkorkmazoglu.instagramkotlinclone.Manifest
import com.muratkorkmazoglu.instagramkotlinclone.R
import com.muratkorkmazoglu.instagramkotlinclone.utils.SharePagerAdapter
import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : AppCompatActivity() {

    private val activity_no = 2;
    private val TAG = "ShareActivity"
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        setupAuthListener()

        izinIste()


    }

    private fun izinIste() {

        Dexter.withActivity(this).withPermissions(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
            , android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report!!.areAllPermissionsGranted()) {
                    Log.e("ÇALIŞTI", "Tüm İzinler Verildi")

                    setupShareViewPager()
                }
                if (report!!.isAnyPermissionPermanentlyDenied) {
                    Log.e("HATA", "İzinlerden birine birdaha sorma denilmiş")

                    var alertDialog = AlertDialog.Builder(this@ShareActivity)
                        .setTitle("İzin Gerekli")
                        .setMessage("Ayarlar kısmından uygulamaya izin vermeniz gerekiyor")
                        .setPositiveButton("AYARLARA GİT", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog!!.cancel()
                                var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                var uri = Uri.fromParts("package", packageName, null)
                                intent.setData(uri)
                                startActivity(intent)
                            }

                        })
                        .setNegativeButton("İptal", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog!!.cancel()
                                finish()

                            }

                        }).show()

                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                Log.e("HATA", "İzinlerden biri reddedildi")

                var alertDialog = AlertDialog.Builder(this@ShareActivity)
                    .setTitle("İzin Gerekli")
                    .setMessage("Uygulamayı kullanmaya devam etmek için lütfen izin veriniz")
                    .setPositiveButton("Onayla", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog!!.cancel()
                            token!!.continuePermissionRequest()
                        }

                    })
                    .setNegativeButton("İptal", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog!!.cancel()
                            token!!.cancelPermissionRequest()
                            finish()

                        }

                    }).show()


            }

        }).withErrorListener(object : PermissionRequestErrorListener {
            override fun onError(error: DexterError?) {
                Log.e("HATA", error.toString())
            }

        }).check()
    }

    private fun setupAuthListener() {

        mAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser
                if (user == null) {

                    var intent =
                        Intent(
                            this@ShareActivity,
                            LoginActivity::class.java
                        ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()

                } else {

                }
            }
        }

    }

    private fun setupShareViewPager() {

        var tabAdlari = ArrayList<String>()
        tabAdlari.add("GALERİ")
        tabAdlari.add("FOTOĞRAF")
        tabAdlari.add("VİDEO")

        var sharePagerAdapter = SharePagerAdapter(supportFragmentManager, tabAdlari)
        sharePagerAdapter.addFragment(ShareGaleryFragment())
        sharePagerAdapter.addFragment(ShareCameraFragment())
        sharePagerAdapter.addFragment(ShareVideoFragment())

        shareViewPager.adapter = sharePagerAdapter
        shareViewPager.currentItem = 1
        shareTabLayout.setupWithViewPager(shareViewPager)

    }

    override fun onBackPressed() {
        anaLayout.visibility = View.VISIBLE
        fragmentContainerLayout.visibility = View.GONE
        super.onBackPressed()
    }

}
