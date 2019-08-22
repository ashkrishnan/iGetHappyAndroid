package com.example.singhrahuldeep.igethappy.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.android.igethappy.R
import com.example.singhrahuldeep.igethappy.interfaces.CareReceiverCallback
import com.example.singhrahuldeep.igethappy.views.core.BaseActivity

/**
 * Created by Gharjyot Singh
 */


class AvatarImageAdapter(
    context: BaseActivity,
    array: Array<Int>,
    callback: CareReceiverCallback
) : RecyclerView.Adapter<AvatarImageAdapter.MyHolder>() {

    var imagesArray: Array<Int>? = null
    var context: Context? = null
    var callback: CareReceiverCallback? = null

    init {
        this.callback = callback
        this.context = context
        this.imagesArray = array
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemView: View =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_signup_profile_emoji, parent, false)
        return AvatarImageAdapter.MyHolder(itemView)
    }

    override fun getItemCount(): Int {
        return imagesArray!!.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        holder.imageView.setImageResource(imagesArray!![position])
        holder.imageView.setOnClickListener {

            callback!!.setAvatar(position)

        }

    }

    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView

        init {
            imageView = view.findViewById(R.id.image_view)
        }
    }

}