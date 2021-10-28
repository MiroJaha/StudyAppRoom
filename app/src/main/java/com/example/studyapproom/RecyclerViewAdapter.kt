package com.example.studyapproom

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.android.synthetic.main.items_view.view.*
import kotlinx.coroutines.*
import kotlin.properties.Delegates

class RecyclerViewAdapter (private val context: Context, private val list:ArrayList<Data>): RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>(){

    private lateinit var myListener: OnItemClickListener
    private val connection by lazy { SubjectsDatabase.getInstance(context).subjectsDao() }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener:OnItemClickListener ){
        myListener=listener
    }

    class ItemViewHolder (itemView : View,listener: OnItemClickListener): RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.items_view,
                parent,
                false
            )
        ,myListener
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val subject=list[position]


        holder.itemView.apply {
            Title.text= subject.title
            Material.text= subject.material

            deleteImage.setOnClickListener{
                CoroutineScope(Dispatchers.IO).launch {
                    connection.deleteKotlinSubject(
                        Data(subject.pk,subject.title,subject.material,subject.details))
                }
                StyleableToast.makeText(context,"Material Deleted Successfully!!",R.style.mytoast).show()
                list.clear()
                CoroutineScope(Dispatchers.IO).launch {
                    val data = async {
                        connection.gettingAllKotlinSubjects()
                    }.await()
                    withContext(Dispatchers.Main) {
                        if (data.isNotEmpty())
                            list.addAll(data)
                        notifyDataSetChanged()
                    }
                }
            }
            editImage.setOnClickListener{
                val intent= Intent(context,EditCelebrity::class.java)
                intent.putExtra("tableName","Kotlin")
                intent.putExtra("pk",subject.pk)
                startActivity(context,intent,null)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}