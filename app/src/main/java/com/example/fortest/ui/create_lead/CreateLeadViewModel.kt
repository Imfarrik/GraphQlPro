package com.example.fortest.ui.create_lead

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fortest.data.apollo.apolloClient
import com.example.rocketreserver.CreateLeadMutation
import com.example.rocketreserver.type.ContactDataInput
import com.example.rocketreserver.type.CreateLeadInput
import kotlinx.coroutines.launch

class CreateLeadViewModel : ViewModel() {

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isProgressActive = MutableLiveData<Boolean>()
    val isProgressActive: LiveData<Boolean> = _isProgressActive

    fun postLead(name: String, intentionId: Int, lang: List<Int>, contact: List<ContactDataInput> = listOf()) {
        _isProgressActive.value = true
        viewModelScope.launch {

            val response = apolloClient.mutation(
                CreateLeadMutation(
                    CreateLeadInput(
                        firstName = name,
                        contacts = contact,
                        intentionId = intentionId,
                        languageIds = lang
                    )
                )
            ).execute()

            if (!response.hasErrors()) {
                _isProgressActive.value = false
                val lead = response.data?.createLead
                if (lead != null) {
                    _isSuccess.value = true
                }
            } else {
                _isProgressActive.value = false
                _isSuccess.value = false
                val errors = response.errors
                errors?.joinToString(" ")?.let { Log.i("ApolloErrors", it) }
            }


        }
    }

}