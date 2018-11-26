package com.muratkorkmazoglu.instagramkotlinclone.Share


import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.muratkorkmazoglu.instagramkotlinclone.R
import com.muratkorkmazoglu.instagramkotlinclone.utils.EventbusDataEvents
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.Gesture
import com.otaliastudios.cameraview.GestureAction
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.fragment_share_camera.*
import kotlinx.android.synthetic.main.fragment_share_camera.view.*
import kotlinx.android.synthetic.main.fragment_share_galery.*
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class ShareCameraFragment : Fragment() {

    var cameraView: CameraView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_share_camera, container, false)
        cameraView = view.cameraView
        cameraView!!.mapGesture(Gesture.PINCH, GestureAction.ZOOM)
        cameraView!!.mapGesture(Gesture.TAP, GestureAction.FOCUS_WITH_MARKER)
        view.imgFotoCek.setOnClickListener {
            cameraView!!.capturePicture()
        }
        cameraView!!.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(jpeg: ByteArray?) {
                super.onPictureTaken(jpeg)
                var cekilenFotoAdi = System.currentTimeMillis()
                var cekilenResim =
                    File(Environment.getExternalStorageDirectory().absolutePath + "/DCIM/camera/" + cekilenFotoAdi + ".jpg")
                var dosyaOlustur = FileOutputStream(cekilenResim)
                dosyaOlustur.write(jpeg)
                dosyaOlustur.close()

                activity!!.anaLayout.visibility = View.GONE
                activity!!.fragmentContainerLayout.visibility = View.VISIBLE
                EventBus.getDefault()
                    .postSticky(EventbusDataEvents.PaylasilacakResmiGonder(cekilenResim.absolutePath.toString(), true))

                var transaction = activity!!.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerLayout, ShareNextFragment())
                transaction.addToBackStack(null)
                transaction.commit()

            }
        })
        view.imgClose.setOnClickListener { activity!!.onBackPressed() }

        return view
    }

    override fun onResume() {
        super.onResume()
        cameraView!!.start()
    }

    override fun onPause() {
        super.onPause()
        cameraView!!.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (cameraView != null)
            cameraView!!.destroy()
    }

}



