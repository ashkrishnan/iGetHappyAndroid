package com.example.singhrahuldeep.igethappy.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.igethappy.R
import com.example.singhrahuldeep.igethappy.interfaces.CareReceiverCallback
import com.example.singhrahuldeep.igethappy.model.input.RelationModel
import com.example.singhrahuldeep.igethappy.views.core.BaseActivity

/**
 * Created by Gharjyot Singh
 */


class HorizontalTextAdapter(
    context: BaseActivity,
    relationArray: ArrayList<RelationModel>,
    callback: CareReceiverCallback
) : RecyclerView.Adapter<HorizontalTextAdapter.MyHolder>() {

    var relationArray: ArrayList<RelationModel>? = null
    var context: Context? = null
    var callback: CareReceiverCallback? = null

    init {
        this.context = context
        this.relationArray = relationArray
        this.callback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemView: View =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_relations, parent, false)
        return HorizontalTextAdapter.MyHolder(itemView)
    }

    override fun getItemCount(): Int {
        return relationArray!!.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        val model = relationArray!!.get(position)

        if (model.isSelected!!) {
            holder.tvRelation.setTextColor(context!!.resources.getColor(R.color.white_color))
            holder.tvRelation.setBackgroundResource(R.drawable.oval_blue_bg)
        } else {
            holder.tvRelation.setTextColor(context!!.resources.getColor(R.color.black))
            holder.tvRelation.setBackgroundResource(R.drawable.oval_transparent_bg)
        }

        holder.tvRelation.text = model.relation

        holder.tvRelation.setOnClickListener {

            callback!!.itemSelected(position)
        }

    }

    fun setData(relationArray: ArrayList<RelationModel>) {
        this.relationArray = relationArray
        notifyDataSetChanged()
    }

    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvRelation: TextView

        init {
            tvRelation = view.findViewById(R.id.tv_relation)
        }
    }

}