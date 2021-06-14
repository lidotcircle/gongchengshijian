package com.example.daoyun.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.daoyun.Course
import com.example.daoyun.R

class CourseAdapter : ArrayAdapter<Course?> {
    private var resourceId: Int
    private var flag = 1

    constructor(
        context: Context,
        textViewResourceId: Int,
        objects: List<Course?>
    ) : super(context, textViewResourceId, objects) {
        resourceId = textViewResourceId
    }

    constructor(
        context: Context,
        textViewResourceId: Int,
        objects: List<Course?>,
        flag: Int
    ) : super(context, textViewResourceId, objects) {
        resourceId = textViewResourceId
        this.flag = flag
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val course: Course? = getItem(position)
        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, parent, false)
            viewHolder = ViewHolder()
            viewHolder.courseImage = view.findViewById(R.id.course_image)
            viewHolder.courseName = view.findViewById(R.id.course_name)
            viewHolder.teacherName = view.findViewById(R.id.teacher_name)
            viewHolder.className = view.findViewById(R.id.class_name)
            viewHolder.signInImg = view.findViewById(R.id.signIn_Iv)
            viewHolder.signInTv = view.findViewById(R.id.signIn_Tv)
            //            if(flag != 1){
            viewHolder.signInImg?.visibility = View.INVISIBLE
            viewHolder.signInTv?.visibility = View.INVISIBLE
            //            }
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        if (course!=null&&course?.getImgFilePath().equals("")) {
            viewHolder.courseImage!!.setImageResource(course.getImageId())
            viewHolder.courseName?.text = course.getCourseName()
            viewHolder.teacherName?.text = course.getTeacherName()
            viewHolder.className?.text = course.getClassName()
        } else if (course!=null&&course.getImageId() === -1) {
            viewHolder.courseImage!!.setImageBitmap(BitmapFactory.decodeFile(course.getImgFilePath()))
            viewHolder.courseName?.text = course.getCourseName()
            viewHolder.teacherName?.text = course.getTeacherName()
            viewHolder.className?.text = course.getClassName()
        }
        if (flag == 1) {
            viewHolder.signInImg!!.setOnClickListener {
            }
            viewHolder.signInTv!!.setOnClickListener {
            }
        }
        return view
    }

    internal inner class ViewHolder {
        var courseImage: ImageView? = null
        var courseName: TextView? = null
        var teacherName: TextView? = null
        var className: TextView? = null
        var signInImg: ImageView? = null
        var signInTv: TextView? = null
    }
}
