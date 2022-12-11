package com.example.kmitlcompanion.ui.identitylogin

import javax.inject.Inject

class DataFacultyDepart @Inject constructor() {

    private val FacultyMap: HashMap<String,ArrayList<String>> = hashMapOf(
        "คณะวิศวกรรมศาสตร์" to arrayListOf<String>(
            "วิศวกรรมโทรคมนาคมและโครงข่าย","วิศวกรรมไฟฟ้า","วิศวกรรมอิเล็กทรอนิกส์",
            "วิศวกรรมคอมพิวเตอร์","วิศวกรรมคอมพิวเตอร์(ต่อเนื่อง)","วิศวกรรมเครื่องกล",
            "วิศวกรรมขนส่งทางราง","วิศวกรรมการวัดคุม(ต่อเนื่อง)","วิศวกรรมเมคคาทรอนิกส์และออโตเมชัน",
            "วิศวกรรมโยธา","วิศวกรรมโยธา(ต่อเนื่อง)","วิศวกรรมระบบอุตสาหกรรมการเกษตร(ต่อเนื่อง)",
            "วิศวกรรมเกษตรอัจฉริยะ","วิศวกรรมเคมี","วิศวกรรมอุตสาหการ","วิศวกรรมอาหาร",
            "B.Eng. Civil Engineering(International Program)","B.Eng. Biomedical Engineering(Internation Program)",
            "B.Eng. Software Engineering(International Program)","B.Eng. Mechanical Engineering(International Program)",
            "B.Eng. Chemical Engineering(International Program)","B.Eng. Electrical Engineering(Internation Program)",
            "B.Eng. Industrial Engineering and Logistics Management(International Program)","B.Eng. Computer Innovation Engineering(International Program)",
            "B.Eng. Robotics and AI Engineering(Internation Program)","B.Eng. Energy Engineering(Internation Program)","B. Eng. Financial Enineering(International Program)",
            "B.Eng. Engineering Management and Entrepreneurship(Internation Program)"
        ),
        "คณะสถาปัตยกรรม ศิลปะและการออกแบบ" to arrayListOf<String>(
            "สถาปัตยกรรมหลัก","ภูมิสถาปัตยกรรม","สถาปัตยกรรมภายใน","ศิลปอุตสาหกรรม" ,"สาขาวิชาการออกแบบสนเทศสามมิติ และสื่อบูรณาการ",
            "การถ่ายภาพ","นิเทศศิลป์","ภาพยนตร์และดิจิทัล มีเดีย","สาขาวิชาศิลปกรรม มีเดียอาร์ต และอิลลัสเตชั่นอาร์ต","สาขาวิชาสถาปัตยกรรม(หลักสูตรนานาชาติ)"
        ),
        "คณะครุศาสตร์อุตสาหกรรมและเทคโนโลยี" to arrayListOf<String>(
            "ครุศาสตร์สถาปัตยกรรม","ครุศาสตร์การออกแบบสภาพแวดล้อมภายใน","ครุศาสตร์การออกแบบ","ครุศาสตร์วิศวกรรม","สาขาวิชาเทคโนโลยีอิเล็กทรอนิกส์",
            "ครุศาสตร์เกษตร","ครุศาสตร์สถาปัตยกรรมและการออกแบบ"
        ),
        "คณะเทคโนโลยีการเกษตร" to arrayListOf<String>(
            "นวัตกรรมการผลิตสัตว์น้ำและการจัดการทรัพยากรประมง","การจัดการฟาร์มอย่างชาญฉลาด","ภูมิทัศน์เพื่อสิ่งแวดล้อม",
            "เทคโนโลยีการผลิตพืช","เทคโนโลยีการผลิตสัตว์และวิทยาศาสตร์เนื้อสัตว์","พัฒนาการเกษตร","นิเทศศาสตร์เกษตร"
        ),
        "คณะวิทยาศาสตร์" to arrayListOf<String>(
            "เคมีสิ่งแวดล้อม","เคมีอุตสาหกรรม","เทคโนโลยีชีวภาพ","จุลชีววิทยาอุตสาหกรรม","วิทยาการคอมพิวเตอร์","คณิตศาสตร์ประยุกต์",
            "ฟิสิกส์อุตสาหกรรม","สถิติประยุกต์","Industrial and Engineering Chemistry(International program)",
            "Applied Microbiology(International program)"
        ),
        "คณะเทคโนโลยีสารสนเทศ" to arrayListOf<String>(
            "เทคโนโลยีสารสนเทศ","วิทยาการข้อมูลและการวิเคราะห์เชิงธุรกิจ","Business Information Technology",
        ),
        "คณะอุตสาหกรรมอาหาร" to arrayListOf<String>(
            "เทคโนโลยีการหมักในอุตสาหกรรมอาหาร","วิทยาศาสตร์และเทคโนโลยีการอาหาร","วิศวกรรมแปรรูปอาหาร","Culinary Science and Foodservice Management(International program)"
        ),
        "เทคโนโลยีและนวัตกรรมวัสดุ" to arrayListOf<String>(
            "นาโนวิทยาและนาโนเทคโนโลยี","Dual Bachelor’s Degree Program consists of Bachelor of Engineering(Smart Materials Technology) and Bachelor of Engineering(Robotics and AI Engineering)",
        ),
        "นวัตกรรมการผลิตขั้นสูง" to arrayListOf<String>(
            "วิศวกรรมระบบการผลิต"
        ),
        "คณะบริหารธุรกิจ" to arrayListOf<String>(
            "บริหารธุรกิจบัณฑิต","เศรษฐศาสตร์ธุรกิจและการจัดการ","บริหารธุรกิจบัณฑิต(หลักสูตรนานาชาติ)","Bachelor of Business Administration Program in Global Entrepreneurship(International Program)"
        ),
        "อุตสาหกรรมการบินนานาชาติ" to arrayListOf<String>(
            "วิศวกรรมการบินและนักบินพาณิชย์(นานาชาติ)","การจัดการโลจิสติกส์(นานาชาติ)"
        ),
        "คณะศิลปศาสตร์" to arrayListOf<String>(
            "ภาษาอังกฤษ","ภาษาญี่ปุ่น","นวัตกรรมการท่องเที่ยวและการบริการ",
        ),
        "คณะแพทยศาสตร์" to arrayListOf<String>(
            "แพทยศาสตรบัณฑิต(นานาชาติ)"
        ),
        "วิศวกรรมสังคีต" to arrayListOf<String>(
            "วิศวกรรมดนตรีและสื่อประสม"
        ),
    )

    fun getFaculty():ArrayList<String>{
        return ArrayList(FacultyMap.keys)!!
    }

    fun getDepart(fac : String):ArrayList<String>{
        return FacultyMap[fac]!!
    }

}