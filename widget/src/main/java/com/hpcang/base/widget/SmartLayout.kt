package com.hpcang.base.widget

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.StateListAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import kotlin.math.max
import kotlin.math.min


class SmartLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var paint = Paint()
    private var clipPath = Path()
    private var cornerRadiusArray = FloatArray(8)
    private var layer = RectF()

    private var stroke: Int = 0
    private var strokeColor: Int = 0
    private var color: Int = 0
    private var endColor: Int = 0
    private var orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.LEFT_RIGHT
    private var rippleColor: Int = 0
    private var shadowColor: Int = 0
    private var maskDrawable: Drawable? = null
    private var disableColor: Int = 0
    private var selectedStrokeColor: Int = 0
    private var selectedColor: Int = 0
    private var selectedEndColor: Int = 0
    private var shape: Int = GradientDrawable.RECTANGLE
    private var defaultStateListAnim: Boolean = false
    private var stateListAnim: StateListAnimator? = null
    private var defaultDuration: Long = 200
    private var elevationNormal: Float = 0f
    private var elevationPressed: Float = 0f
    private var translationZNormal: Float = 0f
    private var translationZPressed: Float = 0f

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmartLayout)
        init(typedArray)
        typedArray.recycle()
    }

    @SuppressLint("AnimatorKeep")
    private fun init(typedArray: TypedArray) {
        color = typedArray.getColor(R.styleable.SmartLayout_sl_color, Color.TRANSPARENT)
        endColor = typedArray.getColor(R.styleable.SmartLayout_sl_endColor, -999)
        orientation = when (typedArray.getInt(R.styleable.SmartLayout_sl_orientation, 6)) {
            0 -> GradientDrawable.Orientation.TOP_BOTTOM
            1 -> GradientDrawable.Orientation.TR_BL
            2 -> GradientDrawable.Orientation.RIGHT_LEFT
            3 -> GradientDrawable.Orientation.BR_TL
            4 -> GradientDrawable.Orientation.BOTTOM_TOP
            5 -> GradientDrawable.Orientation.BL_TR
            6 -> GradientDrawable.Orientation.LEFT_RIGHT
            else -> GradientDrawable.Orientation.TL_BR
        }
        disableColor = typedArray.getColor(
            R.styleable.SmartLayout_sl_disableColor, Color.parseColor("#CCCCCC")
        )
        rippleColor = typedArray.getColor(
            R.styleable.SmartLayout_sl_rippleColor, getColorWithAlpha(0.08f, Color.BLACK)
        )
        shadowColor = typedArray.getColor(
            R.styleable.SmartLayout_sl_shadowColor, Color.BLACK
        )
        maskDrawable = typedArray.getDrawable(R.styleable.SmartLayout_sl_maskDrawable)
        shape = typedArray.getInt(R.styleable.SmartLayout_sl_shape, 0)
        stroke = typedArray.getDimension(R.styleable.SmartLayout_sl_stroke, 0f).toInt()
        strokeColor = typedArray.getColor(
            R.styleable.SmartLayout_sl_strokeColor, Color.parseColor("#DEDEDE")
        )
        selectedColor = typedArray.getColor(
            R.styleable.SmartLayout_sl_selectedColor, -999
        )
        selectedEndColor = typedArray.getColor(
            R.styleable.SmartLayout_sl_selectedEndColor, -999
        )
        selectedStrokeColor = typedArray.getColor(
            R.styleable.SmartLayout_sl_selectedStrokeColor, -999
        )
        val radius = typedArray.getDimension(R.styleable.SmartLayout_sl_radius, 0f)
        val radiusLeftTop =
            typedArray.getDimension(R.styleable.SmartLayout_sl_radiusLeftTop, radius)
        val radiusRightTop =
            typedArray.getDimension(R.styleable.SmartLayout_sl_radiusRightTop, radius)
        val radiusLeftBottom =
            typedArray.getDimension(R.styleable.SmartLayout_sl_radiusLeftBottom, radius)
        val radiusRightBottom =
            typedArray.getDimension(R.styleable.SmartLayout_sl_radiusRightBottom, radius)

        elevationNormal = typedArray.getDimension(R.styleable.SmartLayout_sl_elevationNormal, 0f)
        translationZNormal =
            typedArray.getDimension(R.styleable.SmartLayout_sl_translationZNormal, 0f)
        elevationPressed =
            typedArray.getDimension(R.styleable.SmartLayout_sl_elevationPressed, elevationNormal)
        translationZPressed = typedArray.getDimension(
            R.styleable.SmartLayout_sl_translationZPressed,
            translationZNormal
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = elevationNormal
            translationZ = translationZNormal

            val stateAnim = typedArray.getResourceId(R.styleable.SmartLayout_sl_stateListAnim, 0)
            if (stateAnim != 0)
                stateListAnim = AnimatorInflater.loadStateListAnimator(context, stateAnim)
        }

        isEnabled = typedArray.getBoolean(R.styleable.SmartLayout_sl_enabled, true)
        defaultStateListAnim =
            typedArray.getBoolean(R.styleable.SmartLayout_sl_defaultStateListAnim, false)

        cornerRadiusArray.run {
            set(0, radiusLeftTop)
            set(1, radiusLeftTop)

            set(2, radiusRightTop)
            set(3, radiusRightTop)

            set(4, radiusRightBottom)
            set(5, radiusRightBottom)

            set(6, radiusLeftBottom)
            set(7, radiusLeftBottom)
        }

        initBackground()
        initStateListAnim()
        initClip()
    }

    private fun initBackground() {
        val backgroundDrawable = createDrawable(
            cornerRadiusArray, shape, stroke, strokeColor, color, endColor
        )

        val disableDrawable = createDrawable(
            cornerRadiusArray, shape, stroke, strokeColor, disableColor
        )

        val stateListDrawable = StateListDrawable()
        stateListDrawable.addState(intArrayOf(-android.R.attr.state_enabled), disableDrawable)
        if (selectedColor != -999 || selectedStrokeColor != -999) {
            if (selectedColor == -999)
                selectedColor = Color.TRANSPARENT
            if (selectedStrokeColor == -999)
                selectedStrokeColor = Color.TRANSPARENT
            val selectedDrawable = createDrawable(
                cornerRadiusArray, shape, stroke,
                selectedStrokeColor, selectedColor, selectedEndColor
            )
            stateListDrawable.addState(
                intArrayOf(android.R.attr.state_enabled, android.R.attr.state_selected),
                selectedDrawable
            )
            stateListDrawable.addState(
                intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_selected),
                backgroundDrawable
            )
        } else {
            stateListDrawable.addState(intArrayOf(android.R.attr.state_enabled), backgroundDrawable)
        }

        background = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                foreground = RippleDrawable(
                    ColorStateList.valueOf(rippleColor), null, maskDrawable ?: backgroundDrawable
                )
                stateListDrawable
            } else {
                val rippleDrawable = RippleDrawable(
                    ColorStateList.valueOf(rippleColor), stateListDrawable, maskDrawable
                )
                rippleDrawable
            }
        } else {
            stateListDrawable
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            outlineSpotShadowColor = shadowColor
        }
    }

    private fun initStateListAnim() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (stateListAnim == null && defaultStateListAnim) {
                val xPressedAnim = ObjectAnimator.ofFloat(this, View.SCALE_X, 1.0f, 1.025f).apply {
                    duration = defaultDuration
                }
                val yPressedAnim = ObjectAnimator.ofFloat(this, View.SCALE_Y, 1.0f, 1.025f).apply {
                    duration = defaultDuration
                }
                val zPressedAnim = ObjectAnimator.ofFloat(
                    this, View.TRANSLATION_Z, translationZNormal, translationZPressed
                )
                    .apply {
                        duration = defaultDuration
                    }
                val ePressedAnim = ObjectAnimator.ofFloat(
                    this, "elevation", elevationNormal, elevationPressed
                )
                    .apply {
                        duration = defaultDuration
                    }
                val xNormalAnim = ObjectAnimator.ofFloat(this, View.SCALE_X, 1.0f).apply {
                    duration = defaultDuration
                }
                val yNormalAnim = ObjectAnimator.ofFloat(this, View.SCALE_Y, 1.0f).apply {
                    duration = defaultDuration
                }
                val zNormalAnim =
                    ObjectAnimator.ofFloat(this, View.TRANSLATION_Z, translationZNormal).apply {
                        duration = defaultDuration
                    }
                val eNormalAnim = ObjectAnimator.ofFloat(this, "elevation", elevationNormal).apply {
                    duration = defaultDuration
                }

                stateListAnim = StateListAnimator()
                val pressedAnim = AnimatorSet().apply {
                    play(xPressedAnim).with(yPressedAnim).with(zPressedAnim).with(ePressedAnim)
                }
                val normalAnim = AnimatorSet().apply {
                    play(xNormalAnim).with(yNormalAnim).with(zNormalAnim).with(eNormalAnim)
                }

                stateListAnim?.addState(intArrayOf(android.R.attr.state_pressed), pressedAnim)
                stateListAnim?.addState(intArrayOf(-android.R.attr.state_pressed), normalAnim)
            }
            stateListAnimator = stateListAnim
        }
    }

    private fun initClip() {
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null)//关掉硬件加速
        paint.isAntiAlias = true
    }

    /**
     * 裁剪子View
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        layer.set(0f, 0f, w.toFloat(), h.toFloat())
        val areas = RectF()
        areas.left = paddingLeft.toFloat() + stroke
        areas.top = paddingTop.toFloat() + stroke
        areas.right = w - paddingRight.toFloat() - stroke
        areas.bottom = h - paddingBottom.toFloat() - stroke
        clipPath.reset()
        clipPath.addRoundRect(areas, cornerRadiusArray, Path.Direction.CW)
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.saveLayer(layer, null, Canvas.ALL_SAVE_FLAG)

        super.dispatchDraw(canvas)
        onClipDraw(canvas)

        canvas.restore()
    }

    private fun onClipDraw(canvas: Canvas) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
            canvas.drawPath(clipPath, paint)
        } else {
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
            val path = Path()
            path.addRect(
                0f, 0f, layer.width(), layer.height(), Path.Direction.CW
            )
            path.op(clipPath, Path.Op.DIFFERENCE)
            canvas.drawPath(path, paint)
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener { v ->
            l?.run {
                isSelected = !isSelected
                onClick(v)
            }
        }
    }

    /**
     * 只能拥有一个子View
     */
