package com.example.grocerystore.ui.activityMain.fragments.firstTab

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.grocerystore.CheckNetworkConnection
import com.example.grocerystore.ConstantsSource
import com.example.grocerystore.GroceryStoreApplication
import com.example.grocerystore.R
import com.example.grocerystore.ShoppingAppSessionManager
import com.example.grocerystore.data.helpers.UIstates.item.CategoryUIState
import com.example.grocerystore.data.helpers.UIstates.item.DishUIState
import com.example.grocerystore.data.helpers.UIstates.item.TitleUIState
import com.example.grocerystore.databinding.StoreFragmentBinding
import com.example.grocerystore.ui.activityMain.fragments.firstTab.adapters.DishUIStateStoreAdapter
import com.example.grocerystore.ui.activityMain.fragments.firstTab.adapters.TitleUIStateAdapter
import com.example.grocerystore.ui.activityMain.fragments.firstTab.factories.StoreFragmentViewModelFactory
import com.example.grocerystore.ui.activityMain.fragments.firstTab.viewModels.StoreFragmentViewModel


class StoreFragment : Fragment() {

    companion object {
        const val TAG = "StoreFragment"
    }



    private var _networkManager: CheckNetworkConnection? = null
    private val networkManager get() = _networkManager!!

    private var _binding: StoreFragmentBinding? = null
    private val binding get() = _binding!!

    private var _dishUIStateAdapter: DishUIStateStoreAdapter? = null
    private val dishUIStateAdapter get() =  _dishUIStateAdapter!!
    private var _titleUIStateAdapter: TitleUIStateAdapter? = null
    private val titleUIStateAdapter get() =  _titleUIStateAdapter!!

    private var _viewModel: StoreFragmentViewModel? = null
    private val viewModel get() = _viewModel!!
    private var _viewModelFactory: StoreFragmentViewModelFactory? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = StoreFragmentBinding.inflate(inflater, container, false)

        _viewModelFactory = StoreFragmentViewModelFactory(
            GroceryStoreApplication(requireContext()).dishesRepository,
            GroceryStoreApplication(requireContext()).categoryRepository,
            ShoppingAppSessionManager(requireContext()))

