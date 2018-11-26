package com.muratkorkmazoglu.instagramkotlinclone.utils

import android.os.AsyncTask
import android.os.Environment
import android.support.v4.app.Fragment
import android.util.Log
import com.iceteck.silicompressorr.SiliCompressor
import com.muratkorkmazoglu.instagramkotlinclone.Profile.YukleniyorFragment
import com.muratkorkmazoglu.instagramkotlinclone.Share.ShareNextFragment
import java.io.File
import java.util.*
import kotlin.Comparator

class DosyaIslemleri {
    companion object {
        fun klasordekiDosyalariGetir(klasorAdi: String): ArrayList<String> {

            var tumDosyalar = ArrayList<String>()

            var file = File(klasorAdi)
            var klasordekiTumDosyalar = file.listFiles()

            if (klasordekiTumDosyalar != null) {

                if (klasordekiTumDosyalar.size > 1) {
                    Arrays.sort(klasordekiTumDosyalar, object : Comparator<File> {
                        override fun compare(o1: File?, o2: File?): Int {
                            if (o1!!.lastModified() > o2!!.lastModified()) {
                                return -1
                            } else {
                                return 1
                            }
                        }

                    })
                }

                for (i in 0..klasordekiTumDosyalar.size - 1) {
                    if (klasordekiTumDosyalar[i].isFile) {
                        var okunanDosyaYolu = klasordekiTumDosyalar[i].absolutePath.toString()
                        Log.e("DOSYAYOLU", "---->"+okunanDosyaYolu)
                        var dosyaTuru = okunanDosyaYolu.substring(okunanDosyaYolu.lastIndexOf("."))
                        if (dosyaTuru.equals(".jpg") || dosyaTuru.equals(".jpeg") || dosyaTuru.equals(".png") || dosyaTuru.equals(
                                ".mp4"
                            )
                        ) {
                            tumDosyalar.add(okunanDosyaYolu)
                        }
                    }
                }
            }


            return tumDosyalar
        }

        fun compressResimDosya(fragment: Fragment, secilenResimYolu: String?) {
            ResimCompressAsyncTask(fragment).execute(secilenResimYolu)

        }

        fun compressVideoDosya(fragment: Fragment, secilenVideoYolu: String) {
            VideoCompressAsyncTask(fragment)
        }
    }

    internal class VideoCompressAsyncTask(fragment: Fragment) : AsyncTask<String, String, String>() {
        var mFragment = fragment
        var compressDialog = YukleniyorFragment()
        override fun doInBackground(vararg params: String?): String? {
            var yeniOlusanDosyaKlasoru =
                File(Environment.getExternalStorageDirectory().absolutePath + "/DCIM/compressedVideos/")

            if (yeniOlusanDosyaKlasoru.isDirectory || yeniOlusanDosyaKlasoru.mkdirs()) {

                var yeniDosyaYolu = SiliCompressor.with(mFragment.context)
                    .compressVideo(params[0].toString(), yeniOlusanDosyaKlasoru.path)
                return yeniDosyaYolu
            }
            return null

        }

        override fun onPreExecute() {
            super.onPreExecute()
            compressDialog.show(mFragment.activity!!.supportFragmentManager, "compressDialog")
            compressDialog.isCancelable = false

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result!!.isNullOrEmpty()) {
                compressDialog.dismiss()
                (mFragment as ShareNextFragment).uploadToStorage(result)
            }
        }

    }

    internal class ResimCompressAsyncTask(fragment: Fragment) : AsyncTask<String, String, String>() {
        var compressFrament = YukleniyorFragment()
        var mFragment = fragment

        override fun doInBackground(vararg params: String?): String {
            var yeniOlusanDosyaKlasoru =
                File(Environment.getExternalStorageDirectory().absolutePath + "/DCIM/compressed/")
            var yeniDosyaYolu = SiliCompressor.with(mFragment.context).compress(params[0], yeniOlusanDosyaKlasoru)


            return yeniDosyaYolu
        }

        override fun onPreExecute() {
            super.onPreExecute()

            compressFrament.show(mFragment.activity!!.supportFragmentManager, "compressDialog")
            compressFrament.isCancelable = false
        }

        override fun onPostExecute(result: String?) {

            compressFrament.dismiss()
            (mFragment as ShareNextFragment).uploadToStorage(result)

            super.onPostExecute(result)

        }

    }
}