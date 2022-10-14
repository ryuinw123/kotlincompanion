package com.example.kmitlcompanion.presentation

import android.app.Activity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.cache.entities.User
import com.example.kmitlcompanion.data.model.UserData
import com.example.kmitlcompanion.databinding.FragmentIdentityloginBinding
import com.example.kmitlcompanion.domain.usecases.getUserRoom
import com.example.kmitlcompanion.domain.usecases.postUserData
import com.example.kmitlcompanion.presentation.eventobserver.Event
import com.example.kmitlcompanion.presentation.utils.SingleLiveData
import com.example.kmitlcompanion.ui.identitylogin.IdentityloginFragmentDirections
import com.example.kmitlcompanion.ui.identitylogin.DataFacultyDepart
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.observers.DisposableObserver
import javax.inject.Inject


@HiltViewModel
class IdentityloginViewModel @Inject constructor(
    private val postUserData: postUserData,
    private val getUser: getUserRoom
) : BaseViewModel() {

    //get user token
    private val _getUserRoom = SingleLiveData<ArrayList<Any>>()
    val getUserRoom: SingleLiveData<ArrayList<Any>> = _getUserRoom

    //save data to db
    private val _saveUserDataResponse = SingleLiveData<ArrayList<Any>>()
    val saveUserDataResponse: SingleLiveData<ArrayList<Any>> = _saveUserDataResponse

    //naviagte to homepage
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
        val token = ""
        val sendArrayData = arrayListOf<Any>(name,surname,faculty,department,year,token)
        Log.d("sendArrayData",sendArrayData.toString())


        //getUserToken and SendArrayData in oncomplete
        _getUserRoom.value = sendArrayData
        //getUserRoom(sendArrayData)
        //_saveUserDataResponse.value = sendArrayData

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

    fun getUserRoom(arr : ArrayList<Any>){
        getUser.execute(object : DisposableObserver<List<UserData>>(){
            override fun onComplete() {
                Log.d("getUserRoom","Success !!!")
            }

            override fun onNext(t: List<UserData>) {
                arr[5] = t[0].token
                _saveUserDataResponse.value = arr
                Log.d("getUserRoom","onNext Success !!!")
            }

            override fun onError(e: Throwable) {
                Log.d("getUserRoom",e.toString())
            }
        })
    }


    //Navigation
    fun nextHomePage(){
        navigate(IdentityloginFragmentDirections.actionIdentityloginFragmentToHomeFragment())
    }


}