package com.example.grocerystore.ui.loginActivity


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.grocerystore.R
import com.example.grocerystore.databinding.ActivityLoginBinding
import com.example.grocerystore.locateLazy
import com.example.grocerystore.domain.services.CheckNetworkConnection
import com.example.grocerystore.ui.loginActivity.fragments.login.LoginFragment


class LoginActivity : AppCompatActivity() {


    companion object {
        const val TAG = "LoginActivity"
    }

    private var binding: ActivityLoginBinding? = null
    private val networkManager by locateLazy<CheckNetworkConnection>()
    private fun <T> views(block : ActivityLoginBinding.() -> T): T? = binding?.block()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setReorderingAllowed(true)
            transaction.add(R.id.frameLayout_activity_login, LoginFragment.newInstance())
            transaction.addToBackStack("First tab LoginActivity")
            transaction.commit()
        }
        networkManager.observe(this){ showNoLoading(it == true) }
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (this.supportFragmentManager.backStackEntryCount > 0) {
            this.supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }



    private fun showNoLoading(success : Boolean) {
        views {
            if (!success) {
                loaderLayout.loaderBackground.visibility = View.VISIBLE
                frameLayoutActivityLogin.visibility = View.GONE
                loaderLayout.circularLoader.showAnimationBehavior
            } else {
                loaderLayout.loaderBackground.visibility = View.GONE
                frameLayoutActivityLogin.visibility = View.VISIBLE
            }
        }
    }
}