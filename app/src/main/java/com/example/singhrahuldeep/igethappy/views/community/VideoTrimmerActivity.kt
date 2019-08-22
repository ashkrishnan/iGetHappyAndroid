package com.example.singhrahuldeep.igethappy.views.community

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.android.igethappy.R
import com.deep.videotrimmer.DeepVideoTrimmer
import com.deep.videotrimmer.interfaces.OnTrimVideoListener
import com.example.singhrahuldeep.igethappy.utils.AppConstants
import com.example.singhrahuldeep.igethappy.views.core.BaseActivity
import kotlinx.android.synthetic.main.activity_video_trimmer.*

/**
 * Created by Gharjyot Singh
 */


class VideoTrimmerActivity : BaseActivity(), OnTrimVideoListener {

    private var mVideoTrimmer: DeepVideoTrimmer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentLayout = frameLayout
        val extraIntent = intent
        var path: String? = ""

        if (extraIntent != null) {
            path = extraIntent.getStringExtra(AppConstants.EXTRA_PATH)
        }
        mVideoTrimmer = findViewById<View>(R.id.timeLine) as DeepVideoTrimmer
        if (mVideoTrimmer != null && path != null) {
            mVideoTrimmer!!.setMaxDuration(100)
            mVideoTrimmer!!.setOnTrimVideoListener(this)
            mVideoTrimmer!!.setVideoURI(Uri.parse(path))
        }
    }


    override fun getResult(uri: Uri) {
        runOnUiThread {
                val intent = Intent(this@VideoTrimmerActivity, WriteTextPostActivity::class.java)
                intent.putExtra(AppConstants.EXTRA_VIDEO_URI, uri.path)
                intent.putExtra(AppConstants.CAMEFROM, AppConstants.VIDEO_TRIM_ACTIVITY)
                startActivity(intent)
                finish()
        }
    }

    override fun cancelAction() {
        mVideoTrimmer!!.destroy()
        runOnUiThread { }
        finish()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_video_trimmer
    }

    override fun initViews() {

    }

    override fun dispose() {

    }
}
