package com.example.fortest.ui.dialog

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fortest.databinding.ItemFlagsBinding
import com.example.fortest.databinding.ItemLeadsBinding
import com.example.rocketreserver.FetchInputQuery


class SelectAdapter(val list: List<String>, private val onClick: (String) -> Unit) : RecyclerView.Adapter<SelectAdapter.LeadViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadViewHolder {
        return LeadViewHolder(
            ItemFlagsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LeadViewHolder, position: Int) {
        holder.initCardView(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class LeadViewHolder(private val binding: ItemFlagsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val mContext = binding.root.context

        fun initCardView(item: String, position: Int) {

            itemView.setOnClickListener {
                onClick(item)
            }

            binding.country.text = item

        }
    }
}