package com.example.studyapproom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.kotlinChoice -> {
                val intent = Intent(this, Kotlin::class.java)
                startActivity(intent)
                return true
            }
            R.id.androidChoice -> {
                startActivity(Intent(this, Android::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SubjectsDatabase.getInstance(this).subjectsDao()

        val kotlinButton=findViewById<Button>(R.id.kotlinButton)
        val androidButton=findViewById<Button>(R.id.androidButton)

        kotlinButton.setOnClickListener {
            startActivity(Intent(this, Kotlin::class.java))
        }

        androidButton.setOnClickListener {
            startActivity(Intent(this, Android::class.java))
        }

    }
}