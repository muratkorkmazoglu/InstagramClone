package com.muratkorkmazoglu.instagramkotlinclone.Share


import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.muratkorkmazoglu.instagramkotlinclone.R
import com.muratkorkmazoglu.instagramkotlinclone.utils.EventbusDataEvents
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.fragment_share_video.*
import kotlinx.android.synthetic.main.fragment_share_video.view.*
import org.greenrobot.eventbus.EventBus
import java.io.File


class ShareVideoFragment : Fragment() {

    var videoView: CameraView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_share_video, container, false)
        videoView = view.videoView

        videoView!!.addCameraListener(object : CameraListener() {
            override fun onVideoTaken(video: File?) {
                super.onVideoTaken(video)
                activity!!.anaLayout.visibility = View.GONE
                activity!!.fragmentContainerLayout.visibility = View.VISIBLE
                EventBus.getDefault()
                    .postSticky(EventbusDataEvents.PaylasilacakResmiGonder(video!!.absolutePath.toString(), false))

                var transaction = activity!!.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerLayout, ShareNextFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            }
        })

        view.imgVideoCek.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event!!.action == MotionEvent.ACTION_DOWN) {
                    var olusacakVideoDosyaAdi = System.currentTimeMillis()
                    var olusacakVideoDosyasi =
                        File(Environment.getExternalStorageDirectory().absolutePath + "/DCIM/InstagramVideos/" + olusacakVideoDosyaAdi + ".mp4")

                    videoView!!.startCapturingVideo(olusacakVideoDosyasi)
                    Toast.makeText(activity, "Video Kaydediliyor", Toast.LENGTH_SHORT).show()
                    return true
                } else if (event!!.action == MotionEvent.ACTION_UP) {
                    videoView!!.stopCapturingVideo()
                    Toast.makeText(activity, "Video Kaydedildi", Toast.LENGTH_SHORT).show()
                    return true
                }
                return false
            }

        })
        view.imgClose.setOnClickListener {
            activity!!.onBackPressed()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        videoView!!.start()
    }

    override fun onPause() {
        super.onPause()
        videoView!!.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (videoView != null)
            videoView!!.destroy()
    }

}
