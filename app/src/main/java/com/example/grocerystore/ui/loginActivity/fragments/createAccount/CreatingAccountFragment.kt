package com.example.grocerystore.ui.loginActivity.fragments.createAccount

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
import androidx.lifecycle.Observer
import com.example.grocerystore.R
import com.example.grocerystore.ui.activityMain.MainActivity
import com.example.grocerystore.databinding.FragmentCreatingAccountBinding
import com.example.grocerystore.ui.loginActivity.fragments.createAccount.viewModel.CreateAccountViewModel


class CreatingAccountFragment : Fragment() {


    private val TAG = "CreatingAccountFragment"


    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return CreatingAccountFragment()
        }
    }


    private var binding: FragmentCreatingAccountBinding? = null
    private val viewModel: CreateAccountViewModel by viewModels()

    private fun <T> views(block : FragmentCreatingAccountBinding.() -> T): T? = binding?.block()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View =
        FragmentCreatingAccountBinding.inflate(inflater, container, false).also{ binding = it }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(false)
        showError(false)
        setViews()
        setListeners()

        viewModel.signUp("sssssss","modwert404@gmail.com","qwerty777QQQ")

    }


    private fun setViews() {

        views {

            viewModel.feedbackResultErrors.observe(viewLifecycleOwner, Observer { loginFormState ->
                    if (loginFormState == null) {
                        return@Observer
                    }
                    createAccountBtn1CRFragment.isEnabled = loginFormState.isDataValid
                    loginFormState.nameError?.let {
                        editText1CRFragment.error = getString(it)
                    }
                    loginFormState.emailError?.let {
                        editView2CRFragment.error = getString(it)
                    }
                    loginFormState.passwordError?.let {
                        editView3CRFragment.error = getString(it)
                    }
                    loginFormState.secondPasswordError?.let {
                        editView4CRFragment.error = getString(it)
                    }
                })// loginFormState returns made errors strings


            viewModel.feedbackResult.observe(viewLifecycleOwner, Observer { loginResult ->
                    loginResult ?: return@Observer
                    showLoading(false)

                    if (loginResult.error != null) {
                        showError(true, 5, loginResult.error)
                    }

                    if (loginResult.success != null) {
                        startMain()
                    }

                })// loginResult return success or error of login user



            val afterTextChangedListener = object : TextWatcher { override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable) {

                    viewModel.dataChanged(
                        editText1CRFragment.text.toString(),
                        editView2CRFragment.text.toString(),
                        editView3CRFragment.text.toString(),
                        editView4CRFragment.text.toString()
                    )
                }// actions by changing text
            }



            editView2CRFragment.addTextChangedListener(afterTextChangedListener)
            editView3CRFragment.addTextChangedListener(afterTextChangedListener)
            editText1CRFragment.addTextChangedListener(afterTextChangedListener)
            editView4CRFragment.addTextChangedListener(afterTextChangedListener)

        }
    }


    private fun setListeners() {
        views {
            containerBtnBack1CRFragment.setOnClickListener {
                showLoading(false)
                val transaction = activity?.supportFragmentManager?.beginTransaction()!!
                transaction.replace(R.id.container_login_activity,Fragment())
                transaction.commit()
            }

            createAccountBtn1CRFragment.setOnClickListener {
                viewModel.signUp(
                    editText1CRFragment.text.toString(),
                    editView2CRFragment.text.toString(),
                    editView3CRFragment.text.toString()
                )
            }// just enter in app by click on this button

            frameLayout2CRFragment.setOnClickListener {}
        }
    }





    private fun startMain() {
        activity?.let{
            val intent = Intent (it, MainActivity::class.java)
            it.startActivity(intent)
        }
    }



    private fun showError(success: Boolean, count: Int = 0, error : Int = 0) {
        views {
            if (success) {
                if (error != 0) {
                    textViewError1CRFragment.text = getString(error)
                }
                textViewError1CRFragment.visibility = View.VISIBLE
                val timer = object : CountDownTimer(count.toLong() * 1000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}

                    override fun onFinish() {
                        textViewError1CRFragment.visibility = View.GONE
                    }
                }
                timer.start()
            } else {
                textViewError1CRFragment.visibility = View.GONE
            }
        }
    }



    private fun showLoading(success : Boolean, timerSeconds : Int = 100) {
        views {
            if (success) {

                containerLoader1CRFragment.visibility = View.VISIBLE
                scrollView1CRFragment.visibility = View.GONE

                val timer = object : CountDownTimer((timerSeconds * 1000).toLong(), 500) {
                    override fun onTick(millisUntilFinished: Long) {
                    }

                    override fun onFinish() {
                        containerLoader1CRFragment.visibility = View.GONE
                        scrollView1CRFragment.visibility = View.VISIBLE
                    }
                }
                timer.start()

            } else {

                containerLoader1CRFragment.visibility = View.GONE
                scrollView1CRFragment.visibility = View.VISIBLE

            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}

