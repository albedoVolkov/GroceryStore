package com.example.grocerystore.domain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.grocerystore.R
import com.example.grocerystore.domain.helpers.Product
import com.example.grocerystore.domain.helpers.Section

class SectionsAdapter(private val sectionsClickListener: SectionsClickListener) : RecyclerView.Adapter<SectionsAdapter.SectionsViewHolder>() {
    private var list : List<Section> = listOf()

    interface SectionsClickListener {
        fun onItemClick(id: Int, itemView: View)
    }

    override fun getItemCount(): Int { return list.size }

    inner class SectionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var titleTextView: TextView
        private var imageView: ImageView
        init {
            titleTextView = itemView.findViewById(R.id.text_view_item_section)
            imageView = itemView.findViewById(R.id.image_view_item_section)
            itemView.setOnClickListener { sectionsClickListener.onItemClick(list[adapterPosition].id, itemView) }
        }


        fun bind(sectionModel: Section) {
            titleTextView.text = sectionModel.name//name

            imageView.load(sectionModel.image_url)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        return SectionsViewHolder(itemView.inflate(R.layout.item_section, parent, false))}

    override fun onBindViewHolder(holder: SectionsViewHolder, position: Int) {
        holder.bind(list[position])
    }


    fun setList(listNew : List<Section>){//Section
        list = listNew
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return list[position].id.toLong()
    }
}