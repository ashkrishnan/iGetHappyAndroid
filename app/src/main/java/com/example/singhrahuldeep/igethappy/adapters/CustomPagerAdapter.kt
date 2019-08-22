package com.example.singhrahuldeep.igethappy.adapters

import android.content.Context
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.igethappy.R

/**
 * Created by Gharjyot Singh
 */


class CustomPagerAdapter(private val mContext: Context, private val mStringArray: Array<String>) : PagerAdapter() {
    internal var mLayoutInflater: LayoutInflater

    init {
        mLayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false)
        val textView = itemView.findViewById(R.id.tV_pager) as TextView
        textView.text = mStringArray[position]
        // imageView.setImageResource(mResources[position])
        /* LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(950, 950);
            imageView.setLayoutParams(layoutParams);*/
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return mStringArray.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}