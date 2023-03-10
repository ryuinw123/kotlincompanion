package com.example.kmitlcompanion.ui.auth.utils

import android.util.Log
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import javax.inject.Inject

class ValidateUtils @Inject constructor() {

    fun validTextInput(textInput: String): String?{
        if(textInput.trim().isEmpty()) {
            return "ห้ามเว้นว่าง"
        }
        return null
    }

    fun validDropDownInput(textInput: String): String?{
        if (textInput.isEmpty()) {
            return "ห้ามเว้นช่องนี้ว่าง"
        }
        return null
    }

    fun checkAllFieldValid(
        nameLayout : TextInputLayout?,
        surnameLayout: TextInputLayout?,
        facultyLayout: TextInputLayout?,
        departLayout: TextInputLayout?,
        yearLayout : TextInputLayout?,
        name : TextView?,
        surname : TextView?,
        faculty : TextView?,
        department : TextView?,
        year : TextView?,
        ):Boolean{

        nameLayout?.helperText = validTextInput(name?.text.toString())
        surnameLayout?.helperText = validTextInput(surname?.text.toString())
        facultyLayout?.helperText = validDropDownInput(faculty?.text.toString())
        departLayout?.helperText = validDropDownInput(department?.text.toString())
        yearLayout?.helperText = validDropDownInput(year?.text.toString())

        val validName = nameLayout?.helperText == null
        val validSurname = surnameLayout?.helperText == null
        val validFaculty = facultyLayout?.helperText == null
        val validDepartment = departLayout?.helperText == null
        val validYear = yearLayout?.helperText == null

        Log.d("test_identity","$validName $validSurname $validFaculty $validDepartment $validYear")

        if (validName && validSurname && validFaculty && validDepartment && validYear){
            return true
        }
        return false
    }

}