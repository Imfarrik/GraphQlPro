package com.example.fortest.ui.custom_view

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.PopupWindow
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.example.fortest.R
import com.google.android.material.textfield.TextInputEditText

class MaterialSpinnerView : TextInputEditText {

    private var mItemSelectedListener: OnItemSelectedListener? = null
    private var mAdapter: MaterialSpinnerAdapter? = null
    private var mPopupWindow: PopupWindow? = null
    private var mListView: ListView? = null
    private var mArrowDrawable: Drawable? = null
    private var mHint: String? = null
    private var mArrowColor = 0
    private var mArrowColorDisabled = 0
    private var mArrowHidden = false
    private var mPopupWindowMaxHeight = 0
    private var mPopupWindowHeight = 0
    private var mPopupItemPaddingLeft = 0
    private var mPopupItemPaddingTop = 0
    private var mPopupItemPaddingRight = 0
    private var mPopupItemPaddingBottom = 0
    private var mPopupItemTextColor = 0
    private var mPopupWindowElevation = 0

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    fun lighter(color: Int, factor: Float): Int {
        val red = ((Color.red(color) * (1 - factor) / 255 + factor) * 255).toInt()
        val green = ((Color.green(color) * (1 - factor) / 255 + factor) * 255).toInt()
        val blue = ((Color.blue(color) * (1 - factor) / 255 + factor) * 255).toInt()
        return Color.argb(Color.alpha(color), red, green, blue)
    }

    fun isRtl(context: Context): Boolean {
        return context.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        keyListener = null

        val attr = context.obtainStyledAttributes(attrs, R.styleable.MaterialSpinnerView)
        val defaultColor = textColors.defaultColor
        val rtl = isRtl(context)
        try {
            mHint = attr.getString(R.styleable.MaterialSpinnerView_textHint) ?: ""
            mArrowColor = attr.getColor(R.styleable.MaterialSpinnerView_arrowColor, defaultColor)
            mArrowColorDisabled = lighter(mArrowColor, 0.8f)
            mArrowHidden = attr.getBoolean(R.styleable.MaterialSpinnerView_arrowHidden, false)
            mPopupWindowHeight = attr.getLayoutDimension(R.styleable.MaterialSpinnerView_popup_height, WindowManager.LayoutParams.WRAP_CONTENT)
            mPopupWindowMaxHeight = attr.getDimensionPixelSize(R.styleable.MaterialSpinnerView_popup_maxHeight, 0)
            mPopupItemPaddingLeft = attr.getDimensionPixelSize(R.styleable.MaterialSpinnerView_popup_itemPaddingLeft, 0)
            mPopupItemPaddingTop = attr.getDimensionPixelSize(R.styleable.MaterialSpinnerView_popup_itemPaddingTop, 0)
            mPopupItemPaddingRight = attr.getDimensionPixelSize(R.styleable.MaterialSpinnerView_popup_itemPaddingRight, 0)
            mPopupItemPaddingBottom = attr.getDimensionPixelSize(R.styleable.MaterialSpinnerView_popup_itemPaddingBottom, 0)
            mPopupItemTextColor = attr.getColor(R.styleable.MaterialSpinnerView_popup_itemTextColor, defaultColor)
            mPopupWindowElevation = attr.getDimensionPixelSize(R.styleable.MaterialSpinnerView_popup_elevation, 0)
            mArrowDrawable = attr.getDrawable(R.styleable.MaterialSpinnerView_arrowDrawable)?.mutate()
        } finally {
            attr.recycle()
        }

        isClickable = true

        if (rtl) {
            layoutDirection = LAYOUT_DIRECTION_RTL
            textDirection = TEXT_DIRECTION_RTL
        }

        if (!mArrowHidden) {
            if (mArrowDrawable == null) {
                mArrowDrawable = ContextCompat.getDrawable(context, R.drawable.rotate_arrow)?.mutate()
                mArrowDrawable?.colorFilter = PorterDuffColorFilter(mArrowColor, PorterDuff.Mode.SRC_IN)
            }
            val drawables = compoundDrawables
            if (rtl) {
                drawables[0] = mArrowDrawable
            } else {
                drawables[2] = mArrowDrawable
            }
            setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], drawables[3])
        }
        mListView = ListView(context)
        mListView?.id = id
        mListView?.divider = ContextCompat.getDrawable(context, R.drawable.divider_material_spinner_items)
        mListView?.onItemClickListener = OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
            mAdapter?.notifyItemSelected(position)
            setText(mAdapter?.getSelectedItem().toString())
            hint = ""
            collapse()
            val selectedItem = mAdapter?.getSelectedItem()
            if (selectedItem != null) mItemSelectedListener?.onItemSelected(position, selectedItem)
        }

        mPopupWindow = PopupWindow(context)
        mPopupWindow?.contentView = mListView
        mPopupWindow?.isOutsideTouchable = true
        mPopupWindow?.isFocusable = true
        mPopupWindow?.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rect_stroke_white_radius12))
        mPopupWindow?.elevation = mPopupWindowElevation.toFloat()
        mPopupWindow?.setOnDismissListener {
            if (!mArrowHidden) {
                animateArrow(false)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mPopupWindow?.width = MeasureSpec.getSize(widthMeasureSpec)
        mPopupWindow?.height = calculatePopupWindowHeight()
        if (mAdapter == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        } else {
            val currentText: CharSequence = text as? CharSequence ?: ""
            var longestItem = currentText.toString()
            for (i in 0 until mAdapter!!.count) {
                val itemText = mAdapter!!.getItemText(i)
                if (itemText.length > longestItem.length) {
                    longestItem = itemText
                }
            }
            setText(longestItem)
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            setText(currentText)
            hint = mHint
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            if (isEnabled && isClickable) {
                val isPopupShowing = mPopupWindow?.isShowing ?: false
                if (isPopupShowing) {
                    collapse()
                } else {
                    expand()
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable("state", super.onSaveInstanceState())
        if (mPopupWindow != null) {
            bundle.putBoolean("is_popup_showing", mPopupWindow?.isShowing ?: false)
            collapse()
        } else {
            bundle.putBoolean("is_popup_showing", false)
        }
        return bundle
    }

    override fun onRestoreInstanceState(savedState: Parcelable) {
        if (savedState is Bundle) {
            hint = if ((mAdapter?.getSelectedIndex() ?: -1) >= 0) {
                setText(mAdapter?.getSelectedItem().toString())
                ""
            } else {
                setText("")
                mHint
            }
            if (savedState.getBoolean("is_popup_showing") && mPopupWindow != null) {
                post { expand() }
            }
            super.onRestoreInstanceState(savedState.getParcelable("state"))
        } else {
            super.onRestoreInstanceState(savedState)
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (mArrowColor > 0) {
            mArrowDrawable?.colorFilter = PorterDuffColorFilter(if (enabled) mArrowColor else mArrowColorDisabled, PorterDuff.Mode.SRC_IN)
        }
    }


    /**
     * @return the selected item position
     */
    fun getSelectedIndex() = mAdapter?.getSelectedIndex() ?: -1

    /**
     * Set the default spinner item using its index
     *
     * @param position the item's position
     */
    fun setSelectedIndex(position: Int) {
        requireNotNull(mAdapter) { "Adapter must not be null!" }
        require(position >= 0) { "Position must be bigger than or equals zero!" }
        require(position < (mAdapter?.count ?: 0)) { "Position must be lower than adapter count!" }
        mAdapter?.notifyItemSelected(position)
        setText(mAdapter?.getSelectedItem().toString())
        hint = ""
        val selectedItem = mAdapter?.getSelectedItem()
        if (selectedItem != null) mItemSelectedListener?.onItemSelected(position, selectedItem)
    }

    /**
     * Register a callback to be invoked when an item in the dropdown is selected.
     *
     * @param onItemSelectedListener The callback that will run
     */
    fun setOnItemSelectedListener(onItemSelectedListener: OnItemSelectedListener) {
        mItemSelectedListener = onItemSelectedListener
    }

    /**
     * Set the custom adapter for the dropdown items
     *
     * @param adapter The adapter
     */
    fun setAdapter(adapter: MaterialSpinnerAdapter) {
        mAdapter = adapter.setTextColor(mPopupItemTextColor).setPopupPadding(mPopupItemPaddingLeft, mPopupItemPaddingTop, mPopupItemPaddingRight, mPopupItemPaddingBottom)
        setAdapterInternal(mAdapter!!)
    }

    private fun setAdapterInternal(adapter: MaterialSpinnerAdapter) {
        val shouldResetPopupHeight = mListView?.adapter != null
        mListView?.adapter = adapter
        hint = if (adapter.getSelectedIndex() >= 0) {
            setText(adapter.getSelectedItem().toString())
            ""
        } else {
            setText("")
            mHint
        }
        if (shouldResetPopupHeight) {
            mPopupWindow?.height = calculatePopupWindowHeight()
        }
    }

    /**
     * Get the list of items in the adapter
     *
     * @return A list of items or `null` if no items are set.
     */
    fun getItems(): List<Any>? {
        return if (mAdapter == null) {
            null
        } else mAdapter?.items
    }

    /**
     * Set the dropdown items
     *
     * @param items A list of items
     */
    @SafeVarargs
    fun setItems(vararg items: Any) {
        setItems(listOf(*items))
    }

    /**
     * Set the dropdown items
     *
     * @param items A list of items
     */
    fun setItems(items: List<Any>) {
        mAdapter = MaterialSpinnerAdapter(context, items)
            .setTextColor(mPopupItemTextColor)
            .setPopupPadding(mPopupItemPaddingLeft, mPopupItemPaddingTop, mPopupItemPaddingRight, mPopupItemPaddingBottom)
        setAdapterInternal(mAdapter!!)
    }

    /**
     * Show the dropdown menu
     */
    private fun expand() {
        if (canShowPopup()) {
            if (!mArrowHidden) {
                animateArrow(true)
            }
            mPopupWindow?.showAsDropDown(this, 0, 16.toPx())
        }
    }

    /**
     * Closes the dropdown menu
     */
    private fun collapse() {
        if (!mArrowHidden) {
            animateArrow(false)
        }
        mPopupWindow?.dismiss()
    }

    /**
     * Set the tint color for the dropdown arrow
     *
     * @param color the color value
     */
    fun setArrowColor(@ColorInt color: Int) {
        mArrowColor = color
        mArrowColorDisabled = lighter(mArrowColor, 0.8f)
        mArrowDrawable?.colorFilter = PorterDuffColorFilter(mArrowColor, PorterDuff.Mode.SRC_IN)
    }

    /**
     * Set the maximum height of the dropdown menu.
     *
     * @param height the height in pixels
     */
    fun setPopupMaxHeight(height: Int) {
        mPopupWindowMaxHeight = height
        mPopupWindow?.height = calculatePopupWindowHeight()
    }

    /**
     * Set the height of the dropdown menu
     *
     * @param height the height in pixels
     */
    fun setPopupHeight(height: Int) {
        mPopupWindowHeight = height
        mPopupWindow?.height = calculatePopupWindowHeight()
    }

    private fun canShowPopup(): Boolean {
        val activity = getActivity()
        return if (activity == null || activity.isFinishing) {
            false
        } else isLaidOut
    }

    private fun getActivity(): Activity? {
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context as Activity
            }
            (context as ContextWrapper).baseContext as Activity
        }
        return null
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun animateArrow(shouldRotateUp: Boolean) {
        val start = if (shouldRotateUp) 0 else 10000
        val end = if (shouldRotateUp) 10000 else 0
        val animator = ObjectAnimator.ofInt(mArrowDrawable, "level", start, end)
        animator.start()
    }

    private fun calculatePopupWindowHeight(): Int {
        if (mAdapter == null) return WindowManager.LayoutParams.WRAP_CONTENT
        val itemHeight = 48.toPx()
        val itemsCount = mAdapter?.count ?: 0
        val listViewHeight = itemsCount * itemHeight
        return when {
            mPopupWindowMaxHeight in 1 until listViewHeight -> mPopupWindowMaxHeight
            mPopupWindowHeight != WindowManager.LayoutParams.MATCH_PARENT && mPopupWindowHeight != WindowManager.LayoutParams.WRAP_CONTENT && mPopupWindowHeight <= listViewHeight -> mPopupWindowHeight
            listViewHeight == 0 && mAdapter?.count == 1 -> itemHeight
            else -> WindowManager.LayoutParams.WRAP_CONTENT
        }
    }

    private fun Int.toPx() = (this * Resources.getSystem().displayMetrics.density).toInt()

    /**
     * Interface definition for a callback to be invoked when an item in this view has been selected.
     */
    interface OnItemSelectedListener {
        /**
         *
         * Callback method to be invoked when an item in this view has been selected. This callback is invoked only when
         * the newly selected position is different from the previously selected position or if there was no selected
         * item.
         *
         * @param position The position of the view in the adapter
         * @param item     The selected item
         */
        fun onItemSelected(position: Int, item: Any)
    }
}