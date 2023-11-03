package com.example.grocerystore.loginActivity.login

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.grocerystore.ConstantsSource
import com.example.grocerystore.GroceryStoreApplication
import com.example.grocerystore.R
import com.example.grocerystore.activityMain.MainActivity
import com.example.grocerystore.data.helpers.UIstates.item.AddressUIState
import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.data.helpers.UIstates.item.DishUIState
import com.example.grocerystore.data.helpers.UIstates.item.OrderUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.databinding.ActivityLoginBinding
import com.example.grocerystore.loginActivity.createAccount.CreatingAccountFragment
import com.example.grocerystore.loginActivity.login.viewModel.LoginViewModel
import com.example.grocerystore.loginActivity.login.viewModel.LoginViewModelFactory


class LoginActivity : AppCompatActivity() {


    companion object{
        const val TAG = "LoginActivity"
    }


    private var _loginViewModel: LoginViewModel? = null
    private val loginViewModel get() = _loginViewModel!!
    private var _loginViewModelFactory : LoginViewModelFactory? = null

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val user = UserUIState("1","ALBERT","https://i.pinimg.com/736x/1c/92/4a/1c924a816ee507fcc04d76ef87d84fc0.jpg","modwert404@gmail.com","qwerty777",
//            arrayListOf(1,2),
//            AddressUIState("1","1","1","1","1","1",),
//            listOf(CartUIState("1","1", DishUIState())),
//            listOf(OrderUIState()),"1"
//        )

        setViews()
        setObservers()
    }



    private fun setViews() {
        val loginRep = GroceryStoreApplication(context = this).userRepository
        _loginViewModelFactory = LoginViewModelFactory(loginRep)
        Log.d(TAG,"LoginViewModelFactory - $_loginViewModelFactory")
        _loginViewModel = ViewModelProvider(this, _loginViewModelFactory!!)[LoginViewModel::class.java]

        // image logo
        Glide.with(applicationContext)
            .load(com.example.grocerystore.R.drawable.logo2)
            .error(com.example.grocerystore.R.drawable.not_loaded_image_background)
            .placeholder(com.example.grocerystore.R.drawable.not_loaded_image_background)
            .into( binding.imageView1ActivityLogin)

    }



    private fun setObservers() {

        val emailEditText = binding.emailActivityLogin
        val passwordEditText = binding.passwordActivityLogin
        val loginBtn = binding.loginBtnActivityLogin
        val forgotPasswordBtn = binding.textView1ActivityLogin
        val createAccountBtn = binding.createAccountBtn1ActivityLogin
        showAccountNotExisted(false)
        showProgress(false)


        loginViewModel.loginFormState.observe(this, Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                loginBtn.isEnabled = loginFormState.isDataValid
                loginFormState.emailError?.let {
                    emailEditText.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    passwordEditText.error = getString(it)
                }
            })// loginFormState returns made errors strings

        loginViewModel.loginResult.observe(this, Observer { loginResult ->
                loginResult ?: return@Observer
                    showProgress(false)

                if(loginResult.error != null){
                    showProgress(false)
                    showAccountNotExisted(true,5)
                }

                if(loginResult.success != null){
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

                loginViewModel.loginDataChanged(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString(),
                )
            }// actions by changing text
        }

        emailEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)

        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }//automatically enter in app

        loginBtn.setOnClickListener {
            showProgress(true)
            loginViewModel.login(emailEditText.text.toString(), passwordEditText.text.toString())
        }// just enter in app by button

        forgotPasswordBtn.setOnClickListener {
            showProgress(true)
            TODO()
            showProgress(false)
        }

        createAccountBtn.setOnClickListener {

            showProgress(true)


            val fragment = CreatingAccountFragment()
            val bundle = Bundle()
            bundle.putString(ConstantsSource().EMAIL_DATA_FROM_FRAGMENTS, emailEditText.text.toString())
            bundle.putString(ConstantsSource().PASSWORD_DATA_FROM_FRAGMENTS, passwordEditText.text.toString())
            fragment.arguments = bundle

            showProgress(false)

            val transaction = supportFragmentManager.beginTransaction()
            transaction.setReorderingAllowed(true)
            transaction.addToBackStack(null)
            transaction.replace(R.id.container_login_activity,fragment)
            transaction.commit()


        }
    }



    private fun updateUiByLoginUser() {
        finish()
        val myIntent = Intent(this@LoginActivity, MainActivity::class.java)
        this@LoginActivity.startActivity(myIntent)
    }// enter in app


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun showProgress(success: Boolean) {
        if (success) {
            binding.loadingActivityLogin.visibility = View.VISIBLE
        } else {
            binding.loadingActivityLogin.visibility = View.GONE
        }
    }

    private fun showAccountNotExisted(success : Boolean,count : Int = 0) {
        if (success) {
            binding.textView4ActivityLogin.visibility = View.VISIBLE
            val timer = object : CountDownTimer(count.toLong() * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {}

                override fun onFinish() {
                    binding.textView4ActivityLogin.visibility = View.GONE
                }
            }
            timer.start()
        }else{
            binding.textView4ActivityLogin.visibility = View.GONE
        }
    }



}