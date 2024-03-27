package com.example.grocerystore.ui.activityMain.fragments.firstTab


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.grocerystore.R
import com.example.grocerystore.domain.models.item.CategoryUIState
import com.example.grocerystore.domain.models.user.UserUIState
import com.example.grocerystore.databinding.CategoriesFragmentBinding
import com.example.grocerystore.ui.activityMain.MainActivity
import com.example.grocerystore.ui.activityMain.fragments.firstTab.adapters.CategoryUIStateAdapter
import com.example.grocerystore.ui.activityMain.fragments.firstTab.viewModels.CategoriesFragmentViewModel
import com.example.grocerystore.ui.utils.ConstantsUI
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.StringBuilder
import java.util.Calendar


class CategoriesFragment : Fragment() {


    companion object {
        const val TAG = "CategoriesFragment"

        @JvmStatic
        fun newInstance(): Fragment {
            return CategoriesFragment()
        }
    }



    private var binding: CategoriesFragmentBinding? = null
    private var categoriesAdapter: CategoryUIStateAdapter? = null
    private val viewModel: CategoriesFragmentViewModel by viewModels()



    private fun <T> views(block : CategoriesFragmentBinding.() -> T): T? = binding?.block()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View =
        CategoriesFragmentBinding.inflate(inflater, container, false).also{ binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCategoriesAdapter()

        viewModel.refreshData()

        viewModel.mainCategories.onEach{
            Log.d(TAG, "mainCategories : $it")
            viewModel.setListCategoriesInViewModel(it)
            viewModel.filterItems("All")
            setCategoriesListInRecyclerView(viewModel.showCategories)
        }.launchIn(viewModel.viewModelScope)

       viewModel.userData.onEach(::setUserData).launchIn(viewModel.viewModelScope)// this func just calls func collect in other scope

        showLoading(true)
        setToolbar()

    }



    private fun setToolbar() {
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val mountInt = c.get(Calendar.MONTH)
        val mount = when(mountInt){
            0 -> "Января"
            1 -> "Февраля"
            2 -> "Марта"
            3 -> "Апреля"
            4 -> "Мая"
            5 -> "Июня"
            6 -> "Июля"
            7 -> "Августа"
            8 -> "Сентября"
            9 -> "Октября"
            10 -> "Ноября"
            11 -> "Декабря"
            else -> "Not defined"
        }
        val day = c.get(Calendar.DAY_OF_MONTH)

        views {
            toolBarCategoriesFragment.textView2ToolbarMain.text = StringBuilder().append(day," ", mount," ", year)

            toolBarCategoriesFragment.containerImageToolbarMain.setOnClickListener {
                (requireActivity() as MainActivity).changeFragment(3)
            }
        }
    }



    private fun setCategoriesAdapter() {
        views {
            categoriesAdapter = CategoryUIStateAdapter(requireContext())
            recyclerViewCategoriesFragment.layoutManager = GridLayoutManager(requireContext(), resources.getInteger(R.integer.columns_categories), LinearLayoutManager.VERTICAL, false)
            recyclerViewCategoriesFragment.adapter = categoriesAdapter


            categoriesAdapter?.onClickListener = object : CategoryUIStateAdapter.OnClickListener {

                override fun onClick(itemData: CategoryUIState) {
                    openDishesFragment(itemData)
                }

            }

        }
    }



    private fun setCategoriesListInRecyclerView(list: List<CategoryUIState>) {
        Log.d(TAG, "setCategories : Adapter - $list")
        categoriesAdapter?.setData(list)
        showLoading(list.isEmpty())
    }



    private fun setUserData(user: UserUIState?) {
        Log.d(TAG, "setUserData : data - $user")
        views {
            if (user != null) {

                Glide.with(context)
                    .load(user.image)
                    .error(R.drawable.not_loaded_one_image)
                    .placeholder(R.drawable.not_loaded_one_image)
                    .into(toolBarCategoriesFragment.imageViewToolbarMain)

            }
        }
    }



    private fun openDishesFragment(itemData: CategoryUIState) {
        val fragment = StoreFragment.newInstance()
        val bundle = Bundle()
        bundle.putString(ConstantsUI.MAIN_CATEGORY_BUNDLE, itemData.toString())
        fragment.arguments = bundle

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.add(R.id.frame_layout_categories_fragment,fragment )
        transaction.addToBackStack("First tab MainActivity")
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }



}