        _viewModel = ViewModelProvider(this, _viewModelFactory!!)[StoreFragmentViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(viewModel.mainCategoryId == -1) {
            val bundle = this.arguments
            if (bundle != null) {
                val mainCategoryId = bundle.getInt(ConstantsSource.MAIN_CATEGORY_ID_BUNDLE)
                Log.d(TAG,"mainCategoryId : data = $mainCategoryId")
                viewModel.setCategoryId(mainCategoryId)
                viewModel.refreshCategory()
                viewModel.refreshDishes()
            }
        }




        setViews(CategoryUIState())
        setObservers()
        setAdapters()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setAdapters() {

        if (context != null) {
            setDishesAdapter(viewModel.showDishes.value)
            setTitlesAdapter(viewModel.showTitles.value)

            binding.recyclerView2StoreFragment.apply {
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                adapter = titleUIStateAdapter
            }

            binding.recyclerView1StoreFragment.apply {
                val columns = resources.getInteger(R.integer.columns_dish_store)
                layoutManager = GridLayoutManager(activity,columns, LinearLayoutManager.VERTICAL,false)
                adapter = dishUIStateAdapter
            }

        }else{Log.d(TAG,"Context is null") }
    }



    private fun setViews(category : CategoryUIState) {


        _networkManager = CheckNetworkConnection(activity?.application!!)

        showLoading(true)

        if (context != null) {

            if(category.name != ""){
                Log.d(TAG,"viewModel.mainCategory.value?.name : data = ${category.name}")
                binding.toolBarStoreFragment.textView2ToolbarCategory.text = category.name
            }else{
                Log.d(TAG,"viewModel.mainCategory.value?.name : data = null")
                binding.toolBarStoreFragment.textView2ToolbarCategory.text = "${R.string.null_string}"
            }


            binding.toolBarStoreFragment.viewToolbarCategory.setOnClickListener{
                val transaction = parentFragmentManager.beginTransaction()
                transaction.remove(this@StoreFragment)
                transaction.commit()
            }


            binding.toolBarStoreFragment.containerImageToolbarCategory.setOnClickListener{
                // by click on this btn we go to the account tab(forth tab)
                Toast.makeText(context,"click bu account button",Toast.LENGTH_LONG).show()
            }


        }else{Log.d(TAG,"Context is null") }
    }





    @SuppressLint("NotifyDataSetChanged")
    private fun setObservers() {


        viewModel.mainCategory.observe(viewLifecycleOwner) { category ->
            if (category != null) {

                setViews(category)
                Log.d(TAG, "setViews : $category")

                showLoading(true)
            } else {
                showLoading(false)
            }
        }



        viewModel.showDishes.observe(viewLifecycleOwner) { dishesList ->
            if (dishesList.isNotEmpty()) {

                dishUIStateAdapter.data = dishesList
                dishUIStateAdapter.notifyDataSetChanged()
                Log.d(TAG, "DishesAdapter : $dishesList")

                showLoading(false)
            } else {
                showLoading(true)
            }
        }



        viewModel.filterType.observe(viewLifecycleOwner) {
                viewModel.filterDishes("All")
        }


        viewModel.userData.observe(viewLifecycleOwner) {
            if (it != null) {
                Glide.with(context)
                    .load(it.displayImage)
                    .error(R.drawable.not_loaded_image_background)
                    .placeholder(R.drawable.not_loaded_image_background)
                    .into(binding.toolBarStoreFragment.imageToolbarCategory)
            }else{
                Log.d(TAG, "userData is null")
            }
        }

        viewModel.showTitles.observe(viewLifecycleOwner) { titlesList ->
            if (titlesList.isNotEmpty()) {

                titleUIStateAdapter.data = titlesList
                titleUIStateAdapter.notifyDataSetChanged()
                Log.d(TAG, "TitlesAdapter : $titlesList")

                showLoading(false)
            } else {
                showLoading(true)
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

    private fun setDishesAdapter(itemList: List<DishUIState>?) {
            _dishUIStateAdapter = DishUIStateStoreAdapter(itemList ?: emptyList(), requireContext())
            dishUIStateAdapter.onClickListener = object : DishUIStateStoreAdapter.OnClickListener {

                override fun onClick(itemData: DishUIState) {
                    val fragment = InfoDishFragment()
                    val bundle = Bundle()
                    bundle.putInt(ConstantsSource.MAIN_DISH_ID_BUNDLE, itemData.id)
                    fragment.arguments = bundle

                    val transaction =  parentFragmentManager.beginTransaction()
                    transaction.setReorderingAllowed(true)
                    transaction.addToBackStack(null)
                    transaction.add(R.id.frame_layout_categories_fragment,fragment)
                    transaction.commit()

                }

            }
        }

    private fun setTitlesAdapter(itemList: List<TitleUIState>?) {
        _titleUIStateAdapter = TitleUIStateAdapter(itemList ?: emptyList(), requireContext())
        titleUIStateAdapter.onClickListener = object : TitleUIStateAdapter.OnClickListener{

                override fun onClick(itemData: TitleUIState) {
                    Toast.makeText(context,itemData.name,Toast.LENGTH_LONG).show()
                }

        }
    }

    private fun showLoading(success : Boolean) {
        if (success) {
            binding.loaderLayout.loaderBackground.visibility = View.VISIBLE
            binding.containerStoreFragment.visibility = View.GONE
            binding.loaderLayout.circularLoader.showAnimationBehavior
        }else{
            binding.loaderLayout.loaderBackground.visibility = View.GONE
            binding.containerStoreFragment.visibility = View.VISIBLE
        }
    }




}