package com.example.fortest.ui.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fortest.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout

class SelectDialog(context: Context) : BottomSheetDialog(context, R.style.FilterDialogTheme) {

    @SuppressLint("InflateParams")
    private val bsv = LayoutInflater.from(context).inflate(R.layout.dialog_select, null)

    private var rv: RecyclerView
    private var search: TextInputLayout
    private var btn: TextView

    var value = ""

    init {
        setContentView(bsv)

        rv = this.findViewById(R.id.rv)!!
        search = this.findViewById(R.id.search_container)!!
        btn = this.findViewById(R.id.btn)!!

        val height = Resources.getSystem().displayMetrics.heightPixels

        val bsb = BottomSheetBehavior.from(bsv.parent as View)
        bsb.state = BottomSheetBehavior.STATE_EXPANDED
        val l = this.findViewById<LinearLayout>(R.id.view_dialog_container)
        assert(l != null)
        l?.minimumHeight = height

        setDialogCancelable()

    }

    fun initCountry(onClick: (String, BottomSheetDialog) -> Unit) {
        search.isVisible = true
        btn.isVisible = false
        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv.adapter = SelectAdapter(listOf("Uzb", "Rus", "Eng")) {
            onClick(it, this)
        }
    }

    fun btnClick(onClick: (String, BottomSheetDialog) -> Unit) {
        btn.setOnClickListener {
            onClick(value, this)
        }
    }

    fun initChat() {
        search.isVisible = false
        btn.isVisible = true
        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv.adapter = CatAdapter(listOf("New", "No answer", "Options sent", "Warm", "Hot")) {
            value = it
        }
    }


    private fun setDialogCancelable() {
        setCancelable(true)
        val bottomSheetView = window!!.decorView.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        BottomSheetBehavior.from(bottomSheetView).isHideable = false
        BottomSheetBehavior.from(bottomSheetView).isDraggable = false
    }

    class Builder(context: Context) {

        private val dialog = SelectDialog(context = context)

        fun initFlag(onClick: (String, BottomSheetDialog) -> Unit): SelectDialog {
            dialog.initCountry(onClick)
            return dialog
        }

        fun onClick(onClick: (String, BottomSheetDialog) -> Unit): SelectDialog {
            dialog.btnClick(onClick)
            return dialog
        }

        fun initChat(): SelectDialog {
            dialog.initChat()
            return dialog
        }

        fun show(): SelectDialog {
            dialog.show()
            return dialog
        }

    }


}