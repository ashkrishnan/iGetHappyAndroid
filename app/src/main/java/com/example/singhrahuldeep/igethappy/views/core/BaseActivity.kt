package com.example.singhrahuldeep.igethappy.views.core

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.igethappy.R
import com.example.singhrahuldeep.igethappy.di.component.ActivityComponent
import com.example.singhrahuldeep.igethappy.interactors.Core
import com.example.singhrahuldeep.igethappy.interactors.Disposable
import com.example.singhrahuldeep.igethappy.utils.*
import com.google.android.material.snackbar.Snackbar
import java.util.*

/**
 * Created by Gharjyot Singh
 */


abstract class BaseActivity : AppCompatActivity(), Core,
    Disposable {

    var offset: Int? = 0
    var isLoading: Boolean? = false
    var mCurrentFragment: Fragment? = null
    var baseActivity: BaseActivity? = null
    var mSavedInstanceState: Bundle? = null
    var linearLayoutManager: LinearLayoutManager? = null
    private var mActivityComponent: ActivityComponent? = null
    protected var viewDataBinding: ViewDataBinding? = null
    protected var toolbar: Toolbar? = null
    protected var parentLayout: View? = null

    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    public
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO uncomment


        viewDataBinding = DataBindingUtil.setContentView<ViewDataBinding>(this, getLayoutId())

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        mSavedInstanceState = savedInstanceState
        initViews()
        baseActivity = this


        val prefs = SharedPrefsHelper(this@BaseActivity)
        val userId = prefs.get(PreferenceKeys.PREF_USER_ID, "")
        AppConstants.APP_USER_ID = userId!!
    }

    protected fun getActivityComponent(): ActivityComponent? {
        return this.mActivityComponent
    }

    abstract fun getLayoutId(): Int

    abstract fun initViews()

    override fun showDialog() {
        try {
            if (!isFinishing) {
                val string = getString(R.string.please_wait_meassge)
                ProgressUtility.showProgress(this, string)
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

        //  Toast.makeText(this, resources.getString(resId), Toast.LENGTH_SHORT).show()

    }


    override fun showToast(message: String) {


        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    /**
     * @return toolbar title name
     */
    //  protected abstract String getHeaderTitle();
    override fun onDestroy() {
        super.onDestroy()
        try {
            dispose()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * To get android phone id
     *
     * @return String
     */
    @SuppressLint("HardwareIds")
    fun getDeviceId(): String {
        return Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }


    /**
     * This method is used to replace fragment .
     *
     * @param newFragment :replace an existing fragment with new fragment.
     * @param args        :pass bundle data fron one fragment to another fragment
     */
    fun replaceFragment(frameLayout: Int, newFragment: Fragment, args: Bundle?) {
        val manager = supportFragmentManager
        manager.popBackStack()
        if (args != null)
            newFragment.arguments = args
        val transaction = manager.beginTransaction()
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(frameLayout, newFragment)
        transaction.addToBackStack(null)
        // Commit the transaction
        transaction.commit()
    }

    fun replaceFragment(frameLayout: Int, carouselFragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(frameLayout, carouselFragment)
            .commit()
    }

    fun replaceFragment(carouselFragment: Fragment, args: Bundle?): Fragment {
        val fragmentManager = supportFragmentManager
        val fragment = carouselFragment
        fragment.arguments = args
        return fragment
    }

    /**
     * This method is used to replace fragment .
     *
     * @param newFragment :replace an existing fragment with new fragment.
     * @param args        :pass bundle data fron one fragment to another fragment
     */
    fun addFragment(frameLayout: Int, newFragment: Fragment, args: Bundle?) {
        val manager = supportFragmentManager
        // manager.popBackStack();
        if (args != null)
            newFragment.arguments = args
        val transaction = manager.beginTransaction()
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(frameLayout, newFragment)
        transaction.addToBackStack(null)
        // Commit the transaction
        transaction.commit()
    }


    /**
     * This method is used to set toolbar header title
     *
     * @param title:title of tool bar
     */
    fun setHeaderTitle(title: String) {

    }


    @SuppressLint("WrongConstant")
    open fun setAdapter(recyclerView: RecyclerView, mList: ArrayList<*>/*,activity: AppCompatActivity*/) {
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager!!.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = linearLayoutManager!!.getChildCount()
                val totalItemCount = linearLayoutManager!!.getItemCount()
                val firstVisibleItemPosition = linearLayoutManager!!.findFirstVisibleItemPosition()

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= offset!! && firstVisibleItemPosition + 2 > offset!!
                ) {

                    loadMoreItems()

                }
            }
        })
    }

    protected open fun loadMoreItems() {

    }


    fun getmCurrentFragment(): Fragment? {
        return mCurrentFragment
    }

    fun setmCurrentFragment(mCurrentFragment: Fragment) {
        this.mCurrentFragment = mCurrentFragment
    }


    fun openMessageDialog(strTitle: String) {

        val dialog = Dialog(this@BaseActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_alert_dialog)
        dialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvMessage = dialog.findViewById<TextView>(R.id.tv_message) as TextView
        tvMessage.text = strTitle

        val tvCancel = dialog.findViewById<TextView>(R.id.tv_cancel) as TextView
        val tvOk = dialog.findViewById<TextView>(R.id.tv_ok) as TextView
        tvOk.text = resources.getText(R.string.ok)
        tvCancel.visibility = View.GONE

        tvOk.setOnClickListener { view ->

            finish()
            dialog.dismiss()
        }

        dialog.show()
    }


    fun setFontStyle(edittext: EditText) {
        var typeFace: Typeface? = ResourcesCompat.getFont(this.applicationContext, R.font.upcfl_2)
        edittext.setTypeface(typeFace)

    }


    @SuppressLint("MissingPermission")
    fun checkInternet(activity: Context): Boolean {
        val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return if (netInfo != null && netInfo.isConnectedOrConnecting) {
            true
        } else if (netInfo != null && (netInfo.state == NetworkInfo.State.DISCONNECTED || netInfo.state == NetworkInfo.State.DISCONNECTING || netInfo.state == NetworkInfo.State.SUSPENDED || netInfo.state == NetworkInfo.State.UNKNOWN)) {
            false
        } else {
            false
        }
    }

    fun enableLogs() {
        Utils.PRINT_LOGS = true
    }

    fun disableLogs() {
        Utils.PRINT_LOGS = false
    }

    fun printInfoLog(TAG: String, message: String) {
        if (Utils.PRINT_LOGS)
            Log.i(TAG, message)
    }

    fun printDebugLog(TAG: String, message: String) {
        if (Utils.PRINT_LOGS)
            Log.d(TAG, message)

    }

    fun printErrorLog(TAG: String, message: String) {
        if (Utils.PRINT_LOGS)
            Log.e(TAG, message)
    }
}
