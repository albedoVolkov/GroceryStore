package com.example.grocerystore.ui.activityMain.fragments.thirdTab.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocerystore.domain.models.item.CartUIState
import com.example.grocerystore.databinding.DishBasketBinding
import java.lang.StringBuilder

class CartUIStateAdapter(private val context: Context) : RecyclerView.Adapter<CartUIStateAdapter.ItemViewHolder>() {

    companion object {
        const val TAG = "CartUIStateAdapter"
    }

    private var data : List<CartUIState> = listOf()

    lateinit var onClickListener1: OnClickListener
    lateinit var onClickListener2: OnClickListener
    lateinit var onClickListener3: OnClickListener

        inner class ItemViewHolder(private var binding: DishBasketBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bind(itemModel: CartUIState) {
                binding.textView2DishBasket.text = itemModel.itemData.name
                binding.textView3DishBasket.text = StringBuilder().append(itemModel.itemData.price.toString()," ₽")
                binding.textView4DishBasket.text = StringBuilder().append(itemModel.itemData.weight.toString(),"г")
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
                Glide.with(context)
                    .load(itemModel.itemData.image)
                    .error(null)
                    .placeholder(null)
                    .into(binding.imageDishBasket)
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

        fun getId(position: Int) : String = data[position].cartId

        interface OnClickListener {
            fun onClick(itemData: CartUIState)
        }
    }