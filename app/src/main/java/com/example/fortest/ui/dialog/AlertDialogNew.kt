package com.example.fortest.ui.dialog

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDialog
import com.example.fortest.R
import com.example.fortest.databinding.DialogAlertNewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class AlertDialogNew(context: Context) : BottomSheetDialog(context, R.style.StatusDialogTheme) {

    private val dialog: BottomSheetDialog = this
    val binding = DialogAlertNewBinding.inflate(layoutInflater)

    init {
        setContentView(binding.root)
    }

    private fun setIcon(@DrawableRes iconResId: Int) {
        binding.img.setImageResource(iconResId)
    }

    private fun setDialogFullHeight(isFullHeight: Boolean) {
        if (isFullHeight) {
            setBottomSheetDialogFullHeight(this)
        }
    }

    private fun setBottomSheetDialogFullHeight(appDialog: AppCompatDialog) {
        val dialog = appDialog as BottomSheetDialog

        val bottomSheet = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior: BottomSheetBehavior<FrameLayout> = BottomSheetBehavior.from(bottomSheet as FrameLayout)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.peekHeight = 0
        behavior.isHideable = true
        behavior.skipCollapsed = true
    }

    private fun setDialogCancelable(cancelable: Boolean) {
        if (cancelable) return

        setCancelable(false)
        val bottomSheetView = window!!.decorView.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        BottomSheetBehavior.from(bottomSheetView).isHideable = false
        BottomSheetBehavior.from(bottomSheetView).isDraggable = false
    }

    private fun setDialogTitle(titleResId: Int) {
        binding.tvTitle.visibility = View.VISIBLE
        binding.tvTitle.setText(titleResId)
    }

    private fun setDialogTitle(title: String) {
        binding.tvTitle.visibility = View.VISIBLE
        binding.tvTitle.text = title
    }

    private fun setMessage(msgResId: Int) {
        binding.tvMessage.visibility = View.VISIBLE
        binding.tvMessage.setText(msgResId)
    }

    private fun setMessage(message: String) {
        binding.tvMessage.visibility = View.VISIBLE
        binding.tvMessage.text = message
    }

    private fun setPositiveButton(actionResId: Int, onClickListener: (BottomSheetDialog) -> Unit) {
        binding.btnPositive.visibility = View.VISIBLE
        binding.btnPositive.setOnClickListener { onClickListener(dialog) }
        binding.btnPositive.setText(actionResId)
    }

    private fun setNegativeButton(actionResId: Int, onClickListener: (BottomSheetDialog) -> Unit) {
        binding.btnNegative.visibility = View.VISIBLE
        binding.btnNegative.setOnClickListener { onClickListener(dialog) }
        binding.btnNegative.setText(actionResId)
    }

    private fun setNeutralButton(actionResId: Int, onClickListener: (BottomSheetDialog) -> Unit) {
//        binding.btnNeutral.visibility = View.VISIBLE
//        binding.btnNeutral.setOnClickListener { onClickListener(dialog) }
//        binding.btnNeutral.setText(actionResId)
    }

    class Builder(val context: Context) {

        private val dialog = AlertDialogNew(context)

        fun setIcon(@DrawableRes iconResId: Int): Builder {
            dialog.setIcon(iconResId)
            return this
        }

        fun setDialogTitle(@StringRes titleResId: Int): Builder {
            dialog.setDialogTitle(titleResId)
            return this
        }

        fun setDialogTitle(title: String): Builder {
            dialog.setDialogTitle(title)
            return this
        }

        fun setMessage(@StringRes msgResId: Int): Builder {
            dialog.setMessage(msgResId)
            return this
        }

        fun setMessage(message: String): Builder {
            dialog.setMessage(message)
            return this
        }

        fun setDialogFullHeight(isFullHeight: Boolean): Builder {
            dialog.setDialogFullHeight(isFullHeight)
            return this
        }

        fun setPositiveButton(@StringRes actionResId: Int, onClickListener: (BottomSheetDialog) -> Unit): Builder {
            dialog.setPositiveButton(actionResId, onClickListener)
            return this
        }

        fun setNegativeButton(@StringRes actionResId: Int, onClickListener: (BottomSheetDialog) -> Unit): Builder {
            dialog.setNegativeButton(actionResId, onClickListener)
            return this
        }

        fun setNeutralButton(@StringRes actionResId: Int, onClickListener: (BottomSheetDialog) -> Unit): Builder {
            dialog.setNeutralButton(actionResId, onClickListener)
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            dialog.setCancelable(cancelable)
            return this
        }

        fun setDialogCancelable(cancelable: Boolean): Builder {
            dialog.setDialogCancelable(cancelable)
            return this
        }

        fun show(): BottomSheetDialog {
            dialog.show()
            return dialog
        }

    }
}