package com.example.grocerystore.ui.activityMain.fragments.firstTab.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.grocerystore.data.helpers.UIstates.item.TitleUIState
import com.example.grocerystore.databinding.TitleItemListBinding

class TitleUIStateAdapter( private val context: Context) : RecyclerView.Adapter<TitleUIStateAdapter.ItemViewHolder>() {

    val TAG = "TitleUIStateAdapter"

    private var data = listOf<TitleUIState>()

    lateinit var onClickListener: OnClickListener

        inner class ItemViewHolder(private val binding: TitleItemListBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(itemModel: TitleUIState) {
                binding.containerTitleItemList.setOnClickListener {
                    onClickListener.onClick(itemModel)
                }
                binding.textView1TypeOfDish.text = itemModel.name
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val itemView = LayoutInflater.from(parent.context)
            return ItemViewHolder(TitleItemListBinding.inflate(itemView, parent, false))
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.bind(data[position])

        override fun getItemId(position: Int) = data[position].id.toLong()

        override fun getItemCount() = data.size

        fun setData(newList : List<TitleUIState>){
            Log.d(TAG, "newList ${newList.toString()}")
            data = newList
            notifyDataSetChanged()
        }


        interface OnClickListener {
            fun onClick(itemData: TitleUIState)
        }
    }