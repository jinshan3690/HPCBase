package com.hpcang.base.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NoScrollViewPager @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

    var noScroll: Boolean = false
    private val smoothScroll: Boolean
    private var intercept = false
    private var lastX = 0
    private var lastY = 0
    private val autoHeight: Boolean

    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.NoScrollViewPager)
        autoHeight = typedArray.getBoolean(R.styleable.NoScrollViewPager_no_autoHeight, false)
        noScroll = typedArray.getBoolean(R.styleable.NoScrollViewPager_no_scroll, false)
        smoothScroll = typedArray.getBoolean(R.styleable.NoScrollViewPager_no_smoothScroll, false)
    }

    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item, !smoothScroll)
    }

    override fun scrollTo(x: Int, y: Int) {
        super.scrollTo(x, y)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (!noScroll) {
            val x = ev.x.toInt()
            val y = ev.y.toInt()
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastY = y
                    lastX = x
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = lastX - x
                    val dy = lastY - y

                    //判断是否是向上滑动和是否在第一屏
                    if (Math.abs(dx) > Math.abs(dy) || intercept) {
                        parent.requestDisallowInterceptTouchEvent(true)
                    } else {
                        parent.requestDisallowInterceptTouchEvent(false)
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (noScroll) return false else try {
            return super.onTouchEvent(event)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {
        if (noScroll) return false else try {
            return super.onInterceptTouchEvent(arg0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun setIntercept(intercept: Boolean) {
        this.intercept = intercept
    }

    /**
     * 自适应高度
     */
    private var measureHeight :Int = 0
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        val child = getChildAt(currentItem)
        if (child != null && autoHeight) {
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            measureHeight = child.measuredHeight
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(measureHeight, MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun resetHeight() {
        if (!autoHeight) return
        val child = getChildAt(currentItem)
        if (child != null) {
            val view = getChildAt(currentItem)
            val height = view.measuredHeight
            val layoutParams = layoutParams as MarginLayoutParams
            layoutParams.height = measureHeight
            setLayoutParams(layoutParams)
        }
    }
}