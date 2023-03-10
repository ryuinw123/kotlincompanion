package com.example.kmitlcompanion.ui.identitylogin.helper

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.DefaultLifecycleObserver
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentIdentityloginBinding
import com.example.kmitlcompanion.presentation.viewmodel.IdentityloginViewModel
import com.example.kmitlcompanion.ui.auth.utils.ValidateUtils
import com.example.kmitlcompanion.ui.identitylogin.DataFacultyDepart
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import com.mapbox.maps.extension.style.layers.properties.PropertyValue
import java.lang.ref.WeakReference
import javax.inject.Inject

class IdentityHelper @Inject constructor(
    val dataFacultyDepart: DataFacultyDepart,
    val validateUtils: ValidateUtils,
) : DefaultLifecycleObserver {

    private lateinit var viewModel: IdentityloginViewModel
    private lateinit var context: Context
    private lateinit var activity: Activity

    private lateinit var weakName : WeakReference<TextView>
    private lateinit var weakSurname: WeakReference<TextView>
    private lateinit var weakFaculty: WeakReference<MaterialAutoCompleteTextView>
    private lateinit var weakDepartment: WeakReference<MaterialAutoCompleteTextView>
    private lateinit var weakYear: WeakReference<MaterialAutoCompleteTextView>


    private lateinit var weakNameLayout : WeakReference<TextInputLayout>
    private lateinit var weakSurnameLayout: WeakReference<TextInputLayout>
    private lateinit var weakFacultyLayout : WeakReference<TextInputLayout>
    private lateinit var weakDepartLayout: WeakReference<TextInputLayout>
    private lateinit var weakYearLayout: WeakReference<TextInputLayout>

    private lateinit var weakSignInButton: WeakReference<Button>

    fun setup(viewModel: IdentityloginViewModel,context: Context,activity: Activity,
              name: TextView,
              surname : TextView,
              faculty : MaterialAutoCompleteTextView,
              department : MaterialAutoCompleteTextView,
              year : MaterialAutoCompleteTextView,
              nameLayout: TextInputLayout,
              surnameLayout: TextInputLayout,
            facultyLayout: TextInputLayout,
            departLayout: TextInputLayout,
            yearLayout: TextInputLayout,
            signInButton: Button){

        this.weakName = WeakReference(name)
        this.weakSurname = WeakReference(surname)
        this.weakFaculty = WeakReference(faculty)
        this.weakDepartment = WeakReference(department)
        this.weakYear = WeakReference(year)

        this.weakNameLayout = WeakReference(nameLayout)
        this.weakSurnameLayout = WeakReference(surnameLayout)
        this.weakFacultyLayout = WeakReference(facultyLayout)
        this.weakDepartLayout = WeakReference(departLayout)
        this.weakYearLayout = WeakReference(yearLayout)

        this.weakSignInButton = WeakReference(signInButton)

        this.context = context
        this.activity = activity
        this.viewModel = viewModel


        val facultyList = dataFacultyDepart.getFaculty()//arrayListOf<String>("Engineer1","Engineer2","Engineer3")
        val arrayAdapter1 = ArrayAdapter<String>(activity,
            R.layout.dropdown_item,facultyList)
        faculty?.setAdapter(arrayAdapter1)

        val yearList = arrayListOf<Int>(1,2,3,4,5,6,7,8)
        val arrayAdapter3 = ArrayAdapter<Int>(activity, R.layout.dropdown_item,yearList)
        year?.setAdapter(arrayAdapter3)

        //val departmentList = dataFacultyDepart.getDepart(arrayListOf<String>())//arrayListOf<String>(faculty + " CE",faculty + " EE",faculty + " CPE")
        val departmentList = arrayListOf<String>()
        val arrayAdapter2 = ArrayAdapter<String>(activity, R.layout.dropdown_item,departmentList)
        department?.setAdapter(arrayAdapter2)
        department?.isEnabled = false
        signInButton.isEnabled = false
    }


    //check require field
    fun inputListener(){
        name?.doAfterTextChanged {
            nameLayout?.helperText = validateUtils.validTextInput(name?.text.toString())
        }

        surname?.doAfterTextChanged {
            surnameLayout?.helperText = validateUtils.validTextInput(surname?.text.toString())
        }

        faculty?.doAfterTextChanged{
            facultyLayout?.helperText = validateUtils.validDropDownInput(faculty?.text.toString())
            department?.text = null
            if (!faculty?.text.toString().isEmpty()){
                //setDepartmentAdapter(binding.facultyList.text.toString())
                val departmentList = dataFacultyDepart.getDepart(faculty?.text.toString())//arrayListOf<String>(faculty + " CE",faculty + " EE",faculty + " CPE")
                val arrayAdapter2 = ArrayAdapter<String>(activity, R.layout.dropdown_item,departmentList)
                department?.setAdapter(arrayAdapter2)
                department?.isEnabled = true
            }
        }

        department?.doAfterTextChanged {
            departLayout?.helperText = validateUtils.validDropDownInput(department?.text.toString())
        }

        year?.doAfterTextChanged {
            yearLayout?.helperText = validateUtils.validDropDownInput(year?.text.toString())
        }
    }

    fun checkAllFieldValid(){
        val check = validateUtils.checkAllFieldValid(nameLayout,surnameLayout,facultyLayout,departLayout,yearLayout,name,surname,faculty,department,year)
        signInButton?.isEnabled = check
    }

//    fun getUserRoom(arr : ArrayList<Any>){
//        viewModel.getUserRoom(arr)
//    }
//
//    fun postUserData(arr : ArrayList<Any>){
//        viewModel.postUserData(arr)
//    }

//    fun nextHomePage(){
//        viewModel.nextHomePage()
//    }

    private val name
        get() = weakName?.get()

    private val surname
        get() = weakSurname?.get()

    private val faculty
        get() = weakFaculty?.get()

    private val department
        get() = weakDepartment?.get()

    private val year
        get() = weakYear?.get()

    private val nameLayout
        get() = weakNameLayout?.get()

    private val surnameLayout
        get() = weakSurnameLayout?.get()

    private val facultyLayout
        get() = weakFacultyLayout?.get()

    private val departLayout
        get() = weakDepartLayout?.get()

    private val yearLayout
        get() = weakYearLayout?.get()

    private val signInButton
        get() = weakSignInButton?.get()

}