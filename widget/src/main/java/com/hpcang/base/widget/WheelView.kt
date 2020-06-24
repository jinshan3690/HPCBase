package com.hpcang.base.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.hpcang.base.common.extensions.dp2px
import com.hpcang.base.common.extensions.px2sp
import com.hpcang.base.common.extensions.sp2px
import java.util.*

class WheelView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : ScrollView(context, attrs, defStyle) {

    private val textSize: Float
    private val textPadding: Int
    private var minDivider: Int
    private var maxWidth: Int
    private val textColor: Int
    private val textHintColor: Int
    private val borderColor: Int

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.WheelView)
        textSize = px2sp(
            typedArray.getDimension(
                R.styleable.WheelView_wheel_text_size, 20f.sp2px().toFloat()
            )
        )
        textPadding = typedArray.getDimension(
            R.styleable.WheelView_wheel_text_padding, 15f.dp2px().toFloat()
        ).toInt()
        minDivider = typedArray.getDimension(
            R.styleable.WheelView_wheel_min_divider_width, 70f.dp2px().toFloat()
        ).toInt()
        maxWidth = typedArray.getDimension(
            R.styleable.WheelView_wheel_max_width, 0f.dp2px().toFloat()
        ).toInt()
        textColor = typedArray.getColor(
            R.styleable.WheelView_wheel_text_color, resources.getColor(R.color.colorPrimary)
        )
        textHintColor = typedArray.getColor(
            R.styleable.WheelView_wheel_text_hint_color, Color.parseColor("#666666")
        )
        borderColor = typedArray.getColor(
            R.styleable.WheelView_wheel_border_color, resources.getColor(R.color.colorPrimary)
        )
        init()
    }

    //    private ScrollView scrollView;
    private var views: LinearLayout? = null

    //    String[] items;
    private var items: MutableList<String>? = null
    fun getItems(): List<String>? {
        return items
    }

    fun setItems(list: List<String>?) {
        if (null == items) {
            items = ArrayList()
        }
        views!!.removeAllViews()
        items!!.clear()
        items!!.addAll(list!!)

        // 前面和后面补全
        for (i in 0 until offset) {
            items!!.add(0, "")
            items!!.add("")
        }
        initData()
    }

    var offset = 1 // 偏移量（需要在最前面和最后面补全）

    //setItem之前
    fun set(offset: Int) {
        selectedIndex = selectedIndex + offset - this.offset
        this.offset = offset
    }

    private var displayItemCount: Int = 0 // 每页显示的数量 = 0
    var selectedIndex = offset
    private fun init() {

//        this.setOrientation(VERTICAL);
        this.isVerticalScrollBarEnabled = false
        views = LinearLayout(context)
        views!!.orientation = LinearLayout.VERTICAL
        this.addView(views)
        scrollerTask = Runnable {
            val newY = scrollY
            if (initialY - newY == 0) { // stopped
                val remainder = initialY % itemHeight
                val divided = initialY / itemHeight
                if (remainder == 0) {
                    selectedIndex = divided + offset
                    onSelectedCallBack()
                } else {
                    if (remainder > itemHeight / 2) {
                        post {
                            smoothScrollTo(
                                0,
                                initialY - remainder + itemHeight
                            )
                            selectedIndex = divided + offset + 1
                            onSelectedCallBack()
                        }
                    } else {
                        post {
                            smoothScrollTo(0, initialY - remainder)
                            selectedIndex = divided + offset
                            onSelectedCallBack()
                        }
                    }
                }
            } else {
                initialY = scrollY
                postDelayed(scrollerTask, newCheck.toLong())
            }
        }
    }

    var initialY = 0
    var scrollerTask: Runnable? = null
    var newCheck = 50
    fun startScrollerTask() {
        removeCallbacks(scrollerTask)
        initialY = scrollY
        postDelayed(scrollerTask, newCheck.toLong())
    }

    private fun initData() {
        displayItemCount = offset * 2 + 1
        for (i in items!!.indices) {
            views!!.addView(createView(i, items!![i]))
        }
        refreshItemView((selectedIndex - offset) * itemHeight)
    }

    var itemHeight = 0
    private fun createView(index: Int, item: String): TextView {
        val tv = TextView(context)
        tv.tag = index - offset
        if (index - offset >= 0 && index - offset < items!!.size - offset * 2) tv.setOnClickListener { v: View ->
            setSelection(v.tag as Int, false)
            onSelectedCallBack()
        }
        tv.isSingleLine = true
        tv.textSize = textSize
        tv.text = item
        tv.gravity = Gravity.CENTER
        tv.ellipsize = TextUtils.TruncateAt.END
        tv.layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val padding = textPadding
        tv.setPadding(0, padding, 0, padding)
        if (0 == itemHeight) {
            itemHeight = getViewMeasuredHeight(tv)
            views!!.layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                itemHeight * displayItemCount
            )
            val lp = this.layoutParams
            lp.height = itemHeight * displayItemCount
        }
        return tv
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        refreshItemView(t)
        scrollDirection = if (t > oldt) {
            SCROLL_DIRECTION_DOWN
        } else {
            SCROLL_DIRECTION_UP
        }
    }

    private fun refreshItemView(y: Int) {
        var position = y / itemHeight + offset
        val remainder = y % itemHeight
        val divided = y / itemHeight
        if (remainder == 0) {
            position = divided + offset
        } else {
            if (remainder > itemHeight / 2) {
                position = divided + offset + 1
            }
        }
        val childSize = views!!.childCount
        for (i in 0 until childSize) {
            val itemView = views!!.getChildAt(i) as TextView ?: return
            if (position == i) {
                itemView.setTextColor(textColor)
            } else {
                itemView.setTextColor(textHintColor)
            }
        }
    }

    /**
     * 获取选中区域的边界
     */
    private var selectedAreaBorder: IntArray? = null
    private fun obtainSelectedAreaBorder(): IntArray {
        if (null == selectedAreaBorder) {
            selectedAreaBorder = IntArray(2)
            selectedAreaBorder!![0] = itemHeight * offset
            selectedAreaBorder!![1] = itemHeight * (offset + 1)
        }
        return selectedAreaBorder!!
    }

    private var scrollDirection = -1
    var paint: Paint? = null
    var viewWidth = 0
    var dividerWidth = 0
    override fun setBackgroundDrawable(background: Drawable?) {
        var background: Drawable? = background
        dividerWidth = if (minDivider != 0) {
            minDivider
        } else {
            viewWidth * 4 / 6
        }
        if (null == paint) {
            paint = Paint()
            paint!!.color = context!!.resources.getColor(borderColor)
            paint!!.strokeWidth = dip2px(1f).toFloat()
        }
        background = object : Drawable() {
            override fun draw(canvas: Canvas) {
                var offset = 0f
                if (viewWidth > dividerWidth) offset = (viewWidth - dividerWidth) / 2.toFloat()
                canvas.drawLine(
                    offset,
                    obtainSelectedAreaBorder()[0].toFloat(),
                    viewWidth - offset,
                    obtainSelectedAreaBorder()[0].toFloat(),
                    paint!!
                )
                canvas.drawLine(
                    offset,
                    obtainSelectedAreaBorder()[1].toFloat(),
                    viewWidth - offset,
                    obtainSelectedAreaBorder()[1].toFloat(),
                    paint!!
                )
            }

            override fun setAlpha(alpha: Int) {}
            override fun setColorFilter(cf: ColorFilter?) {}
            override fun getOpacity(): Int {
                return PixelFormat.UNKNOWN
            }
        }
        super.setBackgroundDrawable(background)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        var w = w
        val lp = this.layoutParams as ViewGroup.LayoutParams
        if (maxWidth != 0 && w > maxWidth || w < minDivider) {
            w = if (maxWidth > minDivider) maxWidth else minDivider
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT
            lp.width = w
        }
        viewWidth = w
        super.onSizeChanged(w, h, oldw, oldh)
        setBackgroundDrawable(null)
    }

    /**
     * 选中回调
     */
    private fun onSelectedCallBack() {
        if (null != onWheelViewListener) {
            onWheelViewListener!!.onSelected(selectedIndex - offset, items!![selectedIndex])
        }
    }

    fun setSelection(position: Int) {
        setSelection(position, true)
    }

    fun setSelection(position: Int, hasSmooth: Boolean) {
        var position = position
        if (position < 0) position = 0 else if (position > items!!.size - 1 - offset * 2) {
            position = items!!.size - 1 - offset * 2
        }
        val p = position
        selectedIndex = p + offset
        if (hasSmooth) post {
            smoothScrollTo(
                0,
                p * itemHeight
            )
        } else post { scrollTo(0, p * itemHeight) }
        onSelectedCallBack()
    }

    val selectedItem: String
        get() = items!![selectedIndex]

    val seletedIndex: Int
        get() = selectedIndex - offset

    override fun fling(velocityY: Int) {
        super.fling(velocityY / 3)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_UP) {
            startScrollerTask()
        }
        return super.onTouchEvent(ev)
    }

    var onWheelViewListener: OnWheelViewListener? = null

    private fun dip2px(dpValue: Float): Int {
        val scale = context!!.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    private fun getViewMeasuredHeight(view: View): Int {
        val width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val expandSpec =
            MeasureSpec.makeMeasureSpec(Int.MAX_VALUE shr 2, MeasureSpec.AT_MOST)
        view.measure(width, expandSpec)
        return view.measuredHeight
    }

    fun setMinDivider(minDivider: Int) {
        this.minDivider = minDivider
    }

    fun setMaxWidth(maxWidth: Int) {
        this.maxWidth = maxWidth
    }

    class OnWheelViewListener {
        fun onSelected(selectedIndex: Int, item: String?) {}
    }

    private val SCROLL_DIRECTION_UP = 0
    private val SCROLL_DIRECTION_DOWN = 1
}