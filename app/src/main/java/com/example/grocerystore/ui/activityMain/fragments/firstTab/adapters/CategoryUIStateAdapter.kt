package com.example.grocerystore.ui.activityMain.fragments.firstTab.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocerystore.R
import com.example.grocerystore.data.helpers.UIstates.item.CategoryUIState
import com.example.grocerystore.databinding.CategoryListItemBinding

class CategoryUIStateAdapter(list: List<CategoryUIState>, private val context: Context) : RecyclerView.Adapter<CategoryUIStateAdapter.ItemViewHolder>() {

    var data = list
    lateinit var onClickListener: OnClickListener

    inner class ItemViewHolder(binding: CategoryListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val titleTextView: TextView = binding.textViewCategory
        private val imageView: ImageView = binding.imageViewCategory
        private val itemCard: CardView = binding.itemCardCategory

        fun bind(itemModel: CategoryUIState) {
            itemCard.setOnClickListener {
                onClickListener.onClick(itemModel)
            }
            titleTextView.text = itemModel.name

            if (itemModel.image.isNotEmpty()) {
                Glide.with(context)
                    .load(itemModel.image)
                    .error(R.drawable.not_loaded_image_background)
                    .placeholder(R.drawable.not_loaded_image_background)
                    .into(imageView)
            }else{
                imageView.setImageDrawable(
                    AppCompatResources.getDrawable(
                        context,
                        R.drawable.not_loaded_image_background))
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

    interface OnClickListener {
        fun onClick(itemData: CategoryUIState)
    }
}