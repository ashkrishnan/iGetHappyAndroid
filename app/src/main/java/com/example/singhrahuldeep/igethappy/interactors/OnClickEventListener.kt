package com.example.singhrahuldeep.igethappy.interactors

import com.example.singhrahuldeep.igethappy.adapter.CommunityPostAdapter

/**
 * Created by Gharjyot Singh
 */


interface OnClickEventListener
{
    fun onViewClicked(
        pos: Int,
        holder: CommunityPostAdapter.MyHolder
    )
}