package com.example.fortest.ui.leads

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fortest.databinding.FragmentLeadsBinding
import com.example.fortest.ui.base.BaseAppCompatFragment
import com.example.fortest.ui.leads.adapter.LeadsAdapter

class LeadsFragment : BaseAppCompatFragment<FragmentLeadsBinding>(FragmentLeadsBinding::inflate) {

    private val viewModel: LeadsViewModel by viewModels()
    private var _adapter: LeadsAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        initViewModel()

    }

    private fun initViewModel() = with(binding) {

        viewModel.apply {
            leadsList.observe(viewLifecycleOwner) {
                _adapter?.updateList(it!!)
            }
        }

    }

    private fun initView() = with(binding) {

        rvLeads.let {
            _adapter = LeadsAdapter()
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            it.adapter = _adapter
        }

    }

}