package com.example.fortest.ui.lead_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fortest.R
import com.example.fortest.databinding.ActivityCreateLeadBinding
import com.example.fortest.databinding.ActivityLeadDetailsBinding
import com.example.fortest.navigation.Navigation
import com.example.fortest.ui.dialog.SelectDialog

class LeadDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLeadDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeadDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvName.text = "${intent.getStringExtra(Navigation.NAME)} ${intent.getStringExtra(Navigation.LAST_NAME)}"

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.progressBar.progress = 35

        binding.statusContainer.setOnClickListener {
            val dialog = SelectDialog.Builder(this)
            dialog.let {
                it.initChat()
                it.onClick { str, dialog ->
                    dialog.dismiss()
                }
                it.show()
            }
        }
    }


}