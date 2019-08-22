package com.example.singhrahuldeep.igethappy.views.core

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.android.igethappy.R
import com.example.singhrahuldeep.igethappy.interactors.Core
import com.example.singhrahuldeep.igethappy.interactors.Disposable
import com.example.singhrahuldeep.igethappy.utils.ProgressUtility
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Gharjyot Singh
 */

abstract class BaseFragment : Fragment(), Core, Disposable {

    var baseActivity: BaseActivity? = null
    var rootView: View? = null
    protected var parentLayout: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.rootView = view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUp(view)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            this.baseActivity = context
        }
    }

    protected abstract fun setUp(view: View?)

    override fun showDialog() {
        try {
            if (!activity!!.isFinishing) {
                val string = getString(R.string.please_wait_meassge)
                ProgressUtility.showProgress(activity, string)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    override fun hideDialog() {
        ProgressUtility.dismissProgress()

    }

    override fun showToast(resId: Int) {

        val snackbar = Snackbar.make(parentLayout!!, resources.getString(resId), Snackbar.LENGTH_SHORT)
        snackbar.show()

    }


    override fun showToast(message: String) {

        val snackbar = Snackbar.make(parentLayout!!, message, Snackbar.LENGTH_SHORT)
        snackbar.show()

    }

}
