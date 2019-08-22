package com.example.singhrahuldeep.igethappy.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.android.igethappy.R
import com.example.singhrahuldeep.igethappy.facebookreactions.ReactionView
import com.example.singhrahuldeep.igethappy.interactors.OnClickEventListener
import com.example.singhrahuldeep.igethappy.interfaces.PostCallback
import com.example.singhrahuldeep.igethappy.model.input.UpdatePostInputModel
import com.example.singhrahuldeep.igethappy.model.output.GetPostResponse
import com.example.singhrahuldeep.igethappy.utils.AppConstants
import com.example.singhrahuldeep.igethappy.utils.Utilities
import com.hugomatilla.audioplayerview.AudioPlayerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerSimple
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard
import io.zla.reactions.ReactionPopup
import io.zla.reactions.ReactionsConfigBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.ArrayList
import kotlin.collections.HashMap
/**
 * Created by Gharjyot Singh
 */


@Suppress("DEPRECATION")
class CommunityPostAdapter(
    context: Context, listener: OnClickEventListener, postList: ArrayList<GetPostResponse.Data>,
    postLike: PostCallback
) : RecyclerView.Adapter<CommunityPostAdapter.MyHolder>() {

    var TAG = "CommunityPostAdapter"
    var postLike: PostCallback? = null
    private var postList: ArrayList<GetPostResponse.Data>? = null
    var context: Context
    var listener: OnClickEventListener? = null
    var strings = arrayOf("like", "love", "laugh", "wow", "sad", "angry")
    var reactCapStrings = arrayOf("LIKE", "HEART", "HAHA", "WOW", "SAD", "ANGRY")
    var reactionStrings = arrayOf("Like", "Love", "Haha", "Wow", "Sad", "Angry")
    var colorResources = arrayOf(
        R.color.reaction_like, R.color.reaction_love, R.color.reaction_laugh,
        R.color.reaction_wow, R.color.reaction_cry, R.color.reaction_angry
    )
    private var holder: CommunityPostAdapter.MyHolder? = null


    var resourceIds = arrayOf(
        R.drawable.like,
        R.drawable.love,
        R.drawable.haha,
        R.drawable.wow,
        R.drawable.cry,
        R.drawable.ic_fb_angry
    )

    init {
        this.postList = postList
        this.context = context
        this.listener = listener
        this.postLike = postLike
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyHolder {
        val itemView: View =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_community_post, parent, false)
        return MyHolder(itemView)

    }

    override fun getItemCount(): Int {
        if (postList != null)
            return postList!!.size
        else
            return 0

    }


    override fun onViewDetachedFromWindow(holder: MyHolder) {
        super.onViewDetachedFromWindow(holder)

        holder.videoView!!.release()
        holder.avPlayer!!.destroy()

    }

    @SuppressLint("NewApi", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        this.holder = holder
        val listModel = postList!![position]

        postLike!!.getInstance(position)

        if (!listModel.user_id.equals(AppConstants.APP_USER_ID))
            holder.ivMenu!!.visibility = View.GONE

        if (!listModel.user_id.equals(AppConstants.APP_USER_ID)) {
            if (listModel.is_anonymous.equals(AppConstants.YES)) {

                Picasso.with(context).load("dadas").fit().centerCrop()
                    .placeholder(R.drawable.ic_profile_picture)
                    .error(R.drawable.ic_profile_picture)
                    .into(holder.ivUserProfilePic!!)

                holder.tvUsername.text = AppConstants.ANNOYMOUS
            } else {

                if (listModel.userimage!!.trim().length == 0) {
                    Picasso.with(context).load("dadas").fit().centerCrop()
                        .placeholder(R.drawable.ic_profile_picture)
                        .error(R.drawable.ic_profile_picture)
                        .into(holder.ivUserProfilePic!!)
                } else
                    Picasso.with(context).load(listModel.userimage!!).fit().centerCrop()
                        .placeholder(R.drawable.ic_profile_picture)
                        .error(R.drawable.ic_profile_picture)
                        .into(holder.ivUserProfilePic!!)

                holder.tvUsername.text = listModel.username
            }
        } else {
            if (listModel.userimage!!.trim().length == 0) {
                Picasso.with(context).load("dadas").fit().centerCrop()
                    .placeholder(R.drawable.ic_profile_picture)
                    .error(R.drawable.ic_profile_picture)
                    .into(holder.ivUserProfilePic!!)
            } else
                Picasso.with(context).load(listModel.userimage!!).fit().centerCrop()
                    .placeholder(R.drawable.ic_profile_picture)
                    .error(R.drawable.ic_profile_picture)
                    .into(holder.ivUserProfilePic!!)
            holder.tvUsername.text = listModel.username
        }

        holder.tvDesc!!.text = listModel.description


        val fileType = listModel.post_upload_type

        if (listModel.liked_type!!.trim().isNotEmpty()) {


                val itemPosition = reactCapStrings.indexOf(listModel.liked_type!!)

                holder.ivLike.setBackgroundDrawable(null)
                holder.ivLike.setBackgroundResource(resourceIds[itemPosition])

                if(!listModel.liked_type!!.toString().equals("LIKE"))
                holder.tvReaction!!.text = reactionStrings[itemPosition]
                else
                holder.tvReaction!!.text = context.resources.getText(R.string.support)

                holder.tvReaction!!.setTextColor(context.resources.getColor(colorResources[itemPosition]))


        }

        //listModel.postfile!!.trim()
        if (fileType.equals(AppConstants.VIDEO)) {
            holder.rlMain!!.visibility = View.VISIBLE
            holder.videoView!!.visibility = View.VISIBLE
            holder.videoView!!.setUp("http://203.100.79.159:9054/uploads/community_posts/post_upload_file-1566367825445.mp4", JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "嫂子在家吗")
            holder.avPlayer!!.visibility = View.GONE
        } else if (fileType.equals(AppConstants.AUDIO)) {
            holder.rlMain!!.visibility = View.VISIBLE
            holder.videoView!!.visibility = View.GONE
            holder.avPlayer!!.visibility = View.VISIBLE
            holder.avPlayer!!.withUrl(listModel.postfile)
        } else {
            holder.rlMain!!.visibility = View.GONE
        }


        holder.avPlayer!!.setOnAudioPlayerViewListener(object : AudioPlayerView.OnAudioPlayerViewListener {
            override fun onAudioPreparing() {

            }

            override fun onAudioReady() {

            }

            override fun onAudioFinished() {

            }
        })

        holder.ivMenu!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

            }

        })


        val popup = ReactionPopup(
            context,
            ReactionsConfigBuilder(context)
                .withReactions(
                    resourceIds.toIntArray()
                )
                .withTextSize(context.resources.getDimension(R.dimen.sp_6))
                .withReactionTexts { position ->
                    strings[position]
                }
                .build())

        // holder.tvReaction!!.setOnTouchListener(popup)
        holder.ivLike.setOnLongClickListener(popup)
        holder.tvReaction!!.setOnLongClickListener(popup)

        popup.reactionSelectedListener = { reactionPosition ->

            if (reactionPosition != -1) {

                if (Utilities.checkInternet(context)) {
                    holder.ivLike.setBackgroundDrawable(null)
                    holder.ivLike.setBackgroundResource(resourceIds.get(reactionPosition))
                    if (!reactionStrings.get(reactionPosition).equals("Like"))
                        holder.tvReaction!!.text = reactionStrings.get(reactionPosition)
                    else
                        holder.tvReaction!!.text = context.resources.getText(R.string.support)
//                    holder.tvReaction!!.text = reactionStrings.get(reactionPosition)
                    holder.tvReaction!!.setTextColor(context.resources.getColor(colorResources.get(reactionPosition)))

                    val mHashMap = HashMap<String, RequestBody>()
                    mHashMap[AppConstants.USER_ID] = createPartFromString(AppConstants.APP_USER_ID)
                    mHashMap[AppConstants.POSTID] = createPartFromString(listModel._id!!)
                    mHashMap[AppConstants.LIKETYPE] = createPartFromString(reactCapStrings[reactionPosition])
                    listModel.liked_type = reactCapStrings[reactionPosition]
                    postLike!!.likePost(mHashMap)
                } else
                    postLike!!.noConnection()

            }

            true
        }

        holder.ivLike.setOnClickListener {
            if (Utilities.checkInternet(context)) {
                if (listModel.liked_type!!.trim().equals("")) {
                    holder.ivLike.setBackgroundDrawable(null)
                    holder.ivLike.setBackgroundResource(resourceIds[0])

                    if (!reactionStrings[0].equals("Like"))
                        holder.tvReaction!!.text = reactionStrings[0]
                    else
                        holder.tvReaction!!.text = context.resources.getText(R.string.support)

                    holder.tvReaction!!.setTextColor(context.resources.getColor(colorResources[0]))

                    val mHashMap = HashMap<String, RequestBody>()
                    mHashMap.put(AppConstants.USER_ID, createPartFromString(AppConstants.APP_USER_ID))
                    mHashMap.put(AppConstants.POSTID, createPartFromString(listModel._id!!))
                    mHashMap.put(AppConstants.LIKETYPE, createPartFromString(reactCapStrings[0]))
                    listModel.liked_type = reactCapStrings.get(0)
                    postLike!!.likePost(mHashMap)

                } else {
                    // hit like api and set like icon.
                    //

                    holder.ivLike.setBackgroundDrawable(null)
                    holder.ivLike.setBackgroundResource(R.drawable.ic_like)

                    if (!reactionStrings[0].equals("Like"))
                        holder.tvReaction!!.text = reactionStrings[0]
                    else
                        holder.tvReaction!!.text = context.resources.getText(R.string.support)


                    holder.tvReaction!!.setTextColor(context.resources.getColor(R.color.black))

                    val mHashMap = HashMap<String, RequestBody>()
                    mHashMap.put(AppConstants.USER_ID, createPartFromString(AppConstants.APP_USER_ID))
                    mHashMap.put(AppConstants.POSTID, createPartFromString(listModel._id!!))
                    mHashMap.put(AppConstants.LIKETYPE, createPartFromString(AppConstants.UNLIKE))
                    listModel.liked_type = ""
                    postLike!!.likePost(mHashMap)
                }
            } else {
                postLike!!.noConnection()
            }
        }

        holder.ivMenu!!.setOnClickListener {

            val model = UpdatePostInputModel()
            model.description = listModel.description
            model.is_anonymous = listModel.is_anonymous
            model.post_upload_type = listModel.post_upload_type
            model.privacy_option = listModel.privacy_option
            model.post_upload_file = listModel.postfile

            postLike!!.openMenuDialog(listModel._id!!, model)
        }


        holder.tvReaction!!.setOnClickListener {
            if (Utilities.checkInternet(context)) {
                if (listModel.liked_type!!.trim().equals("")) {
                    holder.ivLike.setBackgroundDrawable(null)
                    holder.ivLike.setBackgroundResource(resourceIds[0])
                    if (!reactionStrings[0].equals("Like"))
                        holder.tvReaction!!.text = reactionStrings[0]
                    else
                        holder.tvReaction!!.text = context.resources.getText(R.string.support)

                    holder.tvReaction!!.setTextColor(context.resources.getColor(colorResources[0]))

                    val mHashMap = HashMap<String, RequestBody>()
                    mHashMap.put(AppConstants.USER_ID, createPartFromString(AppConstants.APP_USER_ID))
                    mHashMap.put(AppConstants.POSTID, createPartFromString(listModel._id!!))
                    mHashMap.put(AppConstants.LIKETYPE, createPartFromString(reactCapStrings[0]))
                    listModel.liked_type = reactCapStrings.get(0)
                    postLike!!.likePost(mHashMap)

                } else {
                    // hit like api and set like icon.
                    //

                    holder.ivLike.setBackgroundDrawable(null)
                    holder.ivLike.setBackgroundResource(R.drawable.ic_like)
                    holder.tvReaction!!.text = context.resources.getText(R.string.support)
                    holder.tvReaction!!.setTextColor(context.resources.getColor(R.color.black))

                    val mHashMap = HashMap<String, RequestBody>()
                    mHashMap.put(AppConstants.USER_ID, createPartFromString(AppConstants.APP_USER_ID))
                    mHashMap.put(AppConstants.POSTID, createPartFromString(listModel._id!!))
                    mHashMap.put(AppConstants.LIKETYPE, createPartFromString(AppConstants.UNLIKE))
                    listModel.liked_type = ""
                    postLike!!.likePost(mHashMap)
                }
            } else {
                postLike!!.noConnection()
            }
        }
    }

    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        var reactionView: ReactionView
        var linLike: LinearLayout
        var ivLike: ImageView
        var ivMenu: ImageView? = null
        var tvUsername: TextView
        var rlMain: RelativeLayout? = null
        var tvDesc: TextView? = null
        var tv_time: TextView? = null
        var ivUserPic: CircleImageView? = null
        var videoView: JCVideoPlayerSimple? = null
        var avPlayer: AudioPlayerView? = null
        var ivUserProfilePic: CircleImageView? = null
        var tvReaction: TextView? = null
        var relativelayout: FrameLayout?=null

        init {
            relativelayout = view.findViewById(R.id.framelayout)
            tvReaction = view.findViewById(R.id.tv_reaction)
            ivUserProfilePic = view.findViewById(R.id.iv_userprofile)
            avPlayer = view.findViewById(R.id.av_player)
            videoView = view.findViewById(R.id.videoview)
            rlMain = view.findViewById(R.id.rl_main)
            tvUsername = view.findViewById(R.id.tv_username)
            tvDesc = view.findViewById(R.id.tv_desc)
            tv_time = view.findViewById(R.id.tv_time)
            ivUserPic = view.findViewById(R.id.iv_userprofile)
            reactionView = view.findViewById(R.id.view_reaction)
            linLike = view.findViewById(R.id.ll_like)
            ivLike = view.findViewById(R.id.iv_like)
            ivMenu = view.findViewById(R.id.iv_menu)

        }
    }

    fun createPartFromString(string: String): RequestBody {
        return RequestBody.create(
            MultipartBody.FORM, string
        )
    }

}