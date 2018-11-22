package com.muratkorkmazoglu.instagramkotlinclone.Share


import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import com.muratkorkmazoglu.instagramkotlinclone.R
import com.muratkorkmazoglu.instagramkotlinclone.utils.DosyaIslemleri
import kotlinx.android.synthetic.main.fragment_share_galery.*
import kotlinx.android.synthetic.main.fragment_share_galery.view.*


class ShareGaleryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_share_galery, container, false)

        var klasorPaths = ArrayList<String>()
        var klasorAdlari = ArrayList<String>()
        var root: String? = Environment.getExternalStorageDirectory().path
        var kameraResimleri = root + "/DCIM/Camera"
        var indirilenResimler = root + "/Download"
        var whatsappResimleri = root + "/WhatsApp/Media/WhatsApp Images"

        klasorPaths.add(kameraResimleri)
        klasorPaths.add(indirilenResimler)
        klasorPaths.add(whatsappResimleri)
        klasorAdlari.add("Camera")
        klasorAdlari.add("Downloads")
        klasorAdlari.add("WhatsApp Images")

        var spinnerArrayAdapler = ArrayAdapter(activity, android.R.layout.simple_spinner_item, klasorAdlari)
        spinnerArrayAdapler.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        view.spnKlasorAdlari.adapter = spinnerArrayAdapler

        var klasordekiDosyalar = DosyaIslemleri.klasordekiDosyalariGetir(kameraResimleri)
        for (str in klasordekiDosyalar) {
            Log.e("OKUNAN DOSYALAR", str.toString())
        }



        return view
    }


}
