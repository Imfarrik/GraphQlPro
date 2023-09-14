package com.example.fortest.ui.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.fortest.R
import com.example.fortest.ui.dialog.AlertDialogNew
import com.example.fortest.ui.dialog.ProgressDialog

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseAppCompatFragment<T : ViewBinding>(private val inflate: Inflate<T>) : Fragment() {

    private var _binding: T? = null
    val binding get() = _binding!!
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("CURRENT_ACTIVITY_FRAGMENT", javaClass.simpleName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // from child fragments

    fun showBaseProgress(show: Boolean, action: () -> Unit) {
        progressDialog = if (show) {
            ProgressDialog.Builder(requireActivity())
                .setCancelable(false)
                .setOnBackClickListener(action)
                .show()
        } else {
            progressDialog?.dismiss()
            null
        }
    }

    fun showBaseError(msg: String? = null, msgId: Int? = null, onClick: () -> Unit) {
        val builder = AlertDialogNew.Builder(requireContext())
            .setIcon(R.drawable.ic_launcher_background)
            .setCancelable(false)
            .setPositiveButton(R.string.action_back) {
                it.dismiss()
                onClick()
            }

        if (msg != null) builder.setDialogTitle(msg)
        if (msgId != null) builder.setDialogTitle(msgId)

        builder.show()
    }
}