package com.example.fortest.ui.edit_info

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.inputmethod.InputMethodManager
import com.example.fortest.R
import com.example.fortest.databinding.ActivityEditBinding
import com.example.fortest.ui.custom_view.MaterialSpinnerView
import com.example.fortest.ui.dialog.SelectDialog
import com.google.android.material.internal.ViewUtils.hideKeyboard

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.country.setArrowColor(Color.TRANSPARENT)

        binding.country.setOnClickListener {
            val dialog = SelectDialog.Builder(this)
            dialog.initFlag { str, dialog ->
                binding.dialogTxt.let {
                    it.text = str
                    it.setTextColor(resources.getColor(R.color.black))
                }
                dialog.dismiss()
            }
            dialog.show()
        }

        initLang()
        initIntention()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initLang() = with(binding) {
        tvLanguage.setOnTouchListener { v, event ->
            hideKeyboard(this@EditActivity)
            v?.onTouchEvent(event) ?: true
        }

        val map = mapOf("Russian" to 1, "Uzbek" to 2, "English" to 3)

        tvLanguage.setItems(map.keys.toList())

        tvLanguage.setOnItemSelectedListener(
            object : MaterialSpinnerView.OnItemSelectedListener {
                override fun onItemSelected(position: Int, item: Any) {

                }
            }
        )
        tvLanguage.setSelectedIndex(0)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initIntention() = with(binding) {
        tvIntention.setOnTouchListener { v, event ->
            hideKeyboard(this@EditActivity)
            v?.onTouchEvent(event) ?: true
        }

        val map = mapOf("Buy" to 1, "Sell" to 2, "Rent" to 3, "Rent out" to 4)

        tvIntention.setItems(map.keys.toList())

        tvIntention.setOnItemSelectedListener(
            object : MaterialSpinnerView.OnItemSelectedListener {
                override fun onItemSelected(position: Int, item: Any) {

                }
            }
        )
        tvIntention.setSelectedIndex(0)
    }


    private fun hideKeyboard(activity: Activity?) {
        val view = activity?.currentFocus
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE)
        if (imm is InputMethodManager) imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}