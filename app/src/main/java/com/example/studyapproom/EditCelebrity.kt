package com.example.studyapproom

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.*

class EditCelebrity : AppCompatActivity() {

    private val connection by lazy { SubjectsDatabase.getInstance(this).subjectsDao() }

    private lateinit var titleEntry: EditText
    private lateinit var materialEntry: EditText
    private lateinit var detailsEntry: EditText
    private lateinit var editButton: Button
    private lateinit var backButton: Button
    private lateinit var tableName: String
    private lateinit var myTitle: String
    private lateinit var myMaterial: String
    private lateinit var myDetails: String
    private lateinit var dataList: ArrayList<Data>
    private lateinit var dataList2: ArrayList<DataAndroid>
    private var pk: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_delete_celebrity)

        title="Edit Material"

        titleEntry= findViewById(R.id.titleEntry)
        materialEntry= findViewById(R.id.materialEntry)
        detailsEntry= findViewById(R.id.detailsEntry)
        editButton= findViewById(R.id.editButton)
        backButton= findViewById(R.id.backButton)

        tableName= intent.extras?.getString("tableName")!!
        pk= intent.extras?.getInt("pk")!!

        dataList= arrayListOf()
        dataList2= arrayListOf()

        CoroutineScope(Dispatchers.IO).launch {
            val data= async {
                connection.gettingAllKotlinSubjects()
            }.await()
            val data2 = async {
                connection.gettingAllAndroidSubjects()
            }.await()
            withContext(Dispatchers.Main) {
                dataList.addAll(data)
                dataList2.addAll(data2)
                setHintData()
            }
        }

        backButton.setOnClickListener{
            finish()
        }

        editButton.setOnClickListener{
            if (checkEntry()) {
                CoroutineScope(Dispatchers.IO).launch {
                    if (tableName == "Kotlin")
                        connection.updateKotlinSubject(Data(pk, myTitle, myMaterial, myDetails))
                    else
                        connection.updateAndroidSubject(DataAndroid(pk, myTitle, myMaterial, myDetails))
                }
                StyleableToast.makeText(this, "Material Updated Successfully!!", R.style.mytoast)
                    .show()
                clearEntry()
            }
            else
                StyleableToast.makeText(this,"Please Enter Valid Values!!",R.style.mytoast).show()
        }

    }

    private fun setHintData() {
        if (tableName=="Kotlin") {
            for (data in dataList) {
                if (data.pk == pk) {
                    myTitle = data.title
                    titleEntry.hint = myTitle
                    myMaterial = data.material
                    materialEntry.hint = myMaterial
                    myDetails = data.details
                    detailsEntry.hint = myDetails
                }
            }
        }
        else{
            for (data in dataList2) {
                if (data.pk == pk) {
                    myTitle = data.title
                    titleEntry.hint = myTitle
                    myMaterial = data.material
                    materialEntry.hint = myMaterial
                    myDetails = data.details
                    detailsEntry.hint = myDetails
                }
            }
        }
    }

    private fun clearEntry() {
        titleEntry.text.clear()
        materialEntry.text.clear()
        detailsEntry.text.clear()
        titleEntry.hint= myTitle
        materialEntry.hint= myMaterial
        detailsEntry.hint= myDetails
    }

    private fun checkEntry(): Boolean {
        var check= false
        if (titleEntry.text.isNotBlank()) {
            myTitle= titleEntry.text.toString()
            check = true
        }
        if (materialEntry.text.isNotBlank()) {
            myMaterial= materialEntry.text.toString()
            check = true
        }
        if (detailsEntry.text.isNotBlank()) {
            myDetails= detailsEntry.text.toString()
            check = true
        }
        return check
    }

}