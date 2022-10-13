package com.example.kmitlcompanion.presentation

import android.util.Log
import android.widget.TextView
import com.example.kmitlcompanion.domain.usecases.postUserData
import com.example.kmitlcompanion.presentation.eventobserver.Event
import com.example.kmitlcompanion.presentation.utils.SingleLiveData
import com.example.kmitlcompanion.ui.auth.IdentityloginFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import javax.inject.Inject


@HiltViewModel
class IdentityloginViewModel @Inject constructor(
    private val postUserData: postUserData
) : BaseViewModel() {

    private val _saveUserDataResponse = SingleLiveData<ArrayList<Any>>()
    val saveUserDataResponse: SingleLiveData<ArrayList<Any>> = _saveUserDataResponse

    private val _nextHomePage = SingleLiveData<Event<Boolean>>()
    val nextHomepage: SingleLiveData<Event<Boolean>> = _nextHomePage

    fun saveData(name: TextView,
                 surname : TextView,
                 faculty : TextView,
                 department : TextView,
                 year : TextView){

        val name = name.text.toString()
        val surname = surname.text.toString()
        val faculty = faculty.text.toString()
        val department = department.text.toString()
        val year = year.text.toString()
        val token = "eyJhbGciOiJSUzI1NiIsImtpZCI6Ijk1NTEwNGEzN2ZhOTAzZWQ4MGM1NzE0NWVjOWU4M2VkYjI5YjBjNDUiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI1NjM1MDkwMDIwODQtMzVwMjdvZG85anFhcm8yaGRnbzJuZG1vMjE1aWVuajAuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI1NjM1MDkwMDIwODQtYjdtMDVib2lhcXM1bW8wdGhpNGthNTlub2lha2V1czIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTg0MzIwNDk5MTAwMjYyOTA5NjIiLCJoZCI6ImttaXRsLmFjLnRoIiwiZW1haWwiOiI2MjAxMDg5M0BrbWl0bC5hYy50aCIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiNjJfMDg5MyBTVVBIQU5VVCBXQU5ERUUiLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EvQUxtNXd1MUVWbHItbm95d3NhRG4tbUZDWGk2SkpkQ3JYOU82b1JaMEVNZz1zOTYtYyIsImdpdmVuX25hbWUiOiI2Ml8wODkzIFNVUEhBTlVUIiwiZmFtaWx5X25hbWUiOiJXQU5ERUUiLCJsb2NhbGUiOiJ0aCIsImlhdCI6MTY2NTY4ODQ2MSwiZXhwIjoxNjY1NjkyMDYxfQ.OeK3bgmNdgvwJBZxUYRtKFeOjGNxYoo63DjCzFgvGUBJ_J5xqDbIC1NQmLj2fCNjBAQ_eAAcgHmsfvFkBhJyeISPABEUGRQKRYnXevO0VrBgDpOKPfQwH0frESVxXE409TKjRyRH4iuPYUnxSQrLOKv91mdIOXO67ONU0I_oWtyhnIMlNPOgdkiCk_gxRJUoTSGOUSsEIkkQjf0sWK-5LrRqdypgrhvyqiRqm4Bbhp7zm6A9OGLgiJurkHScXTbbPCPJC6sJGxq_o0DAbwomBUQ_ParoGpKPEzCPSK0687PsiwhDR2ai6V7-acc1Hwv326O4MLymXZp8huJOoMgC_A"
        val sss = arrayListOf<Any>(name,surname,faculty,department,year,token)
        Log.d("SEGGESSSSSSSSSSSSSS",sss.toString())

        _saveUserDataResponse.value = sss

    }

    fun postUserData(arr : ArrayList<Any>){
        postUserData.execute(object : DisposableCompletableObserver() {
            override fun onComplete() {
                Log.d("QUERY DATA","Success !!!")
                _nextHomePage.value = Event(true)
            }
            override fun onError(e: Throwable) {
                Log.d("QUERY DATA",e.toString())
            }
        }, arr)

    }




    fun nextHomePage(){
        navigate(IdentityloginFragmentDirections.actionIdentityloginFragmentToHomeFragment())
    }


}