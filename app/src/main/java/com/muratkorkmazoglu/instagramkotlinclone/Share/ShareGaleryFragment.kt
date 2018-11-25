package com.muratkorkmazoglu.instagramkotlinclone.Share


import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter

import com.muratkorkmazoglu.instagramkotlinclone.R
import com.muratkorkmazoglu.instagramkotlinclone.utils.DosyaIslemleri
import com.muratkorkmazoglu.instagramkotlinclone.utils.EventbusDataEvents
import com.muratkorkmazoglu.instagramkotlinclone.utils.ShareActivityGridViewAdapter
import com.muratkorkmazoglu.instagramkotlinclone.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.fragment_share_galery.*
import kotlinx.android.synthetic.main.fragment_share_galery.view.*
import org.greenrobot.eventbus.EventBus


class ShareGaleryFragment : Fragment() {


    var secilenResimYolu: String? = null
    var dosyaTipiResimMi: Boolean? = null

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
        view.spnKlasorAdlari.setSelection(0)
        view.spnKlasorAdlari.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setupGridView(DosyaIslemleri.klasordekiDosyalariGetir(klasorPaths.get(position)))
            }

        }


        view.tvIleriButton.setOnClickListener {

            EventBus.getDefault().postSticky(EventbusDataEvents.PaylasilacakResmiGonder(secilenResimYolu, dosyaTipiResimMi))

            activity!!.anaLayout.visibility = View.GONE
            activity!!.fragmentContainerLayout.visibility = View.VISIBLE
            var transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerLayout, ShareNextFragment())
            transaction.addToBackStack(null)
            transaction.commit()


        }



        return view
    }


    fun setupGridView(secilenKlasordekiDosyalar: ArrayList<String>) {
        var gridAdapter =
            ShareActivityGridViewAdapter(activity, R.layout.tek_sutun_grid_resim, secilenKlasordekiDosyalar)
        gridResimler.adapter = gridAdapter
        secilenResimYolu = secilenKlasordekiDosyalar.get(0)
        resimVeyaVideoGoster(secilenKlasordekiDosyalar.get(0))


        gridResimler.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                secilenResimYolu = secilenKlasordekiDosyalar.get(position)
                resimVeyaVideoGoster(secilenKlasordekiDosyalar.get(position))
            }
        }
    }

    private fun resimVeyaVideoGoster(dosyaYolu: String) {
        var dosyaTuru = dosyaYolu.substring(dosyaYolu.lastIndexOf("."))

        if (dosyaTuru != null) {
            if (dosyaTuru.equals(".mp4")) {
                dosyaTipiResimMi = false
                videoView.visibility = View.VISIBLE
                imgCropView.visibility = View.GONE
                videoView.setVideoURI(Uri.parse("file://" + dosyaYolu))
                videoView.start()
            } else {
                dosyaTipiResimMi = true
                videoView.visibility = View.GONE
                imgCropView.visibility = View.VISIBLE
                UniversalImageLoader.setImage(dosyaYolu, imgCropView, null, "file:/")
            }
        }
    }
}
