/*
 * Copyright (c) 2014,KJFrameForAndroid Open Source Project,张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpcang.base.common.util

import android.app.Activity
import android.content.Context
import java.util.*

class AcStack private constructor() {

    /**
     * 获取当前Activity栈中元素个数
     */
    val count: Int
        get() = activityStack?.size?:0

    /**
     * 添加Activity到栈
     */
    fun addActivity(activity: Activity?) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack?.add(activity)
    }

    /**
     * 获取当前Activity（栈顶Activity）
     */
    fun topActivity(): Activity? {
        if (activityStack == null) {
            throw NullPointerException(
                "Activity stack is Null,your Activity must extend KJActivity"
            )
        }
        if (activityStack!!.isEmpty()) {
            return null
        }
        return activityStack?.lastElement()
    }

    /**
     * 获取当前Activity（栈顶Activity） 没有找到则返回null
     */
    fun findActivity(cls: Class<*>): Activity? {
        var activity: Activity? = null
        if(activityStack == null)
            return activity

        for (aty in activityStack!!) {
            if (aty?.javaClass == cls) {
                activity = aty
                break
            }
        }
        return activity
    }

    /**
     * 结束当前Activity（栈顶Activity）
     */
    fun finishActivity() {
        val activity = activityStack?.lastElement()
        finishActivity(activity)
    }

    /**
     * 结束指定的Activity(重载)
     */
    fun finishActivity(activity: Activity?) {
        if (activity != null) {
            activityStack?.remove(activity)
            activity.finish() //此处不用finish
        }
    }

    fun removeActivity(activity: Activity?) {
        if (activity != null) {
            activityStack?.remove(activity)
        }
    }

    /**
     * 结束指定的Activity(重载)
     */
    fun finishActivity(cls: Class<*>) {
        var i = 0
        val size = activityStack?.size?:0
        while (i < size) {
            if (activityStack!![i].javaClass == cls) {
                activityStack!![i].finish()
            }
            i++
        }
    }

    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     *
     * @param cls
     */
    fun finishOthersActivity(cls: Class<*>) {
        var i = 0
        val size = activityStack?.size?:0
        while (i < size) {
            if (activityStack!![i].javaClass != cls) {
                activityStack!![i].finish()
            }
            i++
        }
    }

    fun finishOthersActivity(className: String) {
        var i = 0
        val size = activityStack?.size?:0
        while (i < size) {
            if (activityStack!![i].javaClass.simpleName != className) {
                activityStack!![i].finish()
            }
            i++
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        var i = 0
        val size = activityStack?.size?:0
        while (i < size) {
            if (null != activityStack!![i]) {
                activityStack!![i].finish()
            }
            i++
        }
        activityStack?.clear()
    }

    /**
     * 应用程序退出
     */
    fun appExit(context: Context?) {
        try {
            finishAllActivity()
            Runtime.getRuntime().exit(0)
        } catch (e: Exception) {
            Runtime.getRuntime().exit(-1)
        }
    }

    companion object {
        private var activityStack: Stack<Activity>? = null
        private val instance = AcStack()
        @JvmStatic
        fun create(): AcStack {
            return instance
        }
    }
}