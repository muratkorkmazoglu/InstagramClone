package com.muratkorkmazoglu.instagramkotlinclone.utils

class EventbusDataEvents {
    internal class KayitBilgileriniGonder(
        var telNo: String?,
        var email: String?,
        var verificationId: String?,
        var code: String?,
        var emailKayit:Boolean?
    )

}