package com.example.singhrahuldeep.igethappy.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.android.igethappy.R
import com.example.singhrahuldeep.igethappy.interfaces.CareReceiverCallback
import com.example.singhrahuldeep.igethappy.model.output.AddCareReceiverResponse
import com.example.singhrahuldeep.igethappy.views.core.BaseActivity
import com.squareup.picasso.Picasso

/**
 * Created by Gharjyot Singh
 */


class ImageAdapter(
    context: BaseActivity,
    careReceiverArray: ArrayList<AddCareReceiverResponse.Data>,
    callback: CareReceiverCallback
) : RecyclerView.Adapter<ImageAdapter.MyHolder>() {

    var careReceiverArray: ArrayList<AddCareReceiverResponse.Data>? = null
    var context: Context? = null
    var callback: CareReceiverCallback? = null

    init {
        this.callback = callback
        this.context = context
        this.careReceiverArray = careReceiverArray
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemView: View =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_signup_profile_emoji, parent, false)
        return ImageAdapter.MyHolder(itemView)
    }

    override fun getItemCount(): Int {
        return careReceiverArray!!.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        holder.ivDelete.visibility = View.VISIBLE
        Picasso.with(context).load(careReceiverArray!!.get(position).profile_image).fit().centerCrop()
            .placeholder(R.drawable.ic_user_profle)
            .error(R.drawable.ic_user_profle)
            .into(holder.imageView)
//        Glide.with(context!!).clear(holder.imageView)
//        Glide.with(context!!).load(careReceiverArray!!.get(position).profile_image).into(holder.imageView)

        holder.ivDelete.setOnClickListener {

            callback!!.deleteCareReceiver(position)
        }

    }

    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView
        var ivDelete: ImageView

        init {
            imageView = view.findViewById(R.id.image_view)
            ivDelete = view.findViewById(R.id.iv_delete)
        }
    }

}