//    override fun onFinishInflate() {
//        super.onFinishInflate()
//        val count = super.getChildCount()
//        if (count > 1) {
//            throw RuntimeException("最多只支持1个子View，Most only support three sub view")
//        }
//    }

    /**
     * 获取背景
     */
    private fun createDrawable(
        cornerRadiusArray: FloatArray, shape: Int, stroke: Int, strokeColor: Int, vararg color: Int
    ): GradientDrawable {
        val colors = color.filter { it != -999 }.toIntArray()
        val drawable = GradientDrawable()
        if (colors.size > 1) {
            drawable.orientation = orientation
            drawable.colors = colors
        } else {
            drawable.setColor(colors[0])
        }
        drawable.setStroke(stroke, strokeColor)
        drawable.cornerRadii = cornerRadiusArray
        val radius = cornerRadiusArray.filter { it > 0 }
        if (radius.isNotEmpty()) {
            drawable.gradientRadius = radius.first()
        }
        //必须最后设置
        drawable.shape = shape
        return drawable
    }

    /**
     * 给color添加透明度
     * @param alpha 透明度 0f～1f
     * @param baseColor 基本颜色
     * @return
     */
    private fun getColorWithAlpha(alpha: Float, baseColor: Int): Int {
        val a = min(255, max(0, (alpha * 255).toInt())) shl 24
        val rgb = 0x00ffffff and baseColor
        return a + rgb
    }

}
