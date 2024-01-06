package com.example.grocerystore.ui.activityMain.fragments.thirdTab.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocerystore.R
import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.databinding.DishBasketBinding

class CartUIStateAdapter(private val context: Context) : RecyclerView.Adapter<CartUIStateAdapter.ItemViewHolder>() {

    val TAG = "CartUIStateAdapter"

    private var data : List<CartUIState> = listOf()

    lateinit var onClickListener1: OnClickListener
    lateinit var onClickListener2: OnClickListener
    lateinit var onClickListener3: OnClickListener

        inner class ItemViewHolder(private var binding: DishBasketBinding) : RecyclerView.ViewHolder(binding.root) {


            fun bind(itemModel: CartUIState) {

                binding.textView2DishBasket.text = itemModel.itemData.name
                binding.textView3DishBasket.text = itemModel.itemData.price.toString()+"₽"
                binding.textView4DishBasket.text = itemModel.itemData.weight.toString()+"г"
                binding.textView5DishBasket.text = itemModel.quantity.toString()


                binding.view1DishBasket.setOnClickListener {
                    onClickListener1.onClick(itemModel)//minus button
                }

                binding.view2DishBasket.setOnClickListener {
                    onClickListener2.onClick(itemModel)//plus button
                }

                binding.textView6DishBasket.setOnClickListener {
                    onClickListener3.onClick(itemModel)//delete button
                }



                //setting image
                if (itemModel.itemData.image.isNotEmpty()) {
                    Glide.with(context)
                        .load(itemModel.itemData.image)
                        .error(R.drawable.not_loaded_one_image)
                        .placeholder(R.drawable.not_loaded_one_image)
                        .into(binding.imageDishBasket)
                }else{
                    binding.imageDishBasket.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.not_loaded_one_image))
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

        @SuppressLint("NotifyDataSetChanged")
        fun setData(newList : List<CartUIState>){
            Log.d(TAG, "newList $newList")
            data = newList
            notifyDataSetChanged()
        }


        interface OnClickListener {
            fun onClick(itemData: CartUIState)
        }
    }