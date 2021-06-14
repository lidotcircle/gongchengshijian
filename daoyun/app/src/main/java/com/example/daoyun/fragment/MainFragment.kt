package com.example.daoyun.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.daoyun.CreateClassActivity
import com.example.daoyun.R
import com.example.daoyun.databinding.FragmentMainBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var addTV: TextView? = null
    private var myCreateTV: TextView? = null
    private var myJoinTV: TextView? = null
    private var myCreateView: View? = null
    private var myJoinView: View? = null
    private var myCreateFragment: MyCreateFragment = MyCreateFragment()
    private var myJoinFragment: MyJoinFragment = MyJoinFragment()

    private lateinit var binding: FragmentMainBinding

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
        val view = inflater.inflate(R.layout.fragment_main, null)
        addTV = view.findViewById(R.id.toolbar_right_tv)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?){
        super.onActivityCreated(savedInstanceState)
        val activity = activity as AppCompatActivity?

        myCreateView = activity!!.findViewById(R.id.view_mycreate)
        myJoinView = activity!!.findViewById(R.id.view_myjoin)
        myCreateTV = activity!!.findViewById(R.id.myCreateTv)
        myJoinTV = activity!!.findViewById(R.id.joinedClassTv)
        myJoinTV?.setTextColor(Color.parseColor("#ff00bfff"))
        myCreateView?.visibility = View.INVISIBLE

        activity.supportFragmentManager
            .beginTransaction()
            .add(R.id.container_content_layout, myCreateFragment)
            .add(R.id.container_content_layout, myJoinFragment)
            .hide(myCreateFragment)
            .commit()

        addTV!!.isEnabled = true
        addTV!!.setOnClickListener {
            showPopupMenu(addTV!!)
        }

        myJoinTV?.setOnClickListener(View.OnClickListener {
            myJoinTV?.setTextColor(Color.parseColor("#ff00bfff"))
            myCreateTV?.setTextColor(Color.parseColor("#80000000"))
            myJoinView?.visibility = View.VISIBLE
            myCreateView?.visibility = View.INVISIBLE
            activity.supportFragmentManager
                .beginTransaction()
                .show(myJoinFragment)
                .hide(myCreateFragment)
                .commit()
        })

        myCreateTV?.setOnClickListener(View.OnClickListener {
            myCreateTV?.setTextColor(Color.parseColor("#ff00bfff"))
            myJoinTV?.setTextColor(Color.parseColor("#80000000"))
            myCreateView?.visibility = View.VISIBLE
            myJoinView?.visibility = View.INVISIBLE
            activity.supportFragmentManager
                .beginTransaction()
                .show(myCreateFragment)
                .hide(myJoinFragment)
                .commit()
        })
    }

    private fun showPopupMenu(view: View) {
        // 这里的view代表popupMenu需要依附的view
        val popupMenu = PopupMenu(activity, view)
        // 获取布局文件
        popupMenu.menuInflater.inflate(R.menu.class_menu, popupMenu.menu)
        popupMenu.show()
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener { item -> // 控件每一个item的点击事件
            when (item.itemId) {
                R.id.myCreate -> startActivityForResult(
                    Intent(
                        context,
                        CreateClassActivity::class.java
                    ), 1
                )
                R.id.joinClass -> {
                    val editText = EditText(context)
                    val builder =
                        AlertDialog.Builder(context!!)
                            .setTitle("请输入七位班课号")
                            .setView(editText)
                    builder.setPositiveButton(
                        "确定"
                    ) { dialog, which ->
                        val classStr = editText.text.toString()
                        joinClass(classStr)
                    }
                    builder.setNegativeButton("取消", null)
                    builder.show()
                }
            }
            true
        }
        popupMenu.setOnDismissListener {
            // 控件消失时的事件
        }
    }

    private fun joinClass(classStr: String) {

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}