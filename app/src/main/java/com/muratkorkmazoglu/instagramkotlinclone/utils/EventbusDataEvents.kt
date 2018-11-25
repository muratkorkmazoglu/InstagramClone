package com.muratkorkmazoglu.instagramkotlinclone.utils

import com.muratkorkmazoglu.instagramkotlinclone.Model.Users

class EventbusDataEvents {
    internal class KayitBilgileriniGonder(
        var telNo: String?,
        var email: String?,
        var verificationId: String?,
        var code: String?,
        var emailKayit: Boolean?
    )

    internal class KullaniciBilgileriniGonder(
        var kullanici: Users?
    )

    internal class PaylasilacakResmiGonder(var resimYolu: String?, var dosyaTipiResimMi: Boolean?)

}