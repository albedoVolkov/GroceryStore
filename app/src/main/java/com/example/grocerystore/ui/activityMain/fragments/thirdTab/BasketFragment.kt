package com.example.grocerystore.ui.activityMain.fragments.thirdTab

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.grocerystore.GroceryStoreApplication
import com.example.grocerystore.R
import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.databinding.BasketFragmentBinding
import com.example.grocerystore.services.CheckNetworkConnection
import com.example.grocerystore.ui.activityMain.fragments.thirdTab.adapters.CartUIStateAdapter
import com.example.grocerystore.ui.activityMain.fragments.thirdTab.factories.BasketFragmentViewModelFactory
import com.example.grocerystore.ui.activityMain.fragments.thirdTab.viewModels.BasketFragmentViewModel


class BasketFragment  : Fragment() {

    companion object {
        const val TAG = "BasketFragment"

        @JvmStatic
        fun newInstance(): Fragment {
            return BasketFragment()
        }
    }


    private var _networkManager: CheckNetworkConnection? = null
    private val networkManager get() = _networkManager!!

    private var _binding: BasketFragmentBinding? = null
    private val binding get() = _binding!!

    private var _cartUIStateAdapter: CartUIStateAdapter? = null
    private val cartUIStateAdapter get() =  _cartUIStateAdapter!!

    private var _viewModel: BasketFragmentViewModel? = null
    private val viewModel get() = _viewModel!!
    private var _viewModelFactory: BasketFragmentViewModelFactory? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        _binding = BasketFragmentBinding.inflate(inflater, container, false)

        _viewModelFactory = BasketFragmentViewModelFactory(GroceryStoreApplication(requireContext()).userRepository, GroceryStoreApplication(requireContext()).sessionManager)
        _viewModel = ViewModelProvider(this, _viewModelFactory!!)[BasketFragmentViewModel::class.java]

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
        setObservers()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setViews() {

        _networkManager = GroceryStoreApplication(activity?.applicationContext!!).getNetworkManager(application = activity?.application!!)

        showLoading(true)

        if (context != null) {
            setCartAdapter(viewModel.showCarts.value)
            binding.recyclerViewBasketFragment.apply {
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                adapter = cartUIStateAdapter
            }
        }else{
            Log.d(TAG, "Context is null")
        }

        binding.toolBarBasketFragment.containerImageToolbarMain.setOnClickListener{
            // by click on this btn we go to the account tab(forth tab)
            Toast.makeText(context,"click by account button", Toast.LENGTH_LONG).show()
        }

    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setObservers() {


        viewModel.mainCarts.observe(viewLifecycleOwner) {
            viewModel.filterItems("None")//usual
        }


        viewModel.showCarts.observe(viewLifecycleOwner) { list -> //так как list = null то он не observe
            if (list.isNotEmpty() && list != null) {
                cartUIStateAdapter.setData(list)
                cartUIStateAdapter.notifyDataSetChanged()
                Log.d(TAG, "setObservers : Adapter : $list")

                showLoading(false)
            }else{
                showLoading(true)
            }
        }


        viewModel.filterType.observe(viewLifecycleOwner) {
            viewModel.filterItems("All")//usual
        }

        viewModel.userData.observe(viewLifecycleOwner) {
            if (it != null) {
                Glide.with(context)
                    .load(it.image)
                    .error(R.drawable.not_loaded_image_background)
                    .placeholder(R.drawable.not_loaded_image_background)
                    .into(binding.toolBarBasketFragment.imageViewToolbarMain)
            }else{
                Log.d(TAG, "userData is null")
            }
        }

        networkManager.observe(viewLifecycleOwner){ status ->
            if(status){
                showLoading(false)
            }else{
                showLoading(true)
            }
        }

    }


    private fun setCartAdapter(itemList: List<CartUIState>?) {
        _cartUIStateAdapter = CartUIStateAdapter(requireContext())
        cartUIStateAdapter.setData(itemList ?: emptyList())
        cartUIStateAdapter.onClickListener = object : CartUIStateAdapter.OnClickListener {

            override fun onClick(itemData: CartUIState) {
                TODO("Not yet implemented")
            }
        }
    }


    private fun openPayFragment(dishesList: List<CartUIState>?) {
        TODO()
//        val fragment = StoreFragment()
//        val bundle = Bundle()
//        bundle.putInt(ConstantsSource.MAIN_CATEGORY_ID_BUNDLE, itemData.id)
//        fragment.arguments = bundle
//
//        val transaction = childFragmentManager.beginTransaction()
//        transaction.setReorderingAllowed(true)
//        transaction.addToBackStack(null)
//        transaction.add(R.id.frame_layout_categories_fragment,fragment)
//        transaction.commit()
    }

    private fun showLoading(success : Boolean) {
        if (success) {
            binding.loaderLayout.loaderBackground.visibility = View.VISIBLE
            binding.recyclerViewBasketFragment.visibility = View.GONE
            binding.loaderLayout.circularLoader.showAnimationBehavior
        }else{
            binding.loaderLayout.loaderBackground.visibility = View.GONE
            binding.recyclerViewBasketFragment.visibility = View.VISIBLE
        }
    }


}