package com.example.fortest.navigation

import android.content.Context
import android.content.Intent
import com.example.fortest.ui.create_lead.CreateLeadActivity
import com.example.fortest.ui.edit_info.EditActivity
import com.example.fortest.ui.lead_details.LeadDetailsActivity

interface Navigation {
    companion object {

        const val NAME = "name"
        const val LAST_NAME = "last_name"
        const val ID = "id"

        fun startCreateLeadActivity(context: Context) {
            val intent = Intent(context, CreateLeadActivity::class.java)
            context.startActivity(intent)
        }

        fun startLeadsDetailsActivity(context: Context, name: String, lastName: String) {
            val intent = Intent(context, LeadDetailsActivity::class.java)
            intent.putExtra(NAME, name)
            intent.putExtra(LAST_NAME, lastName)
            context.startActivity(intent)
        }

        fun startEditActivity(context: Context) {
            val intent = Intent(context, EditActivity::class.java)
            context.startActivity(intent)
        }

    }
}