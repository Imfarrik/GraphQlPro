package com.example.fortest.ui.create_lead

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.fortest.databinding.ActivityCreateLeadBinding
import com.example.fortest.ui.custom_view.MaterialSpinnerView
import com.example.fortest.ui.dialog.ProgressDialog

class CreateLeadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateLeadBinding
    private val viewModel: CreateLeadViewModel by viewModels()
    private var progressDialog: ProgressDialog? = null

    private var name: String? = null
    private var lastName: String? = null
    private var phoneNumber: String? = null
    private var language: MutableList<String>? = null
    private var intention: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateLeadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

    }

    private fun initView() = with(binding) {

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnCancel.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {

            etName.text?.let {
                name = if (it.toString() != "") it.toString() else null
                Log.i("Hello", name.toString())
            }
            etLastName.text?.let {
                lastName = if (it.toString() != "") it.toString() else null
                Log.i("Hello", lastName.toString())
            }
            etPhone.text?.let {
                phoneNumber = if (it.toString() != "") it.toString() else null
                Log.i("Hello", phoneNumber.toString())
            }

            val allValues = listOf(name, lastName, phoneNumber, language, intention)

            allValues.forEach {
                if (it == null) {
                    Toast.makeText(this@CreateLeadActivity, "Empty fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val intentionId = setIntention()[intention]
            val langs = mutableListOf<Int>()
            language?.forEach { l ->
                langs.add(setLang()[l]!!)
            }
            Log.i("Hello", langs.joinToString(","))
            viewModel.postLead(name!!, intentionId!!, langs)
        }

        initLang()

        initIntention()

        viewModel.apply {

            isProgressActive.observe(this@CreateLeadActivity) {
                showProgress(it) { onBackPressedDispatcher.onBackPressed() }
            }

            isSuccess.observe(this@CreateLeadActivity) {
                if (it) {
                    Toast.makeText(this@CreateLeadActivity, "Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@CreateLeadActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initLang() = with(binding) {
        tvLanguage.setOnTouchListener { v, event ->
            hideKeyboard(this@CreateLeadActivity)
            v?.onTouchEvent(event) ?: true
        }

        tvLanguage.setItems(setLang().keys.toList())

        tvLanguage.setOnItemSelectedListener(
            object : MaterialSpinnerView.OnItemSelectedListener {
                override fun onItemSelected(position: Int, item: Any) {
                    if (language == null) {
                        language = mutableListOf(item as String)
                    } else {
                        language!!.add(item as String)
                    }
                }
            }
        )
        tvLanguage.setSelectedIndex(0)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initIntention() = with(binding) {
        tvIntention.setOnTouchListener { v, event ->
            hideKeyboard(this@CreateLeadActivity)
            v?.onTouchEvent(event) ?: true
        }

        tvIntention.setItems(setIntention().keys.toList())

        tvIntention.setOnItemSelectedListener(
            object : MaterialSpinnerView.OnItemSelectedListener {
                override fun onItemSelected(position: Int, item: Any) {
                    intention = item as String
                }
            }
        )
        tvIntention.setSelectedIndex(0)
    }

    private fun setLang(): Map<String, Int> {
        return mapOf("Russian" to 1, "Uzbek" to 2, "English" to 3)
    }

    private fun setIntention(): Map<String, Int> {
        return mapOf("Buy" to 1, "Sell" to 2, "Rent" to 3, "Rent out" to 4)
    }

    private fun hideKeyboard(activity: Activity?) {
        val view = activity?.currentFocus
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE)
        if (imm is InputMethodManager) imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun showProgress(show: Boolean, action: () -> Unit) {
        progressDialog = if (show) {
            ProgressDialog.Builder(this)
                .setCancelable(false)
                .setOnBackClickListener(action)
                .show()
        } else {
            progressDialog?.dismiss()
            null
        }
    }

}