package com.example.grocerystore.ui.activityMain.fragments.forthTab

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.grocerystore.R
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.databinding.FragmentAccountBinding
import com.example.grocerystore.ui.activityMain.fragments.forthTab.viewModels.AccountFragmentViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AccountFragment : Fragment() {

    private val TAG = "AccountFragment"

    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return AccountFragment()
        }
    }

    private var binding: FragmentAccountBinding? = null
    private val viewModel: AccountFragmentViewModel by viewModels()


    private fun <T> views(block : FragmentAccountBinding.() -> T): T? = binding?.block()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentAccountBinding.inflate(inflater, container, false).also{ binding = it }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()

        viewModel.userData.onEach(::setUserData).launchIn(viewModel.viewModelScope)// ERROR
    }

    private fun setListeners() {

        views {
            buttonLogOutAccountFragment.setOnClickListener {
                throw RuntimeException("Test Crash") // Force a crash
            }
        }

    }




    private fun setUserData(user : UserUIState?) {

        views {
            if (user != null) {
                Glide.with(context)
                    .load(user.image)
                    .error(R.drawable.not_loaded_one_image)
                    .placeholder(R.drawable.not_loaded_one_image)
                    .into(imageView1AccountFragment)

                textView2ValueAccountFragment.text = user.name

                textView1ValueAccountFragment.text = user.email

                textView4ValueAccountFragment.text = user.userId

                textView11ValueAccountFragment.text = user.userType
            } else {
                Log.d(TAG, "userData is null")
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}