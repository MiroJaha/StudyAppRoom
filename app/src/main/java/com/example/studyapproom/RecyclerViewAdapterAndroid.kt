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

class RecyclerViewAdapterAndroid (private val context: Context, private val list:ArrayList<DataAndroid>): RecyclerView.Adapter<RecyclerViewAdapterAndroid.ItemViewHolder2>(){

    private lateinit var myListener: OnItemClickListener2
    private val connection by lazy { SubjectsDatabase.getInstance(context).subjectsDao()}

    interface OnItemClickListener2{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener:OnItemClickListener2 ){
        myListener=listener
    }

    class ItemViewHolder2 (itemView : View, listener: OnItemClickListener2): RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder2 {
        return ItemViewHolder2(
            LayoutInflater.from(parent.context).inflate(
                R.layout.items_view,
                parent,
                false
            )
            ,myListener
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder2, position: Int) {
        val subject=list[position]


        holder.itemView.apply {
            Title.text= subject.title
            Material.text= subject.material

            deleteImage.setOnClickListener{
                CoroutineScope(Dispatchers.IO).launch {
                    connection.deleteAndroidSubject(
                        DataAndroid(subject.pk,subject.title,subject.material,subject.details)
                    )
                }
                StyleableToast.makeText(context,"Material Deleted Successfully!!",R.style.mytoast).show()
                list.clear()
                CoroutineScope(Dispatchers.IO).launch {
                    val data = async {
                        connection.gettingAllAndroidSubjects()
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
                intent.putExtra("tableName","Android")
                intent.putExtra("pk",subject.pk)
                startActivity(context, intent, null)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}