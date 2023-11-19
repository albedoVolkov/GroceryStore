package com.example.grocerystore.ui.activityMain.fragments.forthTab

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.grocerystore.GroceryStoreApplication
import com.example.grocerystore.R
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.databinding.FragmentAccountBinding
import com.example.grocerystore.ui.activityMain.fragments.forthTab.factories.AccountFragmentViewModelFactory
import com.example.grocerystore.ui.activityMain.fragments.forthTab.viewModels.AccountFragmentViewModel
import com.example.grocerystore.ui.activityMain.fragments.thirdTab.BasketFragment

class AccountFragment : Fragment() {



    companion object {
        const val TAG = "AccountFragment"

    }



    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private var _viewModel: AccountFragmentViewModel? = null
    private val viewModel get() = _viewModel!!
    private var _viewModelFactory: AccountFragmentViewModelFactory? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        _viewModelFactory = AccountFragmentViewModelFactory(GroceryStoreApplication(requireContext()).userRepository)
        _viewModel = ViewModelProvider(this, _viewModelFactory!!)[AccountFragmentViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews(null)
        setObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setViews(user : UserUIState?) {

        if(user != null) {
            Glide.with(context)
                .load(user.image)
                .error(R.drawable.not_loaded_image_background)
                .placeholder(R.drawable.not_loaded_image_background)
                .into(binding.imageView1AccountFragment)

            binding.textView2ValueAccountFragment.text = user.name

            binding.textView1ValueAccountFragment.text = user.phone

            binding.textView4ValueAccountFragment.text = user.email

            binding.textView11ValueAccountFragment.text = user.userType
        }else{
            Log.d(BasketFragment.TAG, "userData is null")
        }

    }


    private fun setObservers() {


        viewModel.userData.observe(viewLifecycleOwner) {
            if (it != null) {
                Log.d(TAG, "userData = $it")
                setViews(it)
            }else{
                Log.d(TAG, "userData is null")
            }
        }

    }


}