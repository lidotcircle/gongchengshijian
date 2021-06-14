package com.example.daoyun

class Course {
    private var imageId = -1
    private var imgFilePath = ""
    private var classId: String? = null
    private var courseName: String? = null
    private var teacherName: String? = null
    private var className: String? = null
    private var teacherPhone: String? = null

    fun Course(
        imageId: Int,
        courseName: String?,
        teacherName: String?,
        className: String?,
        classId: String?
    ) {
        this.classId = classId
        this.imageId = imageId
        this.courseName = courseName
        this.teacherName = teacherName
        this.className = className
    }

    fun Course(
        imgFilePath: String,
        courseName: String?,
        teacherName: String?,
        className: String?,
        classId: String?
    ) {
        this.classId = classId
        this.imgFilePath = imgFilePath
        this.courseName = courseName
        this.teacherName = teacherName
        this.className = className
    }

    fun getTeacherPhone(): String? {
        return teacherPhone
    }

    fun getClassId(): String? {
        return classId
    }

    fun getImgFilePath(): String? {
        return imgFilePath
    }

    fun getImageId(): Int {
        return imageId
    }

    fun getCourseName(): String? {
        return courseName
    }

    fun getTeacherName(): String? {
        return teacherName
    }

    fun getClassName(): String? {
        return className
    }
}
