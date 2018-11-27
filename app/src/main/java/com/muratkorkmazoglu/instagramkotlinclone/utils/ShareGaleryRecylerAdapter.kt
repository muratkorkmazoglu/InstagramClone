package com.muratkorkmazoglu.instagramkotlinclone.utils

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.muratkorkmazoglu.instagramkotlinclone.R
import kotlinx.android.synthetic.main.tek_sutun_grid_resim.view.*
import org.greenrobot.eventbus.EventBus

class ShareGaleryRecylerAdapter(var klasordekiDosyalar: ArrayList<String>, var mContext: Context) :
    RecyclerView.Adapter<ShareGaleryRecylerAdapter.MyViewHolder>() {

    lateinit var inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(mContext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var tekSutunDosya = inflater.inflate(R.layout.tek_sutun_grid_resim, parent, false)

        return MyViewHolder(tekSutunDosya)
    }

    override fun getItemCount(): Int {
        return klasordekiDosyalar.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var dosyaYolu = klasordekiDosyalar.get(position)
        var dosyaTuru = dosyaYolu.substring(dosyaYolu.lastIndexOf("."))
        if (dosyaTuru != null) {
            if (dosyaTuru.equals(".mp4")) {
                holder.videoSuresi.visibility = View.VISIBLE
                var retriver = MediaMetadataRetriever()
                retriver.setDataSource(mContext, Uri.parse("file://" + dosyaYolu))

                var videoSure = retriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                var videoSuresiLong = videoSure.toLong()
                holder.videoSuresi.text = convertDuration(videoSuresiLong)
                UniversalImageLoader.setImage(
                    dosyaYolu,
                    holder.dosyaResim,
                    holder.progressBar,
                    "file:/"
                )
            } else {
                holder.videoSuresi.visibility = View.GONE

                UniversalImageLoader.setImage(
                    dosyaYolu,
                    holder.dosyaResim,
                    holder.progressBar,
                    "file:/"
                )
            }
            holder.tekSutunDosya.setOnClickListener {
                EventBus.getDefault().post(EventbusDataEvents.GalerySecilenDosyaYolunuGonder(dosyaYolu))
            }
        }
    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        var tekSutunDosya = itemView as ConstraintLayout
        var dosyaResim = tekSutunDosya.imgTekSutunImage
        var videoSuresi = tekSutunDosya.tvSure
        var progressBar = tekSutunDosya.progressBar
    }

    fun convertDuration(duration: Long): String {

        var second = duration / 1000 % 60
        var minute = duration / (1000 * 60) % 60
        var hour = duration / (1000 * 60 * 60) % 24

        var time = ""
        if (hour > 0) {
            time = String.format("%02d:%02d:%02d", hour, minute, second)
        } else {
            time = String.format("%02d:%02d", minute, second)

        }
        return time
    }

}