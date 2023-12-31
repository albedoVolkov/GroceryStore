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
import com.example.grocerystore.data.helpers.UIstates.item.DishUIState
import com.example.grocerystore.data.helpers.UIstates.item.TitleUIState
import com.example.grocerystore.data.helpers.UIstates.item.fromStringToCategoryItem
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.databinding.StoreFragmentBinding
import com.example.grocerystore.ui.activityMain.MainActivity
import com.example.grocerystore.ui.activityMain.fragments.firstTab.adapters.DishUIStateStoreAdapter
import com.example.grocerystore.ui.activityMain.fragments.firstTab.adapters.TitleUIStateAdapter
import com.example.grocerystore.ui.activityMain.fragments.firstTab.viewModels.StoreFragmentViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class StoreFragment : Fragment() {


    private val TAG = "StoreFragment"


    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return StoreFragment()
        }
    }


    private var binding: StoreFragmentBinding? = null
    private lateinit var dishUIStateAdapter: DishUIStateStoreAdapter
    private lateinit var titleUIStateAdapter: TitleUIStateAdapter
    private val viewModel: StoreFragmentViewModel by viewModels()

    private fun <T> views(block : StoreFragmentBinding.() -> T): T? = binding?.block()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View =
        StoreFragmentBinding.inflate(inflater, container, false).also{ binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var mainCategory : CategoryUIState? = null
        val bundle = this.arguments
        if (bundle != null) {
            val mainCategoryString = bundle.getString(ConstantsSource.MAIN_CATEGORY_BUNDLE, "")
            mainCategory = fromStringToCategoryItem(mainCategoryString)
        }


        if (mainCategory != null) {
            viewModel.setMainCategory(mainCategory)
            viewModel.refreshDishes(mainCategory)

            setDishesAdapter()
            setTitlesAdapter()
            setViews(mainCategory)

            viewModel.mainDishes.onEach{
                Log.d(TAG, "mainDishes : $it")
                viewModel.filterDishes(it, viewModel.filterDishesType)
                setDishesList(viewModel.showDishes)

                viewModel.refreshTitles(it)
            }.launchIn(viewModel.viewModelScope)


            viewModel.mainTitles.onEach{
                Log.d(TAG, "mainTitles : $it")
                viewModel.filterTitles(it,viewModel.filterTitlesType)
                setTitlesList(viewModel.showTitles)
            }.launchIn(viewModel.viewModelScope)

            viewModel.userData.onEach(::setUserData).launchIn(viewModel.viewModelScope)// this func just calls func collect in other scope

        }else{
            val transaction = parentFragmentManager.beginTransaction()
            transaction.remove(this@StoreFragment)
            transaction.commit()
        }
    }



    private fun setUserData(user: UserUIState?) {
        views {
            if (user != null) {
                Log.d(TAG, " getCurrentUserData : SUCCESS : listData is $user")

                Glide.with(context)
                    .load(user.image)
                    .error(R.drawable.not_loaded_one_image)
                    .placeholder(R.drawable.not_loaded_one_image)
                    .into(toolBarStoreFragment.imageToolbarDishes)

            } else {
                Log.d(TAG, "getCurrentUserData : ERROR : listData is null")
            }
        }
    }




    private fun setViews(category : CategoryUIState) {
        views {

            toolBarStoreFragment.textView2ToolbarDishes.text = category.name

            toolBarStoreFragment.viewToolbarDishes.setOnClickListener {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.remove(this@StoreFragment)
                transaction.commit()
            }

            toolBarStoreFragment.containerImageToolbarDishes.setOnClickListener {
                // by click on this btn we go to the account tab(forth tab)
                (requireActivity() as MainActivity).changeFragment(3)
            }

        }
    }




    private fun setDishesAdapter() {

        views {
            dishUIStateAdapter = DishUIStateStoreAdapter(requireContext())
            recyclerView1StoreFragment.layoutManager = GridLayoutManager(requireContext(), resources.getInteger(R.integer.columns_dish_store), LinearLayoutManager.VERTICAL, false)
            recyclerView1StoreFragment.adapter = dishUIStateAdapter

                dishUIStateAdapter.onClickListener = object : DishUIStateStoreAdapter.OnClickListener {

                    override fun onClick(itemData: DishUIState) {
                        val fragment = InfoDishFragment()
                        val bundle = Bundle()
                        bundle.putString(ConstantsSource.MAIN_DISH_BUNDLE, itemData.toString())
                        fragment.arguments = bundle

                        val transaction =  parentFragmentManager.beginTransaction()
                        transaction.setReorderingAllowed(true)
                        transaction.addToBackStack(null)
                        transaction.add(R.id.frame_layout_categories_fragment,fragment)
                        transaction.commit()

                    }
            }
        }
    }



    private fun setTitlesAdapter() {

        views {
            showLoading(true)
            titleUIStateAdapter  = TitleUIStateAdapter(requireContext())
            recyclerView2StoreFragment.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            recyclerView2StoreFragment.adapter = titleUIStateAdapter

            titleUIStateAdapter.onClickListener = object : TitleUIStateAdapter.OnClickListener {

                override fun onClick(itemData: TitleUIState) {
                    viewModel.filterDishesType = itemData.name
                    if(viewModel.mainCategory != null) {
                        viewModel.refreshDishes(viewModel.mainCategory!!)
                    }else{
                        Log.d(TAG, "setTitlesAdapter : click : viewModel.mainCategory = null")
                    }
                }

            }
        }

    }



    private fun setDishesList(list: List<DishUIState>) {
        Log.d(TAG, "setDishesList : list : $list")
        dishUIStateAdapter.setData(list)
        showLoading(list.isEmpty())
    }


    private fun setTitlesList(list: List<TitleUIState>) {
        Log.d(TAG, "setTitlesList : list : $list")
        titleUIStateAdapter.setData(list)
        showLoading(list.isEmpty())
    }


    private fun showLoading(success : Boolean) {
        views {
            if (success) {
                loaderLayout.loaderBackground.visibility = View.VISIBLE
                containerStoreFragment.visibility = View.GONE
                loaderLayout.circularLoader.showAnimationBehavior
            } else {
                loaderLayout.loaderBackground.visibility = View.GONE
                containerStoreFragment.visibility = View.VISIBLE
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}