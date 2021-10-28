package com.example.studyapproom

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class Kotlin : AppCompatActivity() {

    private val connection by lazy { SubjectsDatabase.getInstance(this).subjectsDao() }

    private lateinit var recyclerView : RecyclerView
    private lateinit var list: ArrayList<Data>
    private lateinit var addButton: FloatingActionButton
    private lateinit var adapter: RecyclerViewAdapter

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val item1=menu!!.getItem(0)
        val item2=menu.getItem(1)
        item1.title="Main Menu"
        item2.title="Android Studio Review"
        return super.onPrepareOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.kotlinChoice -> {
                val intent = Intent(this, MainActivity::class.java)
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
        setContentView(R.layout.study_list)

        title="Kotlin Review"

        recyclerView=findViewById(R.id.listStudy)
        addButton= findViewById(R.id.addNew)

        list= arrayListOf()

        val dialogBuilder = AlertDialog.Builder(this)

        val listTitle= listOf(
            "var and val",
            "try and catch",
            "when"
        )
        val listMaterials= listOf(
            "Use to Initialize Variables",
            "Use To Catch Errors",
            "Use for Multiple Choices"
        )

        val listDetails= listOf(
            "The Value of Variable (var) Can be Change\nThe Value of Variable (val) can't be Change",
            "You also use it to let the user enter only specific type of data",
            "Has the same use as if/else but more shorter"
        )

        adapter=RecyclerViewAdapter(this@Kotlin,list)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this@Kotlin)

        CoroutineScope(IO).launch {
            val data = async {
                connection.gettingAllKotlinSubjects()
            }.await()
            withContext(Main) {
                if (data.isNotEmpty())
                    list.addAll(data)
                CoroutineScope(IO).launch {
                    for (int in listTitle.indices) {
                        var check = false
                        for (i in list) {
                            if (i.title.contains(listTitle[int]))
                                check = true
                        }
                        if (!check) {
                            connection.addKotlinSubject(
                                Data(0, listTitle[int], listMaterials[int], listDetails[int])
                            )
                        }
                    }
                }
            }
        }

        CoroutineScope(IO).launch {
            val data = async {
                connection.gettingAllKotlinSubjects()
            }.await()
            withContext(Main) {
                list.clear()
                list.addAll(data)
                adapter.notifyDataSetChanged()
            }
        }

        adapter.setOnItemClickListener(object : RecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                dialogBuilder.setMessage("Details:\n${list[position].details}")
                    .setCancelable(false)
                    .setPositiveButton("OK") { dialog, _ -> dialog.cancel() }

                val alert=dialogBuilder.create()
                alert.setTitle(list[position].title)
                alert.show()
            }
        })
        addButton.setOnClickListener{
            val intent= Intent(this,AddCelebrity::class.java)
            intent.putExtra("tableName","Kotlin")
            startActivity(intent)
        }

    }

    override fun onResume() {
        CoroutineScope(IO).launch {
            val data = async {
                connection.gettingAllKotlinSubjects()
            }.await()
            if (data.isNotEmpty())
                withContext(Main) {
                    list.clear()
                    list.addAll(data)
                    adapter.notifyDataSetChanged()
                }
        }
        super.onResume()
    }
}