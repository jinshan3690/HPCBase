package com.hpcang.base.common

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import androidx.work.Configuration
import com.hpcang.base.common.util.SystemUtil
import me.jessyan.autosize.AutoSizeConfig

abstract class BaseApplication : MultiDexApplication(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        innerContext = this
        if (SystemUtil.isMainProcess(getContext())) {
            mainThread()
        } else {
            workThread(SystemUtil.currentProcessName(getContext()))
        }
    }

    /**
     * 主进程回调
     */
    protected open fun mainThread() {
        initAutoSize()
    }

    /**
     * 其他进程回调
     */
    private fun workThread(packageName: String) {}

    companion object {

        var innerContext:Application? = null

        fun getContext():Application{
            return innerContext!!
        }

    }

    private fun initAutoSize() {
        AutoSizeConfig.getInstance().setLog(SystemUtil.isDebug(getContext()))
        AutoSizeConfig.getInstance().isCustomFragment = true
        AutoSizeConfig.getInstance().isExcludeFontScale = true
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}