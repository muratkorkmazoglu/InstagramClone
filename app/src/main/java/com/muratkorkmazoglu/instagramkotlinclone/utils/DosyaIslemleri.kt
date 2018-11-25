package com.muratkorkmazoglu.instagramkotlinclone.utils

import android.os.AsyncTask
import android.os.Environment
import android.support.v4.app.Fragment
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