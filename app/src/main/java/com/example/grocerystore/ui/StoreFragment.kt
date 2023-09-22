package com.example.grocerystore.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerystore.GroceryStoreApplication
import com.example.grocerystore.R
import com.example.grocerystore.data.helpers.CategoryUIState
import com.example.grocerystore.data.helpers.DishUIState
import com.example.grocerystore.data.helpers.TitleUIState
import com.example.grocerystore.databinding.StoreFragmentBinding
import com.example.grocerystore.ui.adapters.DishUIStateStoreAdapter
import com.example.grocerystore.ui.adapters.TitleUIStateAdapter
import com.example.grocerystore.ui.utils.ConstantsSourceUI
import com.example.grocerystore.ui.viewModels.StoreFragmentViewModel
import com.google.gson.Gson


class StoreFragment : Fragment() {

    private val TAG = "StoreFragment"

    private var _binding: StoreFragmentBinding? = null
    private val binding get() = _binding!!

    private var _dishUIStateAdapter: DishUIStateStoreAdapter? = null
    private val dishUIStateAdapter get() =  _dishUIStateAdapter!!
    private var _titleUIStateAdapter: TitleUIStateAdapter? = null
    private val titleUIStateAdapter get() =  _titleUIStateAdapter!!

    private var _viewModel: StoreFragmentViewModel? = null
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = StoreFragmentBinding.inflate(inflater, container, false)
        _viewModel = StoreFragmentViewModel(GroceryStoreApplication(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(viewModel.mainCategory.id == -1) {
            val bundle = this.arguments
            if (bundle != null) {
                val mainCategory_JSON = bundle.getString(ConstantsSourceUI().MAIN_CATEGORY_BUNDLE)
                val mainCategory = Gson().fromJson(mainCategory_JSON, CategoryUIState::class.java) as CategoryUIState
                viewModel.setCategoryById(mainCategory)
                viewModel.refreshDishes()
            }
        }
        if(viewModel.mainCategory.name != "" && viewModel.mainCategory.name != "null"){
        Log.d(TAG,"viewModel.showNameCategory : data = ${viewModel.mainCategory.name}")
            binding.toolBarStoreFragment.textView2ToolbarCategory.text = viewModel.mainCategory.name
        }else{
            Log.d(TAG,"viewModel.showNameCategory : data = null")
            binding.toolBarStoreFragment.textView2ToolbarCategory.text = "not deffined"
        }
        setObservers()
        setViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setViews() {
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

            binding.toolBarStoreFragment.viewToolbarCategory.setOnClickListener{
                val transaction = parentFragmentManager.beginTransaction()
                transaction.remove(this@StoreFragment)
                transaction.commit()
            }

        }else{Log.d(TAG,"Context is null") }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setObservers() {
        viewModel.showDishes.observe(viewLifecycleOwner) { dishesList ->
            if (dishesList.isNotEmpty()) {

                dishUIStateAdapter.data = dishesList
                dishUIStateAdapter.notifyDataSetChanged()
                Log.d(TAG, "DishesAdapter : $dishesList")

                binding.loaderLayout.circularLoader.hideAnimationBehavior
                binding.loaderLayout.loaderBackground.visibility = View.GONE
                binding.containerDishesFragment.visibility = View.VISIBLE
            } else {
                binding.loaderLayout.loaderBackground.visibility = View.VISIBLE
                binding.loaderLayout.circularLoader.showAnimationBehavior
                binding.containerDishesFragment.visibility = View.GONE
            }
        }
        viewModel.filterType.observe(viewLifecycleOwner) {
                viewModel.filterDishes("All")
        }


        viewModel.showTitles.observe(viewLifecycleOwner) { titlesList ->
            if (titlesList.isNotEmpty()) {

                titleUIStateAdapter.data = titlesList
                titleUIStateAdapter.notifyDataSetChanged()
                Log.d(TAG, "TitlesAdapter : $titlesList")

                binding.loaderLayout.circularLoader.hideAnimationBehavior
                binding.loaderLayout.loaderBackground.visibility = View.GONE
                binding.containerDishesFragment.visibility = View.VISIBLE
            } else {
                binding.loaderLayout.loaderBackground.visibility = View.VISIBLE
                binding.loaderLayout.circularLoader.showAnimationBehavior
                binding.containerDishesFragment.visibility = View.GONE
            }
        }



    }

    private fun setDishesAdapter(itemList: List<DishUIState>?) {
            _dishUIStateAdapter = DishUIStateStoreAdapter(itemList ?: emptyList(), requireContext())
            dishUIStateAdapter.onClickListener = object : DishUIStateStoreAdapter.OnClickListener {

                override fun onClick(itemData: DishUIState) {
                    val fragment = StoreFragment()
                    val bundle = Bundle()
                    bundle.putString(ConstantsSourceUI().TITLE_ITEM_BUNDLE, viewModel.getDishById(itemData.id)?.name)
                    fragment.arguments = bundle

                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.add(R.id.frame_layout_1_store_fragment, fragment)
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



}