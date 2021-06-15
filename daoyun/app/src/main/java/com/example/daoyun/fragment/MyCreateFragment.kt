package com.example.daoyun.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.example.daoyun.ClassTabActivity
import com.example.daoyun.Course
import com.example.daoyun.R
import com.example.daoyun.adapter.CourseAdapter
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.thread

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyCreateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyCreateFragment : Fragment() {
    var courseList: List<Course> = ArrayList<Course>()
    private val myJoinNum = 0
    var adapter: CourseAdapter? = null
    var listView: ListView? = null
    var progressDialog: ProgressBar? = null
    private lateinit var jwtToken:String
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_create, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progressDialog = ProgressBar(context)



        adapter = CourseAdapter(context!!, R.layout.course_item, courseList, 2)
        listView = activity!!.findViewById(R.id.list_view1)
        listView?.adapter = adapter
        listView?.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            val course: Course = courseList[position]
            val intent = Intent(context, ClassTabActivity::class.java)
            intent.putExtra("courseName", course.getCourseName())
            intent.putExtra("classId", course.getClassId())
            intent.putExtra("enterType", "create")
            startActivity(intent)
        }
    }

    private fun getCourses(){
        thread {
            try {
                val json = JSONObject()
                    .put("role", "student")
                val stringBody =json.toString().toRequestBody("application/json;charset=utf-8".toMediaType())
                val client=OkHttpClient()
                val request=Request.Builder()
                    .url("https://gcsj.lidotcircle.ltd/apis/course/page")
                    .get ()
                    .build()
                val response=client.newCall(request).execute()
                val responseData=response.body?.string()
            }catch (e: Exception){
                Log.e("TAG", Log.getStackTraceString(e));
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyCreateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyCreateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}