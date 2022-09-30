package com.example.kmitlcompanion.data

import com.google.gson.annotations.SerializedName

data class test(
    val userId:Int,
    val id:Int,
    @SerializedName("title")
    val test_title:String,
    val completed:Boolean
){

}


