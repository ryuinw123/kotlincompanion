package com.example.kmitlcompanion.ui.identitylogin.utils

class ValidationMock {
    fun checkAllFieldValid(name : String , helperName : String):Boolean{

        if (validate(name))
            if (helperName == null)
                return true
        return false
    }

    fun validate(args : String) : Boolean{
        val inputText = args
        if(inputText.isEmpty() || inputText.matches("(.*[^A-Z|a-z].*)".toRegex())){
            return false
        }
        return true
    }

}