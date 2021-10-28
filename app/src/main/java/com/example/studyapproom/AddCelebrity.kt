package com.example.studyapproom

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddCelebrity : AppCompatActivity() {

    private val connection by lazy { SubjectsDatabase.getInstance(this).subjectsDao() }

    private lateinit var titleEntry: EditText
    private lateinit var materialEntry: EditText
    private lateinit var detailsEntry: EditText
    private lateinit var saveButton: Button
    private lateinit var backButton: Button
    private lateinit var tableName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_celebrity)

        title="Add New Material"

        titleEntry= findViewById(R.id.titleEntry)
        materialEntry= findViewById(R.id.materialEntry)
        detailsEntry= findViewById(R.id.detailsEntry)
        saveButton= findViewById(R.id.saveButton)
        backButton= findViewById(R.id.backButton)

        tableName= intent.extras?.getString("tableName")!!

        backButton.setOnClickListener{
            finish()
        }

        saveButton.setOnClickListener{
            if (checkEntry()){
                if (tableName=="Kotlin") {
                    CoroutineScope(Dispatchers.IO).launch {
                        connection.addKotlinSubject(
                            Data(
                                0,
                                titleEntry.text.toString(),
                                materialEntry.text.toString(),
                                detailsEntry.text.toString()
                            )
                        )
                    }
                }
                else{
                    CoroutineScope(Dispatchers.IO).launch {
                        connection.addAndroidSubject(
                            DataAndroid(
                                0,
                                titleEntry.text.toString(),
                                materialEntry.text.toString(),
                                detailsEntry.text.toString()
                            )
                        )
                    }
                }
                StyleableToast.makeText(this,"Material Saved Successfully!!",R.style.mytoast).show()
                clearEntry()
            }
            else
                StyleableToast.makeText(this,"Please Enter Valid Values!!",R.style.mytoast).show()
        }

    }

    private fun clearEntry() {
        titleEntry.text.clear()
        materialEntry.text.clear()
        detailsEntry.text.clear()
    }

    private fun checkEntry(): Boolean {
        if (titleEntry.text.isBlank())
            return false
        if (materialEntry.text.isBlank())
            return false
        if (detailsEntry.text.isBlank())
            return false
        return true
    }
}