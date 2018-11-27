package com.muratkorkmazoglu.instagramkotlinclone.Home

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.*
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.muratkorkmazoglu.instagramkotlinclone.Login.LoginActivity
import com.muratkorkmazoglu.instagramkotlinclone.R
import com.muratkorkmazoglu.instagramkotlinclone.utils.BottomNavigatinViewHelper
import com.muratkorkmazoglu.instagramkotlinclone.utils.EventbusDataEvents
import com.muratkorkmazoglu.instagramkotlinclone.utils.HomePagerAdapter
import com.muratkorkmazoglu.instagramkotlinclone.utils.UniversalImageLoader
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.activity_home.*
import org.greenrobot.eventbus.EventBus

class HomeActivity : AppCompatActivity() {

    private val TAG = "HomeActivity"
    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mAuth = FirebaseAuth.getInstance()
        setupAuthListener()
        initImageLoader()

        setupHomeViewPager();
    }

    private fun setupHomeViewPager() {
        val homePagerAdapter = HomePagerAdapter(supportFragmentManager)
        homePagerAdapter.AddFragment(CameraFragment());
        homePagerAdapter.AddFragment(HomeFragment());
        homePagerAdapter.AddFragment(MessagesFragment());
        homeViewPager.adapter = homePagerAdapter;
        homeViewPager.currentItem = 1;
        homePagerAdapter.secilenFragmentiViewPagerdanSil(homeViewPager,0)
        homePagerAdapter.secilenFragmentiViewPagerdanSil(homeViewPager,2)

        homeViewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {

                    this@HomeActivity.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    this@HomeActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
                    homePagerAdapter.secilenFragmentiViewPagerdanSil(homeViewPager,1)
                    homePagerAdapter.secilenFragmentiViewPagerdanSil(homeViewPager,2)
                    homePagerAdapter.secilenFragmentiViewPageraEkle(homeViewPager,0)
                    izinIste()

                } else if (position == 1) {
                    this@HomeActivity.window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
                    this@HomeActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    homePagerAdapter.secilenFragmentiViewPagerdanSil(homeViewPager,0)
                    homePagerAdapter.secilenFragmentiViewPagerdanSil(homeViewPager,2)
                    homePagerAdapter.secilenFragmentiViewPageraEkle(homeViewPager,1)

                } else if (position == 2) {
                    this@HomeActivity.window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
                    this@HomeActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    homePagerAdapter.secilenFragmentiViewPagerdanSil(homeViewPager,1)
                    homePagerAdapter.secilenFragmentiViewPagerdanSil(homeViewPager,0)
                    homePagerAdapter.secilenFragmentiViewPageraEkle(homeViewPager,2)
                }
            }

        })
    }


    private fun izinIste() {

        Dexter.withActivity(this).withPermissions(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
            , android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report!!.areAllPermissionsGranted()) {
                    Log.e("ÇALIŞTI", "Tüm İzinler Verildi")

                    EventBus.getDefault().postSticky(EventbusDataEvents.KameraIzinBilgisiGonder(true))

                }
                if (report!!.isAnyPermissionPermanentlyDenied) {
                    Log.e("HATA", "İzinlerden birine birdaha sorma denilmiş")

                    var alertDialog = AlertDialog.Builder(this@HomeActivity)
                        .setTitle("İzin Gerekli")
                        .setMessage("Ayarlar kısmından uygulamaya izin vermeniz gerekiyor")
                        .setPositiveButton("AYARLARA GİT", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog!!.cancel()

                                var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                var uri = Uri.fromParts("package", packageName, null)
                                intent.setData(uri)
                                startActivity(intent)
                                homeViewPager.setCurrentItem(1)
                            }

                        })
                        .setNegativeButton("İptal", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog!!.cancel()
                                homeViewPager.setCurrentItem(1)


                            }

                        }).show()

                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                Log.e("HATA", "İzinlerden biri reddedildi")

                var alertDialog = AlertDialog.Builder(this@HomeActivity)
                    .setTitle("İzin Gerekli")
                    .setMessage("Uygulamayı kullanmaya devam etmek için lütfen izin veriniz")
                    .setPositiveButton("Onayla", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog!!.cancel()
                            homeViewPager.setCurrentItem(1)
                            token!!.continuePermissionRequest()
                        }

                    })
                    .setNegativeButton("İptal", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog!!.cancel()
                            homeViewPager.setCurrentItem(1)
                            token!!.cancelPermissionRequest()


                        }

                    }).show()


            }

        }).withErrorListener(object : PermissionRequestErrorListener {
            override fun onError(error: DexterError?) {
                Log.e("HATA", error.toString())
            }

        }).check()
    }




    private fun initImageLoader() {
        var universalImageLoader = UniversalImageLoader(this)
        ImageLoader.getInstance().init(universalImageLoader.config)
    }

    private fun setupAuthListener() {
        mAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser
                if (user == null) {

                    var intent =
                        Intent(this@HomeActivity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()

                } else {

                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(mAuthListener)
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener)
        }
    }

    override fun onResume() {
        super.onResume()

    }
}
