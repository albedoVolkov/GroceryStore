package com.example.grocerystore.activityMain.ui.firstTab

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
import com.example.grocerystore.ConstantsSource
import com.example.grocerystore.GroceryStoreApplication
import com.example.grocerystore.R
import com.example.grocerystore.data.helpers.UIstates.item.CategoryUIState
import com.example.grocerystore.databinding.CategoriesFragmentBinding
import com.example.grocerystore.activityMain.ui.firstTab.adapters.CategoryUIStateAdapter
import com.example.grocerystore.activityMain.ui.firstTab.factories.CategoriesFragmentViewModelFactory
import com.example.grocerystore.activityMain.ui.firstTab.viewModels.CategoriesFragmentViewModel

class CategoriesFragment : Fragment() {

    private val TAG = "CategoriesFragment"


    private var _binding: CategoriesFragmentBinding? = null
    private val binding get() = _binding!!

    private var _categoryUIStateAdapter: CategoryUIStateAdapter? = null
    private val categoryUIStateAdapter get() =  _categoryUIStateAdapter!!

    private var _viewModel: CategoriesFragmentViewModel? = null
    private val viewModel get() = _viewModel!!
    private var _viewModelFactory: CategoriesFragmentViewModelFactory? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        _binding = CategoriesFragmentBinding.inflate(inflater, container, false)

        _viewModelFactory = CategoriesFragmentViewModelFactory(GroceryStoreApplication(requireContext()).categoryRepository, GroceryStoreApplication(requireContext()).sessionManager)
        _viewModel = ViewModelProvider(this, _viewModelFactory!!)[CategoriesFragmentViewModel::class.java]

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
        if (context != null) {
            setCategoryAdapter(viewModel.showCategories.value)
            binding.recyclerViewCategoriesFragment.apply {
                val columns = resources.getInteger(R.integer.columns_categories)
                val gridLayoutManager =
                    GridLayoutManager(activity, columns, LinearLayoutManager.VERTICAL, false)
                layoutManager = gridLayoutManager
                adapter = categoryUIStateAdapter
            }
        }else{
            Log.d(TAG, "Context is null")
        }

        binding.toolBarCategoriesFragment.containerImageToolbarMain.setOnClickListener{
            // by click on this btn we go to the account tab(forth tab)
            Toast.makeText(context,"click bu account button",Toast.LENGTH_LONG).show()
        }

    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setObservers() {
        viewModel.showCategories.observe(viewLifecycleOwner) { categoryList -> //так как categoryList = null то он не observe
            if (categoryList.isNotEmpty() || categoryList != null) {
                categoryUIStateAdapter.data = categoryList
                categoryUIStateAdapter.notifyDataSetChanged()
                Log.d(TAG, "CategoriesFragment : Adapter : $categoryList")

                binding.loaderLayout.circularLoader.hideAnimationBehavior
                binding.loaderLayout.loaderBackground.visibility = View.GONE
                binding.containerCategoriesFragment.visibility = View.VISIBLE
            }else{
                binding.loaderLayout.loaderBackground.visibility = View.VISIBLE
                binding.loaderLayout.circularLoader.showAnimationBehavior
                binding.containerCategoriesFragment.visibility = View.GONE
            }
        }

        viewModel.filterType.observe(viewLifecycleOwner) {
            viewModel.filterCategories("All")//usual
        }

        viewModel.userData.observe(viewLifecycleOwner) {
            if (it != null) {
                Glide.with(context)
                    .load(it.displayImage)
                    .error(R.drawable.not_loaded_image_background)
                    .placeholder(R.drawable.not_loaded_image_background)
                    .into(binding.toolBarCategoriesFragment.imageViewToolbarMain)
            }else{
                Log.d(TAG, "userData is null")
            }
        }
    }


    private fun setCategoryAdapter(itemList: List<CategoryUIState>?) {
        _categoryUIStateAdapter = CategoryUIStateAdapter(itemList ?: emptyList(), requireContext())
        categoryUIStateAdapter.onClickListener = object : CategoryUIStateAdapter.OnClickListener {

            override fun onClick(itemData: CategoryUIState) {
                openDishesFragment(itemData)
            }
        }
    }


    private fun openDishesFragment(itemData: CategoryUIState) {
        val fragment = StoreFragment()
        val bundle = Bundle()
        bundle.putInt(ConstantsSource().MAIN_CATEGORY_ID_BUNDLE, itemData.id)
        fragment.arguments = bundle

        val transaction = childFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.addToBackStack(null)
        transaction.add(R.id.frame_layout_categories_fragment,fragment)
        transaction.commit()
    }


    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return CategoriesFragment()
        }
    }

}