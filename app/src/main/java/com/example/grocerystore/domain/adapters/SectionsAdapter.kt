package com.example.grocerystore.domain.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.grocerystore.R
import com.example.grocerystore.domain.helpers.Section
import java.io.InputStream
import java.lang.RuntimeException
import java.net.URL

class SectionsAdapter(private val sectionsClickListener: SectionsClickListener) : RecyclerView.Adapter<SectionsAdapter.SectionsViewHolder>() {
    private var list = listOf<Section>()

    interface SectionsClickListener {
        fun onItemClick(id: Long, itemView: View)
    }

    override fun getItemCount(): Int { return list.size }

    inner class SectionsViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
        private lateinit var titleTextView: TextView
        private lateinit var imageView: ImageView
        init {
            titleTextView = itemView.findViewById(R.id.text_view_item_section)
            imageView = itemView.findViewById(R.id.image_view_item_section)
            itemView.setOnClickListener { sectionsClickListener.onItemClick(list[adapterPosition].id, itemView) }
        }


        fun bind(sectionModel: Section) {
            titleTextView.text = sectionModel.name.toString()
            imageView = TODO()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        return SectionsViewHolder(itemView.inflate(R.layout.item_section, parent, false),viewType)}

    override fun onBindViewHolder(holder: SectionsViewHolder, position: Int) {
        holder.bind(list[position])
    }


    fun setList(listNew : List<Section>){
        list = listNew
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return list[position].id
    }
}