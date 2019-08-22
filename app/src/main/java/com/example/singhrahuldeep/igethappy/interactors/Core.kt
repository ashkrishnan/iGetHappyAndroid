package com.example.singhrahuldeep.igethappy.interactors

/**
 * Created by Gharjyot Singh
 */


interface Core {
    abstract fun showToast(resId: Int)

    abstract fun showToast(message: String)

    abstract fun showDialog()

    abstract fun hideDialog()

}