package com.muratkorkmazoglu.instagramkotlinclone.Home

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.muratkorkmazoglu.instagramkotlinclone.R
import com.muratkorkmazoglu.instagramkotlinclone.Share.ShareNextFragment
import com.muratkorkmazoglu.instagramkotlinclone.utils.EventbusDataEvents
import com.otaliastudios.cameraview.*
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.fragment_camera.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.File
import java.io.FileOutputStream


class CameraFragment : Fragment() {

    var mCamera: CameraView? = null
    var kameraIzniVerildiMi = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater?.inflate(R.layout.fragment_camera, container, false)
        mCamera = view.cameraView
        mCamera!!.mapGesture(Gesture.PINCH, GestureAction.ZOOM)
        mCamera!!.mapGesture(Gesture.TAP, GestureAction.FOCUS_WITH_MARKER)
        view.imgCameraSwitch.setOnClickListener {
            if (mCamera!!.facing == Facing.BACK) {
                mCamera!!.facing = Facing.FRONT
            } else {
                mCamera!!.facing = Facing.BACK
            }
        }

        view.imgFotoCek.setOnClickListener {
            if (mCamera!!.facing == Facing.BACK) {
                mCamera!!.capturePicture()
            } else {
               mCamera!!.captureSnapshot()
            }
        }

        mCamera!!.addCameraListener(object :CameraListener(){
            override fun onPictureTaken(jpeg: ByteArray?) {
                super.onPictureTaken(jpeg)
                var cekilenFotoAdi = System.currentTimeMillis()
                var cekilenResim =
                    File(Environment.getExternalStorageDirectory().absolutePath + "/DCIM/camera/" + cekilenFotoAdi + ".jpg")
                var dosyaOlustur = FileOutputStream(cekilenResim)
                dosyaOlustur.write(jpeg)
                dosyaOlustur.close()

            }
        })

        return view;


    }

    override fun onResume() {
        super.onResume()
        if (kameraIzniVerildiMi)
            mCamera!!.start()
    }

    override fun onPause() {
        super.onPause()
        mCamera!!.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mCamera != null)
            mCamera!!.destroy()
    }

    @Subscribe(sticky = true)
    internal fun onKameraIzinEvent(izinDurumu: EventbusDataEvents.KameraIzinBilgisiGonder) {
        kameraIzniVerildiMi = izinDurumu.boolean

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