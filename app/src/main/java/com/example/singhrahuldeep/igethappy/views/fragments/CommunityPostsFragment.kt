package com.example.singhrahuldeep.igethappy.views.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.igethappy.R
import com.example.singhrahuldeep.igethappy.adapter.CommunityPostAdapter
import com.example.singhrahuldeep.igethappy.interactors.OnClickEventListener
import com.example.singhrahuldeep.igethappy.interactors.OnEmojiSelectedListener
import com.example.singhrahuldeep.igethappy.interfaces.PostCallback
import com.example.singhrahuldeep.igethappy.model.input.UpdatePostInputModel
import com.example.singhrahuldeep.igethappy.model.output.GetPostResponse
import com.example.singhrahuldeep.igethappy.model.output.LikePostResponseModel
import com.example.singhrahuldeep.igethappy.utils.AppConstants
import com.example.singhrahuldeep.igethappy.utils.Utilities
import com.example.singhrahuldeep.igethappy.viewModel.LikePostResponseViewModel
import com.example.singhrahuldeep.igethappy.viewModel.PostFragmentViewModel
import com.example.singhrahuldeep.igethappy.views.community.CommunityActivity
import com.example.singhrahuldeep.igethappy.views.community.EditTextPostActivity
import com.example.singhrahuldeep.igethappy.views.core.BaseFragment
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerSimple
import okhttp3.RequestBody

/**
 * Created by Gharjyot Singh
 */


class CommunityPostsFragment : BaseFragment(), OnClickEventListener, PostCallback {



    var recyclerView: RecyclerView? = null
    lateinit var communityPostAdapter: CommunityPostAdapter
    private var fragmentViewModel: PostFragmentViewModel? = null
    private var likeViewModel: LikePostResponseViewModel? = null
    var postLike: PostCallback? = null
    var swiperefresh: SwipeRefreshLayout? = null
    var response: ArrayList<GetPostResponse.Data>? = null
    var listener: OnClickEventListener? = null
    var llNoConnection: LinearLayout? = null
    var videoview: JCVideoPlayerSimple?=null
    var arrList: ArrayList<GetPostResponse.Data>?=null
    var position: Int? = 0
    var TAG = "CommunityPostsFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_community, container, false)
        return view
    }

    @SuppressLint("WrongConstant")
    override fun setUp(view: View?) {


        listener = this
        postLike = this

        fragmentViewModel = ViewModelProviders.of(this).get(PostFragmentViewModel::class.java)
        fragmentViewModel?.init()

        likeViewModel = ViewModelProviders.of(this).get(LikePostResponseViewModel::class.java)
        likeViewModel?.init()

        swiperefresh = view!!.findViewById(R.id.swiperefresh)
        recyclerView = view.findViewById(R.id.recyclerView)
        llNoConnection = view.findViewById(R.id.ll_no_connection)

        swiperefresh!!.setOnRefreshListener {

            (activity as CommunityActivity).getMenuContext().close(true)
            fetchPosts()

        }

        swiperefresh!!.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        this.parentLayout = recyclerView

        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView!!.layoutManager = linearLayoutManager


    }

    override fun onResume() {
        super.onResume()

        fetchPosts()

        likeViewModel?.getData()?.observe(this, object : Observer<LikePostResponseModel.Data> {
            override fun onChanged(response: LikePostResponseModel.Data?) {

                if (response != null) {

                } else {

                }
            }
        })
        likeViewModel?.loading?.observe(this, Observer<Boolean> { isLoading ->
            if (isLoading!!) {
                showDialog()
            } else
                hideDialog()
        })


    }


    fun fetchPosts() {
        if (Utilities.checkInternet(activity!!)) {

            llNoConnection!!.visibility = View.GONE
            recyclerView!!.visibility = View.VISIBLE

            fragmentViewModel!!.fetchPost()

            fragmentViewModel?.list?.observe(this, object : Observer<ArrayList<GetPostResponse.Data>> {
                override fun onChanged(response: ArrayList<GetPostResponse.Data>?) {

                    swiperefresh!!.isRefreshing = false
                    if (response != null) {
                        arrList = response
                        communityPostAdapter = CommunityPostAdapter(baseActivity!!, listener!!, response, postLike!!)
                        recyclerView?.adapter = communityPostAdapter
                    } else {

                    }
                }
            })

            fragmentViewModel?.isLoading?.observe(this, Observer<Boolean> { isLoading ->
                if (isLoading!!) {
                    showDialog()
                } else
                    hideDialog()
            })

        } else {
            swiperefresh!!.isRefreshing = false
            llNoConnection!!.visibility = View.VISIBLE
            recyclerView!!.visibility = View.GONE
            showToast(getString(R.string.no_internet_msg))
        }
    }

    override fun onViewClicked(
        pos: Int, holder: CommunityPostAdapter.MyHolder
    ) {
        holder.reactionView.setInterfaceListener(object : OnEmojiSelectedListener {
            override fun onEmojiSelected(imageResource: Int) {
                holder.reactionView.visibility = View.GONE
            }
        }
        )
        if (holder.reactionView.visibility == View.VISIBLE)
            holder.reactionView.visibility = View.GONE
        else
            holder.reactionView.show()
    }

    override fun likePost(mHashMap: HashMap<String, RequestBody>) {

        likeViewModel!!.likePost(mHashMap)

    }

    override fun openMenuDialog(post_id: String, model: UpdatePostInputModel) {

        openEditPostDialog(post_id, model)
    }

    fun openEditPostDialog(post_id: String, model: UpdatePostInputModel) {

        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_pictureoption_dialog)
        dialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvEdit = dialog.findViewById(R.id.tv_camera) as TextView
        val tvDelete = dialog.findViewById(R.id.tv_gallery) as TextView
        val tvCancel = dialog.findViewById(R.id.tv_cancel) as TextView

        tvEdit.text = AppConstants.EDIT
        tvDelete.text = AppConstants.DELETE
        tvCancel.text = AppConstants.CANCEL

        tvEdit.setOnClickListener {

            (activity as CommunityActivity).getMenuContext().close(true)

            val intent = Intent(activity, EditTextPostActivity::class.java)
            intent.putExtra(AppConstants.POSTID, post_id)
            intent.putExtra(AppConstants.POSTDATA, model)
            activity!!.startActivity(intent)
            dialog.dismiss()
        }

        tvDelete.setOnClickListener {
            showToast(R.string.coming_soon)
            dialog.dismiss()
        }

        tvCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun getInstance(position: Int) {

        this.position = position
    }

    override fun noConnection() {
        showToast(getString(R.string.no_internet_msg))
    }

    override fun onPause() {
        super.onPause()


        if(arrList!=null && arrList!!.size>0) {
            recyclerView!!.scrollToPosition(position!!+3)
        }


    }

    override fun dispose() {

    }


}