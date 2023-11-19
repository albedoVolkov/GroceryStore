package com.example.grocerystore.ui.activityMain.fragments.firstTab

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.grocerystore.services.ConstantsSource
import com.example.grocerystore.GroceryStoreApplication
import com.example.grocerystore.R
import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.data.helpers.UIstates.item.DishUIState
import com.example.grocerystore.data.helpers.UIstates.item.fromStringToAddressItem
import com.example.grocerystore.data.helpers.UIstates.item.fromStringToDishItem
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.data.helpers.Utils
import com.example.grocerystore.databinding.InfoDishFragmentBinding
import com.example.grocerystore.services.CheckNetworkConnection
import com.example.grocerystore.ui.activityMain.fragments.firstTab.factories.InfoDishFragmentViewModelFactory
import com.example.grocerystore.ui.activityMain.fragments.firstTab.viewModels.InfoDishFragmentViewModel
import kotlinx.coroutines.launch


class InfoDishFragment : Fragment() {

    companion object {
       const val TAG = "InfoDishFragment"
    }

    private var _binding: InfoDishFragmentBinding? = null
    private val binding get() = _binding!!

    private var _viewModel: InfoDishFragmentViewModel? = null
    private val viewModel get() = _viewModel!!

    private var _viewModelFactory: InfoDishFragmentViewModelFactory? = null

    private var _networkManager: CheckNetworkConnection? = null
    private val networkManager get() = _networkManager!!



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        _binding = InfoDishFragmentBinding.inflate(inflater, container, false)

        _viewModelFactory = InfoDishFragmentViewModelFactory(GroceryStoreApplication(requireContext()).userRepository,
            GroceryStoreApplication(activity?.applicationContext!!).creatingIdsService)
        _viewModel = ViewModelProvider(this, _viewModelFactory!!)[InfoDishFragmentViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var mainDish : DishUIState? = null

        val bundle = this.arguments
        if (bundle != null) {
            Log.d(TAG, "onViewCreated : bundle != null")
            val stringDish = bundle.getString(ConstantsSource.MAIN_DISH_BUNDLE,null)
            mainDish = fromStringToDishItem(stringDish)
            Log.d(TAG, "onViewCreated : bundle = $mainDish")
        }else{
            Log.d(TAG, "onViewCreated : bundle = null")
        }


        setMainDish(mainDish)
    }


    private fun setMainDish(mainDish : DishUIState?){
        if(mainDish !=  null) {
            setViews(mainDish)
            setObservers()
            setListeners(mainDish)
        }else{
            ///Toast.makeText(activity?.applicationContext,getString(R.string.not_open_this_dish),Toast.LENGTH_SHORT).show()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.remove(this@InfoDishFragment)
            transaction.commit()
        }


    }

    private fun setListeners(dish : DishUIState) {
        binding.cardView1InfoDishFragment.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.remove(this@InfoDishFragment)
            transaction.commit()
        }

        binding.cardView2InfoDishFragment.setOnClickListener {
            Toast.makeText(context, "Like", Toast.LENGTH_SHORT).show()
        }


        binding.btn1InfoDishFragment.setOnClickListener {
            viewModel.addProductInBasketOfUser(dish)
        }


        binding.frameLayout2InfoDishFragment.setOnClickListener { }
    }


    private fun setViews(dish : DishUIState) {


        _networkManager =  GroceryStoreApplication(activity?.applicationContext!!).getNetworkManager(application = activity?.application!!)

        showNoInternetConnection(false)
        if (dish.image != "") {
            Log.d(TAG, "dish.image : data = ${dish.image}")
            Glide.with(context)
                .load(dish.image)
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
            binding.textView2InfoDishFragment.text = "${R.string.null_string}"
        }//name


        if (dish.price >= 0) {
            Log.d(TAG, "dish.price : data = ${dish.price}")
            binding.textView1InfoDishFragment.text = dish.price.toString() + " ₽"
        } else {
            Log.d(TAG, "dish.price : data <= 0")
            binding.textView1InfoDishFragment.text = "${R.string.null_string}"
        }//price


        if (dish.weight >= 0) {
            Log.d(TAG, "dish.weight : data = ${dish.weight}")
            binding.textView3InfoDishFragment.text = dish.weight.toString()+ "г"
        } else {
            Log.d(TAG, "dish.weight : data <= 0")
            binding.textView3InfoDishFragment.text = "${R.string.null_string}"
        }//weight


        if (dish.description != "") {
            Log.d(TAG, "dish.description : data = ${dish.description}")
            binding.textView4InfoDishFragment.text = dish.description
        } else {
            Log.d(TAG, "dish.description : data = null")
            binding.textView4InfoDishFragment.text = "${R.string.null_string}"
        }//description

    }

    private fun setObservers() {

        networkManager.observe(viewLifecycleOwner){ status ->
            if (status) {
                showNoInternetConnection(false)
            }else{
                showNoInternetConnection(true)
            }
        }

    }

    private fun showNoInternetConnection(status: Boolean){
        if (!status) {
            binding.cardView3InfoDishFragment.visibility = View.GONE
            binding.btn1InfoDishFragment.isEnabled = true
        }else{
            binding.cardView3InfoDishFragment.visibility = View.VISIBLE
            binding.btn1InfoDishFragment.isEnabled = false
        }

    }



}