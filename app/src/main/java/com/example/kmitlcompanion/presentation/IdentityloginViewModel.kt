package com.example.kmitlcompanion.presentation

import android.app.Activity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentIdentityloginBinding
import com.example.kmitlcompanion.domain.usecases.postUserData
import com.example.kmitlcompanion.presentation.eventobserver.Event
import com.example.kmitlcompanion.presentation.utils.SingleLiveData
import com.example.kmitlcompanion.ui.identitylogin.IdentityloginFragmentDirections
import com.example.kmitlcompanion.ui.identitylogin.DataFacultyDepart
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

    private lateinit var binding: FragmentIdentityloginBinding
    private lateinit var activity:Activity

    private val dataFacultyDepart = DataFacultyDepart()


    fun setActivityBinding(binding: FragmentIdentityloginBinding,activity: Activity){
        this.binding = binding
        this.activity = activity
    }

    fun saveData(name: TextView,
                 surname : TextView,
                 faculty : TextView,
                 department : TextView,
                 year : TextView){

        if (!checkAllFieldValid()){
            Log.d("saveData","InValid Data")
            return
        }

        val name = name.text.toString()
        val surname = surname.text.toString()
        val faculty = faculty.text.toString()
        val department = department.text.toString()
        val year = year.text.toString()
        val token = "eyJhbGciOiJSUzI1NiIsImtpZCI6Ijk1NTEwNGEzN2ZhOTAzZWQ4MGM1NzE0NWVjOWU4M2VkYjI5YjBjNDUiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI1NjM1MDkwMDIwODQtMzVwMjdvZG85anFhcm8yaGRnbzJuZG1vMjE1aWVuajAuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI1NjM1MDkwMDIwODQtYjdtMDVib2lhcXM1bW8wdGhpNGthNTlub2lha2V1czIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTg0MzIwNDk5MTAwMjYyOTA5NjIiLCJoZCI6ImttaXRsLmFjLnRoIiwiZW1haWwiOiI2MjAxMDg5M0BrbWl0bC5hYy50aCIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiNjJfMDg5MyBTVVBIQU5VVCBXQU5ERUUiLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EvQUxtNXd1MUVWbHItbm95d3NhRG4tbUZDWGk2SkpkQ3JYOU82b1JaMEVNZz1zOTYtYyIsImdpdmVuX25hbWUiOiI2Ml8wODkzIFNVUEhBTlVUIiwiZmFtaWx5X25hbWUiOiJXQU5ERUUiLCJsb2NhbGUiOiJ0aCIsImlhdCI6MTY2NTc2NjMyOCwiZXhwIjoxNjY1NzY5OTI4fQ.S7U7yPnMnOAdX3HLgqjkmxmj4Ux5j38AJN-OjkVAxQpHIt-bNDSM-bgrTYi_cdA60P-b69p-ePwdbZmHt_0uBXw_BJf7YKf9kPXZ_f8qHV3YmkytES2woty9YbQ8N9_F6BIGOK5YujVncx9BsRI3jddV1PDqnXoEATjHLnY_NhjyA35ucgUIS-9HHrud-ID2KmdcNIIf1XtWnBW3ZbWkqMuxdeJ-ahRvzxPw_qCQaMOR2SSjqLke7b9Vb6yccrNJZOAYly9TZjedKHPFrwYI89gcOxwGfBurenc3s3PxPzCRJ9fbr60O6QyN-TIlpj_dV6nb62A2oILG91mAHLPjxw"
        val sendArrayData = arrayListOf<Any>(name,surname,faculty,department,year,token)
        Log.d("sendArrayData",sendArrayData.toString())

        _saveUserDataResponse.value = sendArrayData

    }

    //set field adapter
    public fun listFieldAdapter(){
        val facultyList = dataFacultyDepart.getFaculty()//arrayListOf<String>("Engineer1","Engineer2","Engineer3")
        val arrayAdapter1 = ArrayAdapter<String>(activity,
            R.layout.dropdown_item,facultyList)
        binding.facultyList.setAdapter(arrayAdapter1)

        val yearList = arrayListOf<Int>(1,2,3,4,5,6,7,8)
        val arrayAdapter3 = ArrayAdapter<Int>(activity, R.layout.dropdown_item,yearList)
        binding.yearList.setAdapter(arrayAdapter3)
    }

    private fun setDepartmentAdapter(faculty: String){
        val departmentList = dataFacultyDepart.getDepart(faculty)//arrayListOf<String>(faculty + " CE",faculty + " EE",faculty + " CPE")
        val arrayAdapter2 = ArrayAdapter<String>(activity, R.layout.dropdown_item,departmentList)
        binding.departmentList.setAdapter(arrayAdapter2)
        binding.department.setEnabled(true)
    }

    //violate
    //check require field
    public fun inputListener(){
        binding.textNameValue.doAfterTextChanged {
            binding.textinputName.helperText = validTextInput(binding.textNameValue.text.toString())
        }

        binding.textSurnameValue.doAfterTextChanged {
            binding.textinputSurname.helperText = validTextInput(binding.textSurnameValue.text.toString())
        }

        binding.facultyList.doAfterTextChanged{
            binding.faculty.helperText = validDropDownInput(binding.facultyList.text.toString())
            binding.departmentList.text = null
            if (!binding.facultyList.text.toString().isEmpty()){
                setDepartmentAdapter(binding.facultyList.text.toString())
            }
        }

        binding.departmentList.doAfterTextChanged {
            binding.department.helperText = validDropDownInput(binding.departmentList.text.toString())
        }

        binding.yearList.doAfterTextChanged {
            binding.year.helperText = validDropDownInput(binding.yearList.text.toString())
        }
    }

    private fun validTextInput(textInput: String): String?{
        val inputText = textInput
        if(inputText.isEmpty() || inputText.matches("(.*[^A-Z|a-z].*)".toRegex())){
            return "ห้ามมีตัวอักษรพิเศษ และห้ามเว้นว่าง"
        }
        return null
    }

    private fun validDropDownInput(textInput: String): String?{
        val inputText = textInput
        if(inputText.isEmpty()){
            return "ห้ามเว้นช่องนี้ว่าง"
        }
        return null
    }

    private fun checkAllFieldValid():Boolean{

        binding.textinputName.helperText = validTextInput(binding.textNameValue.text.toString())
        binding.textinputSurname.helperText = validTextInput(binding.textSurnameValue.text.toString())
        binding.faculty.helperText = validDropDownInput(binding.facultyList.text.toString())
        binding.department.helperText = validDropDownInput(binding.departmentList.text.toString())
        binding.year.helperText = validDropDownInput(binding.yearList.text.toString())

        val validName = binding.textinputName.helperText == null
        val validSurname = binding.textinputSurname.helperText == null
        val validFaculty = binding.faculty.helperText == null
        val validDepartment = binding.department.helperText == null
        val validYear = binding.year.helperText == null
        if (validName && validSurname && validFaculty && validDepartment && validYear){
            return true
        }
        return false
    }

    //API
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


    //Navigation
    fun nextHomePage(){
        navigate(IdentityloginFragmentDirections.actionIdentityloginFragmentToHomeFragment())
    }


}