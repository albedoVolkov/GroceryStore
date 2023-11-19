package com.example.grocerystore.ui.activityMain.fragments.thirdTab.adapters

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
import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.data.helpers.UIstates.item.CategoryUIState
import com.example.grocerystore.databinding.BasketFragmentBinding
import com.example.grocerystore.databinding.CategoryListItemBinding
import com.example.grocerystore.databinding.DishBasketBinding

class CartUIStateAdapter(private val context: Context) : RecyclerView.Adapter<CartUIStateAdapter.ItemViewHolder>() {

            companion object {
                const val TAG = "CartUIStateAdapter"
            }

        private var data : List<CartUIState> = listOf()

        lateinit var onClickListener: OnClickListener

        inner class ItemViewHolder(binding: DishBasketBinding) : RecyclerView.ViewHolder(binding.root) {

            private val imageDish: ImageView = binding.imageDishBasket
            private val nameDish: TextView = binding.textView2DishBasket
            private val priceDish: TextView = binding.textView3DishBasket
            private val weightDish: TextView = binding.textView4DishBasket
            private val quantityItems: TextView = binding.textView5DishBasket
            private val addOneToQuantityItems: ImageView = binding.view2DishBasket
            private val deleteOneFromQuantityItems: ImageView = binding.view1DishBasket


            fun bind(itemModel: CartUIState) {

                //setting variables
                nameDish.text = itemModel.itemData.name
                priceDish.text = itemModel.itemData.name
                weightDish.text = itemModel.itemData.name
                quantityItems.text = itemModel.itemData.name


                //setting buttons
                addOneToQuantityItems.setOnClickListener {
                    onClickListener.onClick(itemModel)
                }

                deleteOneFromQuantityItems.setOnClickListener {
                    onClickListener.onClick(itemModel)
                }


                //setting image
                if (itemModel.itemData.image.isNotEmpty()) {
                    Glide.with(context)
                        .load(itemModel.itemData.image)
                        .error(R.drawable.not_loaded_image_background)
                        .placeholder(R.drawable.not_loaded_image_background)
                        .into(imageDish)
                }else{
                    imageDish.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.not_loaded_image_background))
                }


            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val itemView = LayoutInflater.from(parent.context)
            return ItemViewHolder(DishBasketBinding.inflate(itemView, parent, false))
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.bind(data[position])

        override fun getItemId(position: Int) = data[position].cartId.toLong()

        override fun getItemCount() = data.size

        fun setData(newList : List<CartUIState>){
            Log.d(TAG, "newList ${newList.toString()}")
            data = newList
        }


        interface OnClickListener {
            fun onClick(itemData: CartUIState)
        }
    }