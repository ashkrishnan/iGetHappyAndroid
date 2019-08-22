package com.example.singhrahuldeep.igethappy.views.community

import android.content.Intent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.android.igethappy.R
import com.android.igethappy.databinding.ActivityCommunityBinding
import com.example.singhrahuldeep.igethappy.utils.AppConstants
import com.example.singhrahuldeep.igethappy.views.core.BaseActivity
import com.example.singhrahuldeep.igethappy.views.dashboard.LandingPageActivity
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu
import vn.tungdx.mediapicker.MediaItem
import vn.tungdx.mediapicker.MediaOptions
import vn.tungdx.mediapicker.activities.MediaPickerActivity

/**
 * Created by Gharjyot Singh
 */


class CommunityActivity : BaseActivity(), View.OnClickListener {


    var activityBinding: ActivityCommunityBinding? = null
    var mMediaSelectedList: MutableList<MediaItem> = mutableListOf<MediaItem>()
    var circleMenu: FloatingActionMenu? = null
    var ivLogo: ImageView? = null

    companion object {
        const val REQUEST_MEDIA = 100
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_community
    }

    override fun initViews() {
        activityBinding = viewDataBinding as ActivityCommunityBinding
        parentLayout = activityBinding!!.clParent
        activityBinding!!.include.toolbarTitle.text = getString(R.string.community)
        val ivTextPost = ImageView(this)
        val ivAudioPost = ImageView(this)
        val ivVideoPost = ImageView(this)
        ivTextPost.setBackgroundResource(R.drawable.ic_write)
        ivAudioPost.setBackgroundResource(R.drawable.ic_audio)
        ivVideoPost.setBackgroundResource(R.drawable.ic_video)
        val tvParams =
            FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        ivTextPost.layoutParams = tvParams
        ivAudioPost.layoutParams = tvParams
        ivVideoPost.layoutParams = tvParams

        circleMenu = FloatingActionMenu.Builder(this)
            .setStartAngle(-45)
            .setEndAngle(-135)
            .setRadius(resources.getDimensionPixelSize(R.dimen.dp_120))
            .addSubActionView(ivVideoPost)
            .addSubActionView(ivAudioPost)
            .addSubActionView(ivTextPost)
            .attachTo(activityBinding?.linAdd)
            .build()

        circleMenu!!.close(true)

        ivLogo = findViewById(R.id.ivLogo)

        activityBinding!!.linChat.setOnClickListener {
            showToast(getString(R.string.coming_soon))
        }

        ivLogo!!.setOnClickListener {

            finish()
        }
        ivTextPost.setOnClickListener {
            circleMenu!!.close(true)
            val intent = Intent(this@CommunityActivity, WriteTextPostActivity::class.java)
            startActivity(intent)
        }
        ivAudioPost.setOnClickListener {
            circleMenu!!.close(true)
            val intent = Intent(this@CommunityActivity, WriteAudioPostActivity::class.java)
            startActivity(intent)
        }

        ivVideoPost.setOnClickListener {
            circleMenu!!.close(true)
            val builder = MediaOptions.Builder()
            val options = builder.selectVideo().canSelectMultiVideo(false).build()
            MediaPickerActivity.open(this@CommunityActivity, REQUEST_MEDIA, options)
        }

    }

    fun getMenuContext(): FloatingActionMenu {
        return circleMenu!!
    }

    override fun dispose() {
    }

    override fun onClick(view: View?) {
        when (view?.id) {


        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_MEDIA) {
            if (resultCode == RESULT_OK) {
                mMediaSelectedList = MediaPickerActivity
                    .getMediaItemSelected(data)
                startTrimActivity(
                    mMediaSelectedList[0].getPathOrigin(
                        this@CommunityActivity,
                        mMediaSelectedList[0].uriOrigin
                    )
                )

            }
        }

    }

    private fun startTrimActivity(path: String) {
        val intent = Intent(this@CommunityActivity, VideoTrimmerActivity::class.java)
        intent.putExtra(AppConstants.EXTRA_PATH, path)
        startActivity(intent)

    }

}
