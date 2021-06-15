package com.example.daoyun

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class CreateClassActivity : AppCompatActivity() {

    private var classIconIV: ImageView? = null
    private var termLayout: LinearLayout? = null
    private var schoolLayout:LinearLayout?=null
    private var facultyLayout:LinearLayout?=null
    private var termTV: TextView? = null
    private var schoolTV:TextView?=null
    private var facultyTV:TextView?=null
    private var classNameET: EditText? = null
    private var gradeClassET: EditText? = null
    private var classIntroductionET: EditText? = null
    private var createClassBtn: Button? = null
    private var backBtn: Button? = null
    private val IMAGE_SELECT = 1
    private val IMAGE_CUT = 2
    private var cropFile: File? = null
    private var selectedTerm: String? = null
    private var selectedSchool: String? = null
    private var selectedFaculty: String? = null
    private var courseExId:String?=null
    private lateinit var jwtToken:String
    private var debugmsg:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_class)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        termTV = findViewById(R.id.term_Tv)
        schoolTV=findViewById(R.id.school_Tv)
        facultyTV=findViewById(R.id.faculty_Tv)
        classNameET = findViewById(R.id.class_name_Et)
        gradeClassET = findViewById(R.id.grade_class_Et)
        classIntroductionET = findViewById(R.id.class_introduction_Et)
        createClassBtn = findViewById(R.id.create_class_btn)
        backBtn = findViewById(R.id.toolbar_left_btn)
        backBtn?.setOnClickListener(View.OnClickListener { finish() })
        classIconIV = findViewById(R.id.class_icon_Iv)
        val userData=getSharedPreferences("userData", Context.MODE_PRIVATE)
        jwtToken= userData.getString("jwtToken","error").toString()

        classIconIV?.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_PICK, null)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intent, IMAGE_SELECT)
        })

        val term = arrayOf(
            "2016-2017-1",
            "2016-2017-2",
            "2017-2018-1",
            "2017-2018-2",
            "2018-2019-1",
            "2018-2019-2",
            "2019-2020-1",
            "2019-2020-2",
            "2020-2021-1",
            "2020-2021-2",
            "2021-2022-1",
            "2021-2022-2",
            "2022-2023-1",
            "2022-2023-2",
            "2023-2024-1",
            "2023-2024-2",
            "2024-2025-1",
            "2024-2025-2",
            "2025-2026-1",
            "2025-2026-2",
            "2026-2027-1",
            "2026-2027-2"
        )
        termLayout = findViewById(R.id.term_layout)
        termLayout?.setOnClickListener(View.OnClickListener {
            val builder =
                AlertDialog.Builder(this@CreateClassActivity)
                    .setTitle("选择班课学期")
                    .setSingleChoiceItems(
                        term, -1
                    ) { _, which -> selectedTerm = term[which] }
            builder.setPositiveButton(
                "确定"
            ) { _, _ -> termTV?.text = selectedTerm }
            builder.setNegativeButton("取消", null)
            builder.show()
        })

        val school= arrayOf("福州大学","其他")
        schoolLayout=findViewById(R.id.school_layout)
        schoolLayout?.setOnClickListener(View.OnClickListener {
            val builder =
                AlertDialog.Builder(this@CreateClassActivity)
                    .setTitle("选择学校")
                    .setSingleChoiceItems(
                        school, -1
                    ) { _, which -> selectedSchool = school[which] }
            builder.setPositiveButton(
                "确定"
            ) { _, _ -> schoolTV?.text = selectedSchool }
            builder.setNegativeButton("取消", null)
            builder.show()
        })

        val faculty= arrayOf("数学与计算机科学学院","其他")
        facultyLayout=findViewById(R.id.faculty_layout)
        facultyLayout?.setOnClickListener(View.OnClickListener {
            val builder =
                AlertDialog.Builder(this@CreateClassActivity)
                    .setTitle("选择学院")
                    .setSingleChoiceItems(
                        faculty, -1
                    ) { _, which -> selectedFaculty = faculty[which] }
            builder.setPositiveButton(
                "确定"
            ) { _, _ -> facultyTV?.text = selectedFaculty }
            builder.setNegativeButton("取消", null)
            builder.show()
        })

        createClassBtn?.setOnClickListener(View.OnClickListener {
            if (classNameET?.text.toString() == "") {
                showAlertDialog("请输入班课名！")
            } else if (schoolTV?.text.toString() == "学校") {
                showAlertDialog("请输入学校！")
            } else if (gradeClassET?.text.toString() == "") {
                showAlertDialog("请输入班级！")
            } else if (termTV?.text.toString() == "班课学期未选择") {
                showAlertDialog("请选择班课学期！")
            } else if (classIntroductionET?.text.toString() == "") {
                showAlertDialog("请输入班课简介！")
            }
            else{
                //addClass(classNameET.toString())
                Toast.makeText(this,"$jwtToken\n创建成功\n$debugmsg",Toast.LENGTH_LONG).show()
                finish()
            }
        })
    }

    private fun addClass(courseName:String, briefDescription:String){
        thread {
            try {
                val json = JSONObject()
                    .put("courseName", courseName)
                val stringBody =json.toString().toRequestBody("application/json;charset=utf-8".toMediaType())
                val client=OkHttpClient()
                val request=Request.Builder()
                    .url("https://gcsj.lidotcircle.ltd/apis/course")
                    .header("Authorization",jwtToken)
                    .post(stringBody)
                    .build()
                val response=client.newCall(request).execute()
                val responseData=response.body?.string()
                debugmsg=responseData.toString()
                courseExId = JSONObject(responseData).getString("courseExId")
            }catch (e: Exception){
                Log.e("TAG", Log.getStackTraceString(e));
            }
        }.join()
    }

    private fun startCropImage(uri: Uri?) {
        try {
            val intent = Intent("com.android.camera.action.CROP")
            val timeStamp =
                SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            intent.setDataAndType(uri, "image/*")
            // 使图片处于可裁剪状态
            intent.putExtra("crop", "true")
            // 裁剪框的比例（根据需要显示的图片比例进行设置）
            intent.putExtra("aspectX", 1)
            intent.putExtra("aspectY", 1)
            // 让裁剪框支持缩放
            intent.putExtra("scale", true)
            // 裁剪后图片的大小（注意和上面的裁剪比例保持一致）
            intent.putExtra("outputX", 1000)
            intent.putExtra("outputY", 1000)
            // 传递原图路径
            cropFile = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/daoyun/" + timeStamp + ".jpg"
            )
            //            cropFile = new File(path + File.separator + MainActivity.userName + ".jpg");
            if (cropFile!!.exists()) {
                cropFile!!.delete()
            }
            val cropImageUri = Uri.fromFile(cropFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri)
            // 设置裁剪区域的形状，默认为矩形，也可设置为原形
            //intent.putExtra("circleCrop", true);
            // 设置图片的输出格式
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
            // return-data=true传递的为缩略图，小米手机默认传递大图，所以会导致onActivityResult调用失败
            intent.putExtra("return-data", false)
            // 是否需要人脸识别
            intent.putExtra("noFaceDetection", true)
            intent.flags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, IMAGE_CUT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showAlertDialog(msg: String?) {
        runOnUiThread {
            Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
        }
    }
}
