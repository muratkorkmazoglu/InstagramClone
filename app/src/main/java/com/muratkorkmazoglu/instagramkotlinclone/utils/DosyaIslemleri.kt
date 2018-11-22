package com.muratkorkmazoglu.instagramkotlinclone.utils

import java.io.File

class DosyaIslemleri {
    companion object {
        fun klasordekiDosyalariGetir(klasorAdi: String): ArrayList<String> {

            var tumDosyalar = ArrayList<String>()

            var file = File(klasorAdi)
            var klasordekiTumDosyalar = file.listFiles()

            if (klasordekiTumDosyalar != null) {
                for (i in 0..klasordekiTumDosyalar.size-1) {
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
    }
}