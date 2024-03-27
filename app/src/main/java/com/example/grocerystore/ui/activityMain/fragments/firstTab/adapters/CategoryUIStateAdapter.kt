package com.example.grocerystore.ui.activityMain.fragments.firstTab.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocerystore.R
import com.example.grocerystore.domain.models.item.CategoryUIState
import com.example.grocerystore.databinding.CategoryListItemBinding

class CategoryUIStateAdapter(private val context: Context) : RecyclerView.Adapter<CategoryUIStateAdapter.ItemViewHolder>() {

    val TAG = "CategoryUIStateAdapter"

    private var data : List<CategoryUIState> = listOf()

    lateinit var onClickListener: OnClickListener

    inner class ItemViewHolder(private var binding: CategoryListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(itemModel: CategoryUIState) {
            binding.itemCardCategory.setOnClickListener {
                onClickListener.onClick(itemModel)
            }
            binding.textViewCategory.text = itemModel.name

            if (itemModel.image.isNotEmpty()) {
                Glide.with(context)
                    .load(itemModel.image)
                    .error(R.drawable.not_loaded_one_image)
                    .placeholder(R.drawable.not_loaded_one_image)
                    .into(binding.imageViewCategory)
            }else{
                binding.imageViewCategory.setImageDrawable(
                    AppCompatResources.getDrawable(
                        context,
                        R.drawable.not_loaded_one_image))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        return ItemViewHolder(CategoryListItemBinding.inflate(itemView, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemId(position: Int) = data[position].id.toLong()

    override fun getItemCount() = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList : List<CategoryUIState>){
        Log.d(TAG, "newList $newList")
        data = newList
        notifyDataSetChanged()
    }

    interface OnClickListener {
        fun onClick(itemData: CategoryUIState)
    }
}