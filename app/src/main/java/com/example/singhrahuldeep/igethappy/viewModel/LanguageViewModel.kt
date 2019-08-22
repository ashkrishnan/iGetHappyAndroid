package com.example.singhrahuldeep.igethappy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.singhrahuldeep.igethappy.CallBackResult
import com.example.singhrahuldeep.igethappy.model.output.LanguageResponse
import com.example.singhrahuldeep.igethappy.model.output.SignUpResponse
import com.example.singhrahuldeep.igethappy.repositories.GetLanguageRepository
/**
 * Created by Gharjyot Singh
 */


class LanguageViewModel: ViewModel(){

    var dataResponse: MutableLiveData<LanguageResponse>? = null

    var languageRepo: GetLanguageRepository? = null
    var isLoading = MutableLiveData<Boolean>()

    fun init() {
        languageRepo = GetLanguageRepository()
        languageRepo?.initRepo()
        if (dataResponse != null) return
    }

    fun fetchLanguages(locale: String) {

        isLoading.postValue(true)

        dataResponse = languageRepo!!.fetchLanguages(locale)
    }

    fun getData(): LiveData<LanguageResponse> {

        if (dataResponse == null) {
            dataResponse = MutableLiveData()
        }

        return dataResponse!!
    }


    val loading: LiveData<Boolean>
        get() = isLoading



}