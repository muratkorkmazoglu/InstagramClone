package com.muratkorkmazoglu.instagramkotlinclone.Profile


import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth

import com.muratkorkmazoglu.instagramkotlinclone.R


class SignOutFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var alert=android.support.v7.app.AlertDialog.Builder(this!!.activity!!)
            .setTitle("Instagramdan Çıkış Yap")
            .setMessage("Emin Misiniz")
            .setPositiveButton("Çıkış Yap",object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    FirebaseAuth.getInstance().signOut()
                    activity!!.finish()
                }

            })
            .setNegativeButton("İptal",object:DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dismiss()
                }

            }).create()

        return alert
    }


}
