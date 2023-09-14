package com.example.fortest.ui.dialog

import android.app.Dialog
import android.content.Context
import android.util.DisplayMetrics
import android.view.KeyEvent
import com.example.fortest.R
import com.example.fortest.databinding.DialogProgressBinding
import java.util.Timer
import java.util.TimerTask

class ProgressDialog private constructor(context: Context?) :
    Dialog(context!!, R.style.AlertDialog) {

    private val mDialog: ProgressDialog

    private val binding = DialogProgressBinding.inflate(layoutInflater)

    init {
        setContentView(binding.root)

        mDialog = this
    }

    fun dismissWithDelay(delay: Long) {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                this@ProgressDialog.dismiss()
            }

        }, delay)
    }

    private fun setOnBackClickListener(action: () -> Unit) {

        mDialog.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                action()
                mDialog.setCancelable(true)
                mDialog.dismiss()
            }
            true
        }
    }

    class Builder(val context: Context?) {
        var dialog: ProgressDialog = ProgressDialog(context)

        fun setCancelable(cancelable: Boolean): Builder {
            dialog.setCancelable(cancelable)
            return this
        }

        fun setOnBackClickListener(action: () -> Unit): Builder {
            dialog.setOnBackClickListener(action)
            return this
        }

        fun show(): ProgressDialog {
            dialog.show()
            val px = (108 * (context!!.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
            dialog.window!!.setLayout(px, px)
            return dialog
        }

        init {
            dialog.setCancelable(false)
        }
    }
}