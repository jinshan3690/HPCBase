package com.hpcang.base

import android.app.Application
import com.hpcang.base.common.BaseApplication
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApp : BaseApplication()