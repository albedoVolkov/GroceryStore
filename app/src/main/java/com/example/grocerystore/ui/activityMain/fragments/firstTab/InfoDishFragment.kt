package com.example.grocerystore.ui.activityMain.fragments.firstTab

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.grocerystore.services.ConstantsSource
import com.example.grocerystore.R
import com.example.grocerystore.data.helpers.UIstates.item.DishUIState
import com.example.grocerystore.data.helpers.UIstates.item.fromStringToDishItem
import com.example.grocerystore.databinding.InfoDishFragmentBinding
import com.example.grocerystore.ui.activityMain.fragments.firstTab.viewModels.InfoDishFragmentViewModel


class InfoDishFragment : Fragment() {


    private val TAG = "InfoDishFragment"


    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return InfoDishFragment()
        }
    }


    private var binding: InfoDishFragmentBinding? = null
    private val viewModel: InfoDishFragmentViewModel by viewModels()

    private fun <T> views(block : InfoDishFragmentBinding.() -> T): T? = binding?.block()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View =
        InfoDishFragmentBinding.inflate(inflater, container, false).also{ binding = it }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = this.arguments
        if (bundle != null) {

            val dish = fromStringToDishItem(bundle.getString(ConstantsSource.MAIN_DISH_BUNDLE,null))

            if(dish != null) {
                viewModel.mainDish = dish
                Log.d(TAG, "onViewCreated : bundle = ${viewModel.mainDish}")
            }else{
                Log.d(TAG, "onViewCreated : bundle is incorrect")
                leaveFragment()
            }

        }else{
            Log.d(TAG, "onViewCreated : bundle = null")
            leaveFragment()
        }

        setViews(viewModel.mainDish!!)
    }




    private fun setViews(dish : DishUIState) {

        setListeners()

        views {
            if (dish.image != "") {
                Log.d(TAG, "dish.image : data = ${dish.image}")
                Glide.with(requireContext())
                    .load(dish.image)
                    .error(R.drawable.not_loaded_one_image)
                    .placeholder(R.drawable.not_loaded_one_image)
                    .into(imageViewInfoDishFragment)
            } else {
                Log.d(TAG, "dish.image : data = null")
            }//picture


            if (dish.name != "") {
                Log.d(TAG, "dish.name : data = ${dish.name}")
                textView2InfoDishFragment.text = dish.name
            } else {
                Log.d(TAG, "dish.name : data = null")
                textView2InfoDishFragment.text = "${R.string.null_string}"
            }//name


            if (dish.price >= 0) {
                Log.d(TAG, "dish.price : data = ${dish.price}")
                textView1InfoDishFragment.text = dish.price.toString() + " ₽"
            } else {
                Log.d(TAG, "dish.price : data <= 0")
                textView1InfoDishFragment.text = "${R.string.null_string}"
            }//price


            if (dish.weight >= 0) {
                Log.d(TAG, "dish.weight : data = ${dish.weight}")
                textView3InfoDishFragment.text = dish.weight.toString() + "г"
            } else {
                Log.d(TAG, "dish.weight : data <= 0")
                textView3InfoDishFragment.text = "${R.string.null_string}"
            }//weight


            if (dish.description != "") {
                Log.d(TAG, "dish.description : data = ${dish.description}")
                textView4InfoDishFragment.text = dish.description
            } else {
                Log.d(TAG, "dish.description : data = null")
                textView4InfoDishFragment.text = "${R.string.null_string}"
            }//description
        }

    }



    private fun setListeners() {
        views {
            cardView1InfoDishFragment.setOnClickListener {
                leaveFragment()
            }

            cardView2InfoDishFragment.setOnClickListener {
                Toast.makeText(context, "Like", Toast.LENGTH_SHORT).show()
            }

            btn1InfoDishFragment.setOnClickListener {
                viewModel.addMainDishInBasketOfUser()
                leaveFragment()
            }

            frameLayout2InfoDishFragment.setOnClickListener { }
        }
    }


    private fun leaveFragment(){
        val transaction = parentFragmentManager.beginTransaction()
        transaction.remove(this@InfoDishFragment)
        transaction.commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}