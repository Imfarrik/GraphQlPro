package com.example.fortest.ui.custom_view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.annotation.ColorInt
import com.example.fortest.R

class MaterialSpinnerAdapter(val context: Context, val items: List<Any>) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val rtl = isRtl(context)
    private var selectedIndex: Int
    private var mTextColor = 0
    private var mPaddingLeft = 0
    private var mPaddingTop = 0
    private var mPaddingRight = 0
    private var mPaddingBottom = 0

    init {
        selectedIndex = -1
    }

    override fun getCount() = items.size

    override fun getItem(position: Int) = items[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView == null) {
            val view: View = inflater.inflate(R.layout.view_material_spinner, parent, false)
            val textView: TextView = view.findViewById(R.id.text_view)
            textView.text = getItem(position).toString()
            textView.setTextColor(mTextColor)
            textView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom)
            if (rtl) textView.textDirection = View.TEXT_DIRECTION_RTL
            view.tag = textView
            view
        } else {
            val textView: TextView = convertView.tag as TextView
            textView.text = getItem(position).toString()
            convertView
        }
    }

    fun getItemText(position: Int): String {
        return getItem(position).toString()
    }

    fun notifyItemSelected(index: Int) {
        selectedIndex = index
    }

    fun get(position: Int): Any {
        return items[position]
    }

    fun setTextColor(@ColorInt textColor: Int): MaterialSpinnerAdapter {
        mTextColor = textColor
        return this
    }

    fun setPopupPadding(left: Int, top: Int, right: Int, bottom: Int): MaterialSpinnerAdapter {
        mPaddingLeft = left
        mPaddingTop = top
        mPaddingRight = right
        mPaddingBottom = bottom
        return this
    }

    fun getSelectedIndex() = selectedIndex

    fun getSelectedItem(): Any? {
        return if (selectedIndex < 0 || selectedIndex >= count) null
        else get(selectedIndex)
    }

    /**
     * Check if layout direction is RTL
     *
     * @param context the current context
     * @return `true` if the layout direction is right-to-left
     */
    private fun isRtl(context: Context): Boolean {
        return context.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL
    }
}