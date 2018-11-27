package com.muratkorkmazoglu.instagramkotlinclone.Share


import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter

import com.muratkorkmazoglu.instagramkotlinclone.R
import com.muratkorkmazoglu.instagramkotlinclone.utils.*
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.fragment_share_galery.*
import kotlinx.android.synthetic.main.fragment_share_galery.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class ShareGaleryFragment : Fragment() {


    var secilenDosyaYolu: String? = null
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
                //setupGridView(DosyaIslemleri.klasordekiDosyalariGetir(klasorPaths.get(position)))
                setupRecyclerView(DosyaIslemleri.klasordekiDosyalariGetir(klasorPaths.get(position)))
            }

        }


        view.tvIleriButton.setOnClickListener {

            activity!!.anaLayout.visibility = View.GONE
            activity!!.fragmentContainerLayout.visibility = View.VISIBLE
            EventBus.getDefault()
                .postSticky(EventbusDataEvents.PaylasilacakResmiGonder(secilenDosyaYolu, dosyaTipiResimMi))
            videoView.stopPlayback()

            var transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerLayout, ShareNextFragment())
            transaction.addToBackStack(null)
            transaction.commit()


        }

        view.imgClose.setOnClickListener {
            activity!!.onBackPressed()
        }



        return view
    }

    private fun setupRecyclerView(klasordekiDosyalar: java.util.ArrayList<String>) {
        var recyclerViewAdapter = ShareGaleryRecylerAdapter(klasordekiDosyalar, this.activity!!)
        recylerViewDosyalar.adapter = recyclerViewAdapter

        var layoutManager = GridLayoutManager(this.activity, 4)
        recylerViewDosyalar.layoutManager = layoutManager
        recylerViewDosyalar.setHasFixedSize(true)
        recylerViewDosyalar.setItemViewCacheSize(30)
        recylerViewDosyalar.isDrawingCacheEnabled=true
        recylerViewDosyalar.drawingCacheQuality=View.DRAWING_CACHE_QUALITY_LOW

        secilenDosyaYolu = klasordekiDosyalar.get(0)
        resimVeyaVideoGoster(secilenDosyaYolu!!)

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

    @Subscribe
    internal fun onSecilenResimEvent(secilenDosya: EventbusDataEvents.GalerySecilenDosyaYolunuGonder) {
        this.secilenDosyaYolu = secilenDosya.dosyaYolu!!
        resimVeyaVideoGoster(secilenDosyaYolu!!)
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
