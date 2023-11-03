package com.example.grocerystore.loginActivity.createAccount

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.grocerystore.ConstantsSource
import com.example.grocerystore.GroceryStoreApplication
import com.example.grocerystore.R
import com.example.grocerystore.activityMain.MainActivity
import com.example.grocerystore.activityMain.ui.firstTab.factories.CategoriesFragmentViewModelFactory
import com.example.grocerystore.activityMain.ui.firstTab.viewModels.CategoriesFragmentViewModel
import com.example.grocerystore.data.helpers.UIstates.item.AddressUIState
import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.data.helpers.UIstates.item.DishUIState
import com.example.grocerystore.data.helpers.UIstates.item.OrderUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.databinding.ActivityLoginBinding
import com.example.grocerystore.databinding.CategoriesFragmentBinding
import com.example.grocerystore.databinding.FragmentCreatingAccountBinding
import com.example.grocerystore.loginActivity.createAccount.viewModel.CreateAccountViewModel
import com.example.grocerystore.loginActivity.createAccount.viewModel.CreateAccountViewModelFactory
import com.example.grocerystore.loginActivity.login.LoginActivity
import com.example.grocerystore.loginActivity.login.viewModel.LoginViewModelFactory
import com.example.grocerystore.loginActivity.login.viewModel.LoginViewModel


class CreatingAccountFragment : Fragment() {


    companion object {
        const val TAG = "LoginActivity"
    }


    private var _createAccountViewModel: CreateAccountViewModel? = null
    private val createAccountViewModel get() = _createAccountViewModel!!
    private var _createAccountViewModelFactory: CreateAccountViewModelFactory? = null

    private var _binding: FragmentCreatingAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreatingAccountBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
        setObservers()
    }



    private fun setViews() {
        _createAccountViewModelFactory = CreateAccountViewModelFactory(GroceryStoreApplication(requireContext()).userRepository)
        Log.d(TAG, "CreateAccountViewModel - $_createAccountViewModel")
        _createAccountViewModel = ViewModelProvider(this, _createAccountViewModelFactory!!)[CreateAccountViewModel::class.java]
    }


    private fun setObservers() {

        val nameEditText = binding.editText1CRFragment
        val emailEditText = binding.editView2CRFragment
        val passwordEditText = binding.editView3CRFragment
        val secondPasswordEditText = binding.editView4CRFragment
        val backBtn = binding.btnBack1CRFragment
        val createAccountBtn = binding.createAccountBtn1CRFragment

        showProgress(false)
        showError(false)

        createAccountViewModel.feedbackResultErrors.observe(viewLifecycleOwner, Observer { loginFormState ->
            if (loginFormState == null) {
                return@Observer
            }
            createAccountBtn.isEnabled = loginFormState.isDataValid
            loginFormState.nameError?.let {
                nameEditText.error = getString(it)
            }
            loginFormState.emailError?.let {
                emailEditText.error = getString(it)
            }
            loginFormState.passwordError?.let {
                passwordEditText.error = getString(it)
            }
            loginFormState.secondPasswordError?.let {
                secondPasswordEditText.error = getString(it)
            }
        })// loginFormState returns made errors strings


        createAccountViewModel.feedbackResult.observe(viewLifecycleOwner, Observer { loginResult ->
            loginResult ?: return@Observer
            showProgress(false)

            if (loginResult.error != null) {
                showProgress(false)
                showError(true, 5, loginResult.error)
            }

            if (loginResult.success != null) {
                showProgress(false)
                updateUiByLoginUser()
            }

        })// loginResult return success or error of login user


        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {

                createAccountViewModel.dataChanged(
                    nameEditText.text.toString(),
                    emailEditText.text.toString(),
                    passwordEditText.text.toString(),
                    secondPasswordEditText.text.toString()
                )
            }// actions by changing text
        }

        emailEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        nameEditText.addTextChangedListener(afterTextChangedListener)
        secondPasswordEditText.addTextChangedListener(afterTextChangedListener)

        createAccountBtn.setOnClickListener {
            showProgress(true)
            createAccountViewModel.signUp(nameEditText.text.toString(), emailEditText.text.toString(),passwordEditText.text.toString())
        }// just enter in app by click on this button

        backBtn.setOnClickListener {
            showProgress(false)
            val transaction = activity?.supportFragmentManager?.beginTransaction()!!
            transaction.replace(R.id.container_login_activity,Fragment())
            transaction.commit()
        }
    }


    private fun updateUiByLoginUser() {
        activity?.let{
            val intent = Intent (it, MainActivity::class.java)
            it.startActivity(intent)
        }
    }// enter in app


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun showProgress(success: Boolean) {
        if (success) {
            binding.loading1CRFragment.visibility = View.VISIBLE
        } else {
            binding.loading1CRFragment.visibility = View.GONE
        }
    }

    private fun showError(success: Boolean, count: Int = 0, error : Int = 0) {
        if (success) {
            if(error != 0) {
                binding.textViewError1CRFragment.text = getString(error)
            }
            binding.textViewError1CRFragment.visibility = View.VISIBLE
            val timer = object : CountDownTimer(count.toLong() * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {}

                override fun onFinish() {
                    binding.textViewError1CRFragment.visibility = View.GONE
                }
            }
            timer.start()
        } else {
            binding.textViewError1CRFragment.visibility = View.GONE
        }
    }

}

