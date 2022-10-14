package com.example.kmitlcompanion.ui.identitylogin

import javax.inject.Inject

class DataFacultyDepart @Inject constructor() {

    private val FacultyMap: HashMap<String,ArrayList<String>> = hashMapOf(
        "Engineer" to arrayListOf<String>(
            "Computer Engineering","Electrical Engineering","Mechanical Engineering",
            "Telecommunications Engineering","Electronics Engineering","Civil Engineering"
        ),
        "Science" to arrayListOf<String>(
            "Bio","Chem","ComSci",
        )
    )

    fun getFaculty():ArrayList<String>{
        return ArrayList(FacultyMap.keys)!!
    }

    fun getDepart(fac : String):ArrayList<String>{
        return FacultyMap[fac]!!
    }

}