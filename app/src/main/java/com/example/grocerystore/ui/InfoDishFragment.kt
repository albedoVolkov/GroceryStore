package com.example.grocerystore.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.grocerystore.GroceryStoreApplication
import com.example.grocerystore.R
import com.example.grocerystore.data.helpers.CategoryUIState
import com.example.grocerystore.data.helpers.DishUIState
import com.example.grocerystore.databinding.InfoDishFragmentBinding
import com.example.grocerystore.ui.utils.ConstantsSourceUI
import com.example.grocerystore.ui.viewModels.CategoriesFragmentViewModel
import com.example.grocerystore.ui.viewModels.InfoDishFragmentViewModel
import com.example.grocerystore.ui.viewModels.StoreFragmentViewModel
import com.google.gson.Gson


class InfoDishFragment : Fragment() {

    private val TAG = "InfoDishFragment"

    private var _binding: InfoDishFragmentBinding? = null
    private val binding get() = _binding!!

    private var _viewModel: InfoDishFragmentViewModel? = null
    private val viewModel get() = _viewModel!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View {
        _binding = InfoDishFragmentBinding.inflate(inflater, container, false)
        _viewModel = InfoDishFragmentViewModel(GroceryStoreApplication(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val bundle = this.arguments
        if (bundle != null) {
            Log.d(TAG, "onViewCreated : bundle != null")
            val mainDishId = bundle.getInt(ConstantsSourceUI().MAIN_DISH_ID_BUNDLE)
            Log.d(TAG, "onViewCreated : bundle = $mainDishId")
            viewModel.setMainDishId(mainDishId)
            viewModel.refreshInfo()
        }else{
            Log.d(TAG, "onViewCreated : bundle = null")
        }


        setViews(DishUIState())
        setObservers()
    }

    private fun setViews(dish : DishUIState) {

        if (dish.image != "") {
            Log.d(TAG, "dish.image : data = ${dish.image}")
            Glide.with(context)
                .load(viewModel.mainDish.value?.image)
                .error(R.drawable.not_loaded_image_background)
                .placeholder(R.drawable.not_loaded_image_background)
                .into(binding.imageViewInfoDishFragment)
        } else {
            Log.d(TAG, "dish.image : data = null")
        }//picture


        if (dish.name != "") {
            Log.d(TAG, "dish.name : data = ${dish.name}")
            binding.textView2InfoDishFragment.text = dish.name
        } else {
            Log.d(TAG, "dish.name : data = null")
            binding.textView2InfoDishFragment.text = "null"
        }//name


        if (dish.price >= 0) {
            Log.d(TAG, "dish.price : data = ${dish.price}")
            binding.textView1InfoDishFragment.text = dish.price.toString() + " ₽"
        } else {
            Log.d(TAG, "dish.price : data <= 0")
            binding.textView1InfoDishFragment.text = "null"
        }//price


        if (dish.weight >= 0) {
            Log.d(TAG, "dish.weight : data = ${dish.weight}")
            binding.textView3InfoDishFragment.text = dish.weight.toString()+ "г"
        } else {
            Log.d(TAG, "dish.weight : data <= 0")
            binding.textView3InfoDishFragment.text = "null"
        }//weight


        if (dish.description != "") {
            Log.d(TAG, "dish.description : data = ${dish.description}")
            binding.textView4InfoDishFragment.text = dish.description
        } else {
            Log.d(TAG, "dish.description : data = null")
            binding.textView4InfoDishFragment.text = "null"
        }//description

    }

    private fun setObservers() {

        binding.cardView1InfoDishFragment.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.remove(this@InfoDishFragment)
            transaction.commit()
        }

        binding.cardView2InfoDishFragment.setOnClickListener {
            Toast.makeText(context,"Like",Toast.LENGTH_SHORT).show()
        }

        binding.buttonInfoDishFragment.setOnClickListener {
            Toast.makeText(context,"click",Toast.LENGTH_SHORT).show()
        }

        binding.frameLayout2InfoDishFragment.setOnClickListener {  }

        viewModel.mainDish.observe(viewLifecycleOwner) { dish ->
            if (dish != null) {

                setViews(dish)

//                binding.loaderLayout.circularLoader.hideAnimationBehavior
//                binding.loaderLayout.loaderBackground.visibility = View.GONE
//                binding.containerDishesFragment.visibility = View.VISIBLE
            } else {
//                binding.loaderLayout.loaderBackground.visibility = View.VISIBLE
//                binding.loaderLayout.circularLoader.showAnimationBehavior
//                binding.containerDishesFragment.visibility = View.GONE
            }
        }
    }


}