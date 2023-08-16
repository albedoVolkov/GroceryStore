package com.example.grocerystore.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.grocerystore.databinding.InfoDishFragmentBinding
import com.example.grocerystore.ui.viewModels.CategoriesFragmentViewModel

private const val TAG = "InfoDishFragment"

class InfoDishFragment : Fragment() {

    private var _binding: InfoDishFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CategoriesFragmentViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = InfoDishFragmentBinding.inflate(inflater, container, false)
        //viewModel = ViewModelProvider(this)[CategoriesFragmentViewModel::class.java]
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
        setObservers()
    }

    private fun setViews() {
        if (context != null) {
            TODO()
        }else{
            Log.d(TAG,"Context is null") }
    }

    private fun setObservers() {
//        viewModel.storeDataStatus.observe(viewLifecycleOwner) { status ->
//            when (status) {
//                Utils.StoreDataStatus.LOADING -> {
//                    binding.loaderLayout.loaderBackground.visibility = View.VISIBLE
//                    binding.loaderLayout.circularLoader.showAnimationBehavior
//                    binding.containerCategoriesFragment.visibility = View.GONE
//                }
//                else -> {
//                    binding.loaderLayout.circularLoader.hideAnimationBehavior
//                    binding.loaderLayout.loaderBackground.visibility = View.GONE
//                    binding.containerCategoriesFragment.visibility = View.VISIBLE
//                }
//            }
            TODO()
//            if (status != null && status != Utils.StoreDataStatus.LOADING) {
//                viewModel.categories.observe(viewLifecycleOwner) { categoryList ->
//                    if (categoryList.isNotEmpty()) {
//                        categoryUIStateAdapter.apply {
//                            categoryUIStateAdapter.data = categoryList
//                            notifyDataSetChanged()
//                            Log.d("package:mine","categoriesAdapter : $categoryList")
//                        }
//                    }
//                }
//            }
        }
//        viewModel.categories.observe(viewLifecycleOwner) {
//            if (it.isNotEmpty()) {
//               // viewModel.filterCategories("All")
//            }
//        }
//    }
}