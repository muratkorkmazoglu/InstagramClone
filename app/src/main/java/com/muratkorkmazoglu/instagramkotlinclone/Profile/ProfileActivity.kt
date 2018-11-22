package com.muratkorkmazoglu.instagramkotlinclone.Profile

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.muratkorkmazoglu.instagramkotlinclone.Home.HomeActivity
import com.muratkorkmazoglu.instagramkotlinclone.Login.LoginActivity
import com.muratkorkmazoglu.instagramkotlinclone.Model.Users
import com.muratkorkmazoglu.instagramkotlinclone.R
import com.muratkorkmazoglu.instagramkotlinclone.utils.BottomNavigatinViewHelper
import com.muratkorkmazoglu.instagramkotlinclone.utils.EventbusDataEvents
import com.muratkorkmazoglu.instagramkotlinclone.utils.UniversalImageLoader

import kotlinx.android.synthetic.main.activity_profile.*
import org.greenrobot.eventbus.EventBus

class ProfileActivity : AppCompatActivity() {

    private val activity_no = 4
    private val TAG = "ProfileActivity"
    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    lateinit var mRef: DatabaseReference
    lateinit var mUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser!!
        setupAuthListener()
        mRef = FirebaseDatabase.getInstance().reference
        setupNavigationView()
        setupToolbar()
        kullaniciBilgileriniGetir()


    }

    private fun kullaniciBilgileriniGetir() {

        tvProfilDuzenleButton.isEnabled = false
        imgProfileSettings.isEnabled = false

        mRef.child("users").child(mUser.uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0!!.value != null) {
                    var okunanKullaniciBilgileri = p0.getValue(Users::class.java)

                    EventBus.getDefault()
                        .postSticky(EventbusDataEvents.KullaniciBilgileriniGonder(okunanKullaniciBilgileri))
                    tvProfilDuzenleButton.isEnabled = true
                    imgProfileSettings.isEnabled = true

                    tv_profileAdiToolbar.text = okunanKullaniciBilgileri!!.user_name.toString()
                    tvProfilGercekAdi.text = okunanKullaniciBilgileri!!.name_surname.toString()
                    tvFollowerSayisi.text = okunanKullaniciBilgileri.user_detail!!.follower.toString()
                    tvFollowingSayisi.text = okunanKullaniciBilgileri.user_detail!!.following.toString()
                    tvPostSayisi.text = okunanKullaniciBilgileri.user_detail!!.post.toString()
                    UniversalImageLoader.setImage(
                        okunanKullaniciBilgileri.user_detail!!.profilePicture.toString(),
                        circleProfileImage,
                        progressBar,
                        ""
                    )
                    if (!okunanKullaniciBilgileri.user_detail!!.biography!!.isNullOrEmpty()) {
                        tvBiyography.visibility = View.VISIBLE
                        tvBiyography.text = okunanKullaniciBilgileri.user_detail!!.biography.toString()
                    }
                    if (!okunanKullaniciBilgileri.user_detail!!.webSite!!.isNullOrEmpty()) {
                        tvWebSİte.visibility = View.VISIBLE
                        tvWebSİte.text = okunanKullaniciBilgileri.user_detail!!.webSite.toString()
                    }
                }

            }

        })
    }

    private fun setupToolbar() {
        imgProfileSettings.setOnClickListener {
            var intent = Intent(this, ProfileSettingsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        tvProfilDuzenleButton.setOnClickListener {
            profileRoot.visibility = View.GONE
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileContainer, ProfileEditFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        profileRoot.visibility = View.VISIBLE
    }

    fun setupNavigationView() {
        BottomNavigatinViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomNavigatinViewHelper.setupNavigation(this, bottomNavigationView)
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(activity_no)
        menuItem.isChecked = true
    }

    private fun setupAuthListener() {

        mAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user = FirebaseAuth.getInstance().currentUser
                if (user == null) {

                    var intent =
                        Intent(
                            this@ProfileActivity,
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
}
