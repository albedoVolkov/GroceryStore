package com.example.grocerystore.ui.activityMain.fragments.firstTab.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.grocerystore.data.helpers.UIstates.item.TitleUIState
import com.example.grocerystore.databinding.TitleItemListBinding

class TitleUIStateAdapter(list: List<TitleUIState>, private val context: Context) : RecyclerView.Adapter<TitleUIStateAdapter.ItemViewHolder>() {

        var data = list
        lateinit var onClickListener: OnClickListener

        inner class ItemViewHolder(binding: TitleItemListBinding) : RecyclerView.ViewHolder(binding.root) {
            private val titleTextView: TextView = binding.textView1TypeOfDish
            private val itemView : View = binding.containerTitleItemList
            fun bind(itemModel: TitleUIState) {
                itemView.setOnClickListener {
                    onClickListener.onClick(itemModel)
                }
                titleTextView.text = itemModel.name
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val itemView = LayoutInflater.from(parent.context)
            return ItemViewHolder(TitleItemListBinding.inflate(itemView, parent, false))
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.bind(data[position])

        override fun getItemId(position: Int) = data[position].id

        override fun getItemCount() = data.size

        interface OnClickListener {
            fun onClick(itemData: TitleUIState)
        }
    }