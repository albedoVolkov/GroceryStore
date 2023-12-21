package com.example.grocerystore.ui.activityMain.fragments.firstTab


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.grocerystore.services.ConstantsSource
import com.example.grocerystore.R
import com.example.grocerystore.data.helpers.UIstates.item.CategoryUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.databinding.CategoriesFragmentBinding
import com.example.grocerystore.ui.activityMain.fragments.firstTab.adapters.CategoryUIStateAdapter
import com.example.grocerystore.ui.activityMain.fragments.firstTab.viewModels.CategoriesFragmentViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class CategoriesFragment : Fragment() {


    private val TAG = "CategoriesFragment"

    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return CategoriesFragment()
        }
    }

    private var binding: CategoriesFragmentBinding? = null
    private val viewModel: CategoriesFragmentViewModel by viewModels()
    private lateinit var categoriesAdapter: CategoryUIStateAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View =
        CategoriesFragmentBinding.inflate(inflater, container, false).also{ binding = it }.root

    private fun <T> views(block : CategoriesFragmentBinding.() -> T): T? = binding?.block()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
        setCategoriesAdapter()

        viewModel.mainCategories.onEach{
            viewModel.filterItems("All")
            setCategoriesList(viewModel.showCategories)
        }.launchIn(viewModel.viewModelScope)

        viewModel.userData.onEach(::setUserData).launchIn(viewModel.viewModelScope)// this func just calls func collect in other scope
    }


    private fun setCategoriesList(list: List<CategoryUIState>) {
        Log.d(TAG, "setCategories : Adapter : $list")
        views {
            if (list.isNotEmpty()) {
                showLoading(false)
                categoriesAdapter.setData(list)
            } else { showLoading(true) }

        }
    }


    private fun setViews() {
        showLoading(true)
        views {
            toolBarCategoriesFragment.containerImageToolbarMain.setOnClickListener {
                Toast.makeText(context, "click by account button", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun setCategoriesAdapter() {
        views {
            categoriesAdapter = CategoryUIStateAdapter(requireContext())
            recyclerViewCategoriesFragment.layoutManager = GridLayoutManager(requireContext(), resources.getInteger(R.integer.columns_categories), LinearLayoutManager.VERTICAL, false)
            recyclerViewCategoriesFragment.adapter = categoriesAdapter

            categoriesAdapter.onClickListener = object : CategoryUIStateAdapter.OnClickListener {

                override fun onClick(itemData: CategoryUIState) {
                    openDishesFragment(itemData)
                }

            }

        }
    }


    private fun setUserData(user: UserUIState?) {
        views {
            if (user != null) {
                Log.d(TAG, " setUserData : SUCCESS : listData is $user")

                Glide.with(context)
                    .load(user.image)
                    .error(R.drawable.not_loaded_one_image)
                    .placeholder(R.drawable.not_loaded_one_image)
                    .into(toolBarCategoriesFragment.imageViewToolbarMain)

            } else {
                Log.d(TAG, "setUserData : ERROR : listData is null")
            }
        }
    }


    private fun openDishesFragment(itemData: CategoryUIState) {
        val fragment = StoreFragment()
        val bundle = Bundle()
        bundle.putString(ConstantsSource.MAIN_CATEGORY_BUNDLE, itemData.toString())
        fragment.arguments = bundle

        val transaction = childFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.addToBackStack(null)
        transaction.add(R.id.frame_layout_categories_fragment,fragment)
        transaction.commit()
    }

    private fun showLoading(success : Boolean) {
        views {
            if (success) {
                loaderLayout.loaderBackground.visibility = View.VISIBLE
                recyclerViewCategoriesFragment.visibility = View.GONE
                loaderLayout.circularLoader.showAnimationBehavior
            } else {
                loaderLayout.loaderBackground.visibility = View.GONE
                recyclerViewCategoriesFragment.visibility = View.VISIBLE
            }
        }
    }

}