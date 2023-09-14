package com.example.fortest.ui.leads

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.Optional
import com.example.fortest.data.apollo.apolloClient
import com.example.rocketreserver.CreateLeadMutation
import com.example.rocketreserver.FetchInputQuery
import com.example.rocketreserver.type.ContactDataInput
import com.example.rocketreserver.type.CreateLeadInput
import kotlinx.coroutines.launch

class LeadsViewModel : ViewModel() {

    private val _leadsList = MutableLiveData<List<FetchInputQuery.Data1>>()
    val leadsList: LiveData<List<FetchInputQuery.Data1>> = _leadsList


    init {
        viewModelScope.launch {
            val a = apolloClient.query(FetchInputQuery()).execute()
            val b = apolloClient.mutation(
                CreateLeadMutation(
                    CreateLeadInput(
                        firstName = "FarrukhTest",
                        contacts = listOf(),
                        intentionId = 2,
                        languageIds = listOf(2, 3)
                    )
                )
            ).execute()
            _leadsList.value = a.data?.fetchLeads?.data
            Log.d("LaunchList", "Success ${a.data}")
            Log.d("LaunchList", "Success ${b.data}")
        }
    }
}