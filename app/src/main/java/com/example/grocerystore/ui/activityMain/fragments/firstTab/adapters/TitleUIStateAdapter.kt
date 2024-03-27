package com.example.grocerystore.ui.activityMain.fragments.firstTab.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.grocerystore.R
import com.example.grocerystore.domain.models.item.TitleUIState
import com.example.grocerystore.databinding.TitleItemListBinding

class TitleUIStateAdapter( private val context: Context) : RecyclerView.Adapter<TitleUIStateAdapter.ItemViewHolder>() {


    companion object{
        const val TAG = "TitleUIStateAdapter"
    }

    private var data = listOf<TitleUIState>()

    lateinit var onClickListener: OnClickListener

        inner class ItemViewHolder(private val binding: TitleItemListBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(itemModel: TitleUIState) {

                binding.textView1TypeOfDish.text = itemModel.name

                if (itemModel.isSelected) {
                    binding.containerTextTitleItemList.setBackgroundColor(context.getColor(R.color.blue60))
                    binding.textView1TypeOfDish.setTextColor(context.getColor(R.color.white))
                } else {
                    binding.containerTextTitleItemList.setBackgroundColor(context.getColor(R.color.gray30))
                    binding.textView1TypeOfDish.setTextColor(context.getColor(R.color.black))
                }

                binding.containerTitleItemList.setOnClickListener {
                    onClickListener.onClick(itemModel)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val itemView = LayoutInflater.from(parent.context)
            return ItemViewHolder(TitleItemListBinding.inflate(itemView, parent, false))
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.bind(data[position])

        override fun getItemId(position: Int) = data[position].id.toLong()

        override fun getItemCount() = data.size

        @SuppressLint("NotifyDataSetChanged")
        fun setData(newList : List<TitleUIState>){
            Log.d(TAG, "newList $newList")
            data = newList
            notifyDataSetChanged()
        }

        @SuppressLint("NotifyDataSetChanged")
        fun markSelectedItem(itemData: TitleUIState): Boolean {
            for (item in data) {
                item.isSelected = itemData.id == item.id
            }
            notifyDataSetChanged()
            return true
        }

//        fun markSelectedItem(position: Int): Boolean {
//            for(item in data){
//                item.isSelected = false
//            }
//
//            val newItem = data[position]
//            newItem.isSelected = true
//            currentSelectedItemId = newItem.id
//
//            notifyDataSetChanged()
//            return true
//        }

//    fun getItemById(id : String) : TitleUIState?{
//        if (id != "-1") {
//            for (item in data) {
//                if(id == item.id){
//                    return item
//                }
//            }
//        }
//        return null
//    }


        interface OnClickListener {
            fun onClick(itemData: TitleUIState)
        }
    }