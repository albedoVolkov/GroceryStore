package com.example.grocerystore.ui.activityMain.fragments.firstTab

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.grocerystore.R
import com.example.grocerystore.domain.models.item.fromStringToDishItem
import com.example.grocerystore.databinding.InfoDishFragmentBinding
import com.example.grocerystore.ui.activityMain.fragments.firstTab.viewModels.InfoDishFragmentViewModel
import com.example.grocerystore.ui.utils.ConstantsUI
import java.lang.StringBuilder


class InfoDishFragment : Fragment() {



    companion object {

        const val TAG = "InfoDishFragment"

        @JvmStatic
        fun newInstance(): Fragment {
            return InfoDishFragment()
        }
    }

    private var binding: InfoDishFragmentBinding? = null
    private val viewModel: InfoDishFragmentViewModel by viewModels()

    private fun <T> views(block : InfoDishFragmentBinding.() -> T): T? = binding?.block()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View =
        InfoDishFragmentBinding.inflate(inflater, container, false).also{ binding = it }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = this.arguments
        Log.d(TAG, "onViewCreated : bundle - $bundle")
        if (bundle != null) {

            val dish = fromStringToDishItem(bundle.getString(ConstantsUI.MAIN_DISH_BUNDLE,null))

            Log.d(TAG, "onViewCreated : mainDish - $dish")
            if(dish != null) {
                viewModel.mainDish = dish
            }else{ leaveFragment() }
        }else{ leaveFragment() }


        setViews()
        setListeners()

    }




    private fun setViews() {

        if(viewModel.mainDish != null) {

            val dish = viewModel.mainDish!!
            Log.d(TAG, "setViews : dish - $dish")

            views {
                if (dish.image != "") {
                    Glide.with(requireContext())
                        .load(dish.image)
                        .error(R.drawable.not_loaded_one_image)
                        .placeholder(R.drawable.not_loaded_one_image)
                        .into(imageViewInfoDishFragment)
                }

                textView2InfoDishFragment.text = dish.name
                textView1InfoDishFragment.text = StringBuilder().append(dish.price.toString()," ₽")
                textView3InfoDishFragment.text = StringBuilder().append(dish.weight.toString(), "г")
                textView4InfoDishFragment.text = dish.description
            }
        }

    }



    private fun setListeners() {
        views {
            cardView1InfoDishFragment.setOnClickListener {
                leaveFragment()
            }

            cardView2InfoDishFragment.setOnClickListener {
                Toast.makeText(context, "Like", Toast.LENGTH_SHORT).show()
            }

            btn1InfoDishFragment.setOnClickListener {
                viewModel.addMainDishInBasketOfUser()
                Toast.makeText(context, "${viewModel.mainDish?.name} was added to the basket", Toast.LENGTH_LONG).show()
                leaveFragment()
            }

            frameLayout2InfoDishFragment.setOnClickListener { }
        }
    }


    private fun leaveFragment(){
        val transaction = parentFragmentManager.beginTransaction()
        transaction.remove(this@InfoDishFragment)
        transaction.commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}