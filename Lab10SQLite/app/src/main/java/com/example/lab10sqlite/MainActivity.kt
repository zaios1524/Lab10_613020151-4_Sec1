package com.example.lab10sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_edit_delete.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.insert_layout.view.*
import kotlinx.android.synthetic.main.insert_layout.view.edt_id
import kotlinx.android.synthetic.main.insert_layout.view.edt_name
import kotlinx.android.synthetic.main.activity_edit_delete.view.edt_age as edt_age1

class MainActivity : AppCompatActivity() {

    private lateinit var dbHandler:DatabaseHelper
    private  var  studentList = arrayListOf<Student>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHandler = DatabaseHelper.getInstance(this)
        dbHandler.writableDatabase
        callStudentData()
        recycler_view.adapter = StudentsAdapter(studentList,applicationContext)
        recycler_view.layoutManager = LinearLayoutManager(applicationContext)
        recycler_view.addItemDecoration(DividerItemDecoration(applicationContext,LinearLayoutManager.VERTICAL))
    }

    override  fun  onResume() {
        super.onResume()
        callStudentData()
    }
    fun callStudentData(){
        studentList.clear()
        studentList.addAll(dbHandler.getALLStudent())
        recycler_view.adapter?.notifyDataSetChanged()
    }
    fun addStudentDialog(v: View){
        val  mDialogView = LayoutInflater.from(this).inflate(R.layout.insert_layout,null)
        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mDialogView.btnAdd.setOnClickListener{
            var id = mDialogView.edt_id.text.toString()
            var name = mDialogView.edt_name.text.toString()
            var age = mDialogView.edt_age.text.toString().toInt()
            var result = dbHandler.insertStudent(Student(id=id,name=name,age=age))
            if(result > -1){
                Toast.makeText(applicationContext,"the student is inserted successfully",Toast.LENGTH_LONG).show()
                mAlertDialog.dismiss()
                callStudentData()
            }else{
                Toast.makeText(applicationContext,"Insert failure",Toast.LENGTH_LONG).show()
            }
            mDialogView.btnReset.setOnClickListener {
                mDialogView.edt_id.setText("")
                mDialogView.edt_name.setText("")
                mDialogView.edt_age.setText("")
            }
        }
    }
}
