package com.example.fortest.ui.leads.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fortest.databinding.ItemLeadsBinding
import com.example.rocketreserver.FetchInputQuery


class LeadsAdapter : RecyclerView.Adapter<LeadsAdapter.LeadViewHolder>() {

    private var mCardList = mutableListOf<FetchInputQuery.Data1>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(cardList: List<FetchInputQuery.Data1>) {
        mCardList.clear()
        mCardList.addAll(cardList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadViewHolder {
        return LeadViewHolder(
            ItemLeadsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LeadViewHolder, position: Int) {
        holder.initCardView(mCardList[position], position)
    }

    override fun getItemCount(): Int {
        return mCardList.size
    }

    inner class LeadViewHolder(private val binding: ItemLeadsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val mContext = binding.root.context

        fun initCardView(item: FetchInputQuery.Data1?, position: Int) {

            binding.leadType.text = item?.status?.title
            binding.followDate.text = item?.createdAt.toString()
            binding.name.text = "${item?.firstName} ${item?.lastName}"

        }
    }
}