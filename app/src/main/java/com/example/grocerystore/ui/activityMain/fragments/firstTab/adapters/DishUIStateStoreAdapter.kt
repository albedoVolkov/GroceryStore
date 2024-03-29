package com.example.grocerystore.ui.activityMain.fragments.firstTab.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocerystore.domain.models.item.DishUIState
import com.example.grocerystore.databinding.DishStoreItemListBinding

class DishUIStateStoreAdapter( private val context: Context) : RecyclerView.Adapter<DishUIStateStoreAdapter.ItemViewHolder>() {

    val TAG = "DishUIStateStoreAdapter"

    private var data = listOf<DishUIState>()

    lateinit var onClickListener: OnClickListener

    inner class ItemViewHolder(private val binding: DishStoreItemListBinding)  : RecyclerView.ViewHolder(binding.root) {

        fun bind(itemModel: DishUIState) {
            binding.containerImageDishStore.setOnClickListener {
                onClickListener.onClick(itemModel)
            }
            binding.textView1DishStore.text = itemModel.name

            Glide.with(context)
                .load(itemModel.image)
                .error(null)
                .placeholder(null)
                .into(binding.image1DishStore)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        return ItemViewHolder(DishStoreItemListBinding.inflate(itemView, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemId(position: Int) = data[position].id.toLong()

    override fun getItemCount() = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList : List<DishUIState>){
        Log.d(TAG, "newList $newList")
        data = newList
        notifyDataSetChanged()
    }

    interface OnClickListener {
        fun onClick(itemData: DishUIState)
    }
}