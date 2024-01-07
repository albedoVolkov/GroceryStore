package com.example.grocerystore.ui.activityMain.fragments.thirdTab

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocerystore.R
import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.databinding.BasketFragmentBinding
import com.example.grocerystore.ui.activityMain.fragments.thirdTab.adapters.CartUIStateAdapter
import com.example.grocerystore.ui.activityMain.fragments.thirdTab.viewModels.BasketFragmentViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach



class BasketFragment  : Fragment() {


    private val TAG = "BasketFragment"

    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return BasketFragment()
        }
    }


    private var binding: BasketFragmentBinding? = null
    private val viewModel: BasketFragmentViewModel by viewModels()
    private lateinit var cartsAdapter: CartUIStateAdapter

    private fun <T> views(block : BasketFragmentBinding.() -> T): T? = binding?.block()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View =
        BasketFragmentBinding.inflate(inflater, container, false).also{ binding = it }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViews()
        setCartsAdapter()

        viewModel.userData.onEach{
            if(it != null) {
                setUserData(it)
                viewModel.filterItems("Filtered", it.cart)
                views {
                    textViewBasketFragment.text = "Purchase ${viewModel.getPriceAllItems(viewModel.showCarts)}₽"
                }
                setCarts(viewModel.showCarts)
            }
        }.launchIn(viewModel.viewModelScope)
    }


    private fun setViews() {

        views {

            toolBarBasketFragment.containerImageToolbarMain.setOnClickListener {
                // by click on this btn we go to the account tab(forth tab)
                Toast.makeText(context, "click by account button", Toast.LENGTH_LONG).show()
            }

        }
    }


    private fun setCartsAdapter() {
        views {
            cartsAdapter = CartUIStateAdapter(requireContext())
            recyclerViewBasketFragment.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            recyclerViewBasketFragment.adapter = cartsAdapter

            toolBarBasketFragment.containerImageToolbarMain.setOnClickListener {
                // by click on this btn we go to the account tab(forth tab)
                Toast.makeText(context, "click by account button", Toast.LENGTH_LONG).show()
            }

            cartsAdapter.onClickListener1 = object : CartUIStateAdapter.OnClickListener {

                override fun onClick(itemData: CartUIState) {
                    viewModel.decreaseQuantityOfCart(itemData)
                }
            }

            cartsAdapter.onClickListener2 = object : CartUIStateAdapter.OnClickListener {

                override fun onClick(itemData: CartUIState) {
                    viewModel.increaseQuantityOfCart(itemData)
                }
            }

            cartsAdapter.onClickListener3 = object : CartUIStateAdapter.OnClickListener {

                override fun onClick(itemData: CartUIState) {
                    viewModel.deleteCart(itemData.cartId)
                }
            }

            val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    viewModel.deleteCart(cartsAdapter.getId(viewHolder.adapterPosition))
                }
            }
            views {
                val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
                itemTouchHelper.attachToRecyclerView(recyclerViewBasketFragment)
            }

        }
    }

    private fun setCarts(list: List<CartUIState>) {
        Log.d(TAG, "setCarts : Adapter : $list")
        views {
            cartsAdapter.setData(list)
            if (list.isNotEmpty()) {
                textView2BasketFragment.visibility = View.INVISIBLE
            } else { textView2BasketFragment.visibility = View.VISIBLE }

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
                    .into(toolBarBasketFragment.imageViewToolbarMain)

            } else {
                Log.d(TAG, "setUserData : ERROR : listData is null")
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




    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}