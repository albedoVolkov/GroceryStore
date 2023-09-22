package com.example.grocerystore.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerystore.GroceryStoreApplication
import com.example.grocerystore.R
import com.example.grocerystore.data.helpers.CategoryUIState
import com.example.grocerystore.databinding.CategoriesFragmentBinding
import com.example.grocerystore.ui.adapters.CategoryUIStateAdapter
import com.example.grocerystore.ui.utils.ConstantsSourceUI
import com.example.grocerystore.ui.viewModels.CategoriesFragmentViewModel
import com.google.gson.Gson

class CategoriesFragment : Fragment() {
    private val TAG = "CategoriesFragment"
    private var _binding: CategoriesFragmentBinding? = null
    private val binding get() = _binding!!

    private var _categoryUIStateAdapter: CategoryUIStateAdapter? = null
    private val categoryUIStateAdapter get() =  _categoryUIStateAdapter!!

    private var _viewModel: CategoriesFragmentViewModel? = null
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = CategoriesFragmentBinding.inflate(inflater, container, false)
        _viewModel = CategoriesFragmentViewModel(GroceryStoreApplication(requireContext()))
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
        bundle.putString(ConstantsSourceUI().MAIN_CATEGORY_BUNDLE, Gson().toJson(itemData))
        fragment.arguments = bundle

        val transaction = childFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.add(R.id.frame_layout_categories_fragment,fragment)
        transaction.addToBackStack("categories fragment")
        transaction.commit()
    }


    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return CategoriesFragment()
        }
    }

}