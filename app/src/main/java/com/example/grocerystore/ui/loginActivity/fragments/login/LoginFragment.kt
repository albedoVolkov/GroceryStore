package com.example.grocerystore.ui.loginActivity.fragments.login


import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.grocerystore.R
import com.example.grocerystore.databinding.FragmentLoginBinding
import com.example.grocerystore.services.ConstantsSource
import com.example.grocerystore.ui.activityMain.MainActivity
import com.example.grocerystore.ui.loginActivity.fragments.createAccount.CreatingAccountFragment



class LoginFragment : Fragment() {

    private val TAG = "LoginFragment"

    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return LoginFragment()
        }
    }

    private var binding: FragmentLoginBinding? = null
    private val viewModel: LoginFragmentViewModel by viewModels()

    private fun <T> views(block : FragmentLoginBinding.() -> T): T? = binding?.block()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View =
        FragmentLoginBinding.inflate(inflater, container, false).also{ binding = it }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViews()
        setObservers()

    }


    private fun setViews() {
        viewModel.getLoginStatus()
        showLoading(false)
        showAccountNotExisted(false)

        views {
            // image logo
            Glide.with(context)
                .load(R.drawable.main_logo_2_image)
                .error(R.drawable.not_loaded_one_image)
                .placeholder(R.drawable.not_loaded_one_image)
                .into(imageView1LoginFragment)
        }

    }



    private fun setObservers() {

    views {
//        val createAccountBtn = createAccountBtn1ActivityLogin

        viewModel.isLoggedIn.observe(viewLifecycleOwner) {
            if (it) {
                startMain()
            }
        }// loginFormState returns made errors strings


        viewModel.loginFormState.observe(viewLifecycleOwner){ loginFormState ->
            if (loginFormState == null) {
                return@observe
            }
            loginBtnLoginFragment.isEnabled = loginFormState.isDataValid
            loginFormState.emailError?.let {
                emailLoginFragment.error = getString(it)
            }
            loginFormState.passwordError?.let {
               passwordLoginFragment.error = getString(it)
            }
        }// loginFormState returns made errors strings


        viewModel.loginResult.observe(viewLifecycleOwner) { loginResult ->
            loginResult ?: return@observe
            showLoading(false)

            if (loginResult.error != null) {
                showAccountNotExisted(true, 5)
            }

            if (loginResult.success != null) {
                startMain()
            }

        }// loginResult return success or error of login user


        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {

                viewModel.loginDataChanged(
                    emailLoginFragment.text.toString(),
                    passwordLoginFragment.text.toString(),
                )
            }// actions by changing text
        }

        emailLoginFragment.addTextChangedListener(afterTextChangedListener)
        passwordLoginFragment.addTextChangedListener(afterTextChangedListener)


        loginBtnLoginFragment.setOnClickListener {
                showLoading(true)
                viewModel.login(
                    emailLoginFragment.text.toString(),
                    passwordLoginFragment.text.toString()
                )
        }// just enter in app by button


        textView1LoginFragment.setOnClickListener {
            //showProgress(false)
        }


        createAccountBtn1LoginFragment.setOnClickListener {
            showLoading(true)


            val fragment = CreatingAccountFragment()
            val bundle = Bundle()
            bundle.putString(
                ConstantsSource.EMAIL_DATA_FROM_FRAGMENTS,
                emailLoginFragment.text.toString()
            )
            bundle.putString(
                ConstantsSource.PASSWORD_DATA_FROM_FRAGMENTS,
                passwordLoginFragment.text.toString()
            )
            fragment.arguments = bundle

            showLoading(false)

            val transaction = activity?.supportFragmentManager?.beginTransaction()!!
            transaction.replace(R.id.container_login_activity, fragment)
            //transaction.addToBackStack(null)
            transaction.commit()
        }
    }
    }



    private fun startMain() {
        activity?.finish()
        val myIntent = Intent(activity, MainActivity::class.java)
        startActivity(myIntent)
    }// enter in app



    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }



    private fun showLoading(success: Boolean) {
        views {
            if (success) {
                containerLoaderLoginFragment.visibility = View.VISIBLE
                scrollView1LoginFragment.visibility  = View.GONE
                loaderLayoutLoginFragment.circularLoader.showAnimationBehavior
            } else {
                containerLoaderLoginFragment.visibility = View.GONE
                scrollView1LoginFragment.visibility  = View.VISIBLE
            }
        }
    }

    private fun showAccountNotExisted(success : Boolean,count : Int = 0) {
        views {
            if (success) {
                textView4LoginFragment.visibility = View.VISIBLE
                val timer = object : CountDownTimer(count.toLong() * 1000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}

                    override fun onFinish() {
                        textView4LoginFragment.visibility = View.GONE
                    }
                }
                timer.start()
            } else {
                textView4LoginFragment.visibility = View.GONE
            }
        }
    }

}