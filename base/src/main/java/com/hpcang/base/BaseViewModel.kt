package com.hpcang.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    @JvmField
    var finishLiveData = MutableLiveData<Void?>()

    @JvmField
    var showLoadingLiveData = MutableLiveData<Void?>()

    @JvmField
    var hideLoadingLiveData = MutableLiveData<Void?>()

    @JvmField
    var httpErrorLiveData = MutableLiveData<Map<String, Any?>>()

    fun showLoading() {
        showLoadingLiveData.value = null
    }

    fun hideLoading() {
        hideLoadingLiveData.value = null
    }

    fun httpError(message: String?, type: Int, data: Any?) {
        val params: MutableMap<String, Any?> = HashMap()
        params[ParameterField.MESSAGE] = message
        params[ParameterField.TYPE] = type
        params[ParameterField.DATA] = data
        httpErrorLiveData.value = params
    }

    /**
     * 关闭界面
     */
    fun finish() {
        finishLiveData.value = null
    }

    object ParameterField {
        @JvmField
        var CLASS = "CLASS"

        @JvmField
        var BUNDLE = "BUNDLE"

        @JvmField
        var MESSAGE = "MESSAGE"

        @JvmField
        var TYPE = "TYPE"

        @JvmField
        var DATA = "DATA"
    }

}