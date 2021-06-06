package com.example.daoyun

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

class CustomToolBar @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr) {
    private var isLeftBtnVisible: Boolean? = null
    private var leftResId = 0
    private var isLeftTvVisible: Boolean? = null
    private var leftTvText: String? = null
    private var isRightBtnVisible: Boolean? = null
    private var rightResId = 0
    private var isRightTvVisible: Boolean? = null
    private var rightTvText: String? = null
    private var isTitleVisible: Boolean? = null
    private var titleText: String? = null
    private var backgroundResId = 0

    /**
     * 初始化属性
     * @param attrs
     */
    fun initView(attrs: AttributeSet?) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CustomToolBar)
        /**-------------获取左边按钮属性------------ */
        isLeftBtnVisible = typedArray.getBoolean(R.styleable.CustomToolBar_left_btn_visible, false)
        leftResId = typedArray.getResourceId(R.styleable.CustomToolBar_left_btn_src, -1)
        /**-------------获取左边文本属性------------ */
        isLeftTvVisible = typedArray.getBoolean(R.styleable.CustomToolBar_left_tv_visible, false)
        if (typedArray.hasValue(R.styleable.CustomToolBar_left_tv_text)) {
            leftTvText = typedArray.getString(R.styleable.CustomToolBar_left_tv_text)
        }
        /**-------------获取右边按钮属性------------ */
        isRightBtnVisible =
            typedArray.getBoolean(R.styleable.CustomToolBar_right_btn_visible, false)
        rightResId = typedArray.getResourceId(R.styleable.CustomToolBar_right_btn_src, -1)
        /**-------------获取右边文本属性------------ */
        isRightTvVisible = typedArray.getBoolean(R.styleable.CustomToolBar_right_tv_visible, false)
        if (typedArray.hasValue(R.styleable.CustomToolBar_right_tv_text)) {
            rightTvText = typedArray.getString(R.styleable.CustomToolBar_right_tv_text)
        }
        /**-------------获取标题属性------------ */
        isTitleVisible = typedArray.getBoolean(R.styleable.CustomToolBar_title_visible, false)
        if (typedArray.hasValue(R.styleable.CustomToolBar_title_text)) {
            titleText = typedArray.getString(R.styleable.CustomToolBar_title_text)
        }
        /**-------------背景颜色------------ */
        backgroundResId = typedArray.getResourceId(R.styleable.CustomToolBar_barBackground, -1)
        typedArray.recycle()
        /**-------------设置内容------------ */
        val barLayoutView =
            View.inflate(context, R.layout.layout_common_toolbar, null)
        val leftBtn =
            barLayoutView.findViewById<View>(R.id.toolbar_left_btn) as Button
        val leftTv =
            barLayoutView.findViewById<View>(R.id.toolbar_left_tv) as TextView
        val titleTv =
            barLayoutView.findViewById<View>(R.id.toolbar_title_tv) as TextView
        val rightBtn =
            barLayoutView.findViewById<View>(R.id.toolbar_right_btn) as Button
        val rightTv =
            barLayoutView.findViewById<View>(R.id.toolbar_right_tv) as TextView
        val barRlyt =
            barLayoutView.findViewById<View>(R.id.toolbar_content_rlyt) as RelativeLayout
        if (isLeftBtnVisible as Boolean) {
            leftBtn.visibility = View.VISIBLE
        }
        if (isLeftTvVisible as Boolean) {
            leftTv.visibility = View.VISIBLE
        }
        if (isRightBtnVisible as Boolean) {
            rightBtn.visibility = View.VISIBLE
        }
        if (isRightTvVisible as Boolean) {
            rightTv.visibility = View.VISIBLE
        }
        if (isTitleVisible as Boolean) {
            titleTv.visibility = View.VISIBLE
        }
        leftTv.text = leftTvText
        rightTv.text = rightTvText
        titleTv.text = titleText
        if (leftResId != -1) {
            leftBtn.setBackgroundResource(leftResId)
        }
        if (rightResId != -1) {
            rightBtn.setBackgroundResource(rightResId)
        }
        if (backgroundResId != -1) {
            barRlyt.setBackgroundColor(resources.getColor(R.color.bg_toolbar))
        }
        //将设置完成之后的View添加到此LinearLayout中
        addView(barLayoutView, 0)
    }

    init {
        initView(attrs)
    }
}

