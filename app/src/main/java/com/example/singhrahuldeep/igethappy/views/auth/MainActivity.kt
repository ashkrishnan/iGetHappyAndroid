package com.example.singhrahuldeep.igethappy.views.auth

import com.android.igethappy.R
import com.example.singhrahuldeep.igethappy.utils.Utils
import com.example.singhrahuldeep.igethappy.views.core.BaseActivity
import javax.inject.Inject
/**
 * Created by Gharjyot Singh
 */


class MainActivity : BaseActivity() {
    @Inject
    lateinit var validationUtils: Utils

    private var tAG = "MainActivity"
    //  private var binding: ActivityMainBinding?=null

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initViews() {

        //This method is used for intializing components
        baseActivity = this
        //  binding = viewDataBinding as ActivityMainBinding
        //  binding!!.tvMessage.text = "Main Activity"

        printDebugLog(tAG, "In main activity")
        getActivityComponent()?.inject(this)
        validationUtils.getImageBitmap("", "", "")
    }

    override fun dispose() {
        //This method is used for disposing off components
    }

}
