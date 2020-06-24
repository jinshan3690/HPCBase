package com.hpcang.base.common.util

import android.R
import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup

/**
 * @describe: 监听软件盘
 * @other:
 */
class SoftKeyBoardListener private constructor(//activity的根视图
    private val rootView: View
) {
    private var rootViewVisibleHeight:Int = 0 //纪录根视图的显示高度 = 0
    private var rootViewVisibleHeight1:Int = 0 //纪录根视图的显示高度 = 0
    private val frameLayoutParams: ViewGroup.LayoutParams = rootView.layoutParams
    private var onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener? =
        null

    fun setListener(onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener?) {
        this.onSoftKeyBoardChangeListener = onSoftKeyBoardChangeListener
    }

    interface OnSoftKeyBoardChangeListener {
        fun keyBoardShow(height: Int)
        fun keyBoardHide(height: Int)
    }

    companion object {
        fun getInstance(activity: Activity): SoftKeyBoardListener {
            return SoftKeyBoardListener(
                activity.window.decorView
                    .findViewById(R.id.content)
            )
        }

        fun getInstance(view: View): SoftKeyBoardListener {
            return SoftKeyBoardListener(view)
        }
    }

    init {
        //获取activity的根视图
        //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
        rootView.viewTreeObserver.addOnGlobalLayoutListener {

            //获取当前根视图在屏幕上显示的大小
            val r = Rect()
            rootView.getWindowVisibleDisplayFrame(r)
            val visibleHeight = r.height()
            if (rootViewVisibleHeight == 0) {
                rootViewVisibleHeight = visibleHeight
                return@addOnGlobalLayoutListener
            }

            //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
            if (rootViewVisibleHeight == visibleHeight) {
                return@addOnGlobalLayoutListener
            }
            val usableHeightNow = r.bottom
            if (usableHeightNow != rootViewVisibleHeight1) {
                //如果两次高度不一致
                //将计算的可视高度设置成视图的高度
                frameLayoutParams.height = usableHeightNow
                rootView.requestLayout() //请求重新布局
                rootViewVisibleHeight1 = usableHeightNow
            }

            //根视图显示高度变小超过200，可以看作软键盘显示了
            if (rootViewVisibleHeight - visibleHeight > 200) {
                if (onSoftKeyBoardChangeListener != null) {
                    onSoftKeyBoardChangeListener!!.keyBoardShow(rootViewVisibleHeight - visibleHeight)
                }
                rootViewVisibleHeight = visibleHeight
                return@addOnGlobalLayoutListener
            }

            //根视图显示高度变大超过200，可以看作软键盘隐藏了
            if (visibleHeight - rootViewVisibleHeight > 200) {
                if (onSoftKeyBoardChangeListener != null) {
                    onSoftKeyBoardChangeListener!!.keyBoardHide(visibleHeight - rootViewVisibleHeight)
                }
                rootViewVisibleHeight = visibleHeight
                return@addOnGlobalLayoutListener
            }
        }
    }
}