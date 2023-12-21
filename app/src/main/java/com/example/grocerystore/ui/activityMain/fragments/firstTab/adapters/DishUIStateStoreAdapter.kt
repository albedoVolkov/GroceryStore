package com.example.grocerystore.ui.activityMain.fragments.firstTab.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocerystore.R
import com.example.grocerystore.data.helpers.UIstates.item.DishUIState
import com.example.grocerystore.databinding.DishStoreItemListBinding

class DishUIStateStoreAdapter( private val context: Context) : RecyclerView.Adapter<DishUIStateStoreAdapter.ItemViewHolder>() {

    val TAG = "DishUIStateStoreAdapter"

    private var data = listOf<DishUIState>()

    lateinit var onClickListener: OnClickListener

    inner class ItemViewHolder(binding: DishStoreItemListBinding)  : RecyclerView.ViewHolder(binding.root) {
        private val titleTextView: TextView = binding.textView1DishStore
        private val imageView: ImageView = binding.image1DishStore
        private val itemCard: CardView = binding.containerImageDishStore

        fun bind(itemModel: DishUIState) {
            itemCard.setOnClickListener {
                onClickListener.onClick(itemModel)
            }
            titleTextView.text = itemModel.name

            if (itemModel.image.isNotEmpty()) {
                Glide.with(context)
                    .load(itemModel.image)
                    .error(R.drawable.not_loaded_one_image)
                    .placeholder(R.drawable.not_loaded_one_image)
                    .into(imageView)
                //imageView.clipToOutline = true
            }else{
                imageView.setImageDrawable(
                    AppCompatResources.getDrawable(
                        context,
                        R.drawable.not_loaded_one_image))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        return ItemViewHolder(DishStoreItemListBinding.inflate(itemView, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemId(position: Int) = data[position].id.toLong()

    override fun getItemCount() = data.size

    fun setData(newList : List<DishUIState>){
        Log.d(TAG, "newList ${newList.toString()}")
        data = newList
        notifyDataSetChanged()
    }

    interface OnClickListener {
        fun onClick(itemData: DishUIState)
    }
}