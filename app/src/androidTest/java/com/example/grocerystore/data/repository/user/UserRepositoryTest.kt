package com.example.grocerystore.data.repository.user

import androidx.annotation.VisibleForTesting
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.grocerystore.GroceryStoreApplication
import com.example.grocerystore.services.ShoppingAppSessionManager
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.data.helpers.Utils
import com.example.grocerystore.data.source.local.GroceryStoreDatabase
import com.example.grocerystore.data.source.local.user.UserDao
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.time.Duration.Companion.seconds

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class UserRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    private lateinit var dataSource: GroceryStoreDatabase
    private lateinit var userDao: UserDao
    private lateinit var userRepository: UserRepoInterface
    private lateinit var sessionManager: ShoppingAppSessionManager


    @Before
    fun setup(){
        dataSource = GroceryStoreDatabase.getDataBase(context = ApplicationProvider.getApplicationContext())

        sessionManager = GroceryStoreApplication(context = ApplicationProvider.getApplicationContext()).sessionManager
        userRepository = GroceryStoreApplication(context = ApplicationProvider.getApplicationContext()).userRepository
        userDao = dataSource.userDao()

    }

    @After
    fun tearDataSource(){
        dataSource.close()
    }

    @After
    fun resetAll(){
        GroceryStoreApplication(context = ApplicationProvider.getApplicationContext()).resetAll()
    }

    @After
    fun tearSessionManager(){
        sessionManager.deleteLoginSession()
    }







    @Test
    fun insertUserInUserRepositoryTest(): Unit = runTest{
        val user = UserUIState("1", "alex", "url", "230045320","gef@gmail.com","qwerty777", listOf(),listOf(), listOf(), listOf(), Utils.UserType.CUSTOMER.name)
        launch {
            userRepository.addUserLocalSource(user)

            val userList = userDao.getAll().getOrAwaitValue()
            Truth.assertThat(userList).contains(user)
            Truth.assertThat(userList.size).isEqualTo(1)
        }

    }

    @Test
    fun deleteUserFromUserRepositoryTest(): Unit = runTest(timeout = 30.seconds) {
        val user = UserUIState("1", "alex", "url", "230045320","gef@gmail.com","qwerty777", listOf(),listOf(), listOf(), listOf(), Utils.UserType.CUSTOMER.name)

        userRepository.addUserLocalSource(user)
        Truth.assertThat(userDao.getAll().getOrAwaitValue()).contains(user)

        userRepository.deleteUserLocalSource("1")
        Truth.assertThat(userDao.getAll().getOrAwaitValue()).doesNotContain(user)

    }

    @Test
    fun getByIdUserFromUserRepositoryTest(): Unit = runTest(timeout = 30.seconds)  {
        val user = UserUIState("1", "alex", "url", "230045320","gef@gmail.com","qwerty777", listOf(),listOf(), listOf(), listOf(), Utils.UserType.CUSTOMER.name)

        userRepository.addUserLocalSource(user)
        val newUserNotNull = userRepository.getUserById("1").getOrNull()
            ?: UserUIState()

        Truth.assertThat(newUserNotNull).isEqualTo(user)

    }

    @Test
    fun checkLoginRepositoryTest(): Unit = runTest(timeout = 30.seconds) {
        val user = UserUIState("1", "alex", "url", "230045320","gef@gmail.com","qwerty777", listOf(),listOf(), listOf(), listOf(), Utils.UserType.CUSTOMER.name)

        userRepository.addUserLocalSource(user)

        val newUser = userRepository.checkLogin("gef@gmail.com","qwerty777",false).getOrNull() ?: UserUIState()

        Truth.assertThat(newUser).isEqualTo(user)

    }

    @Test
    fun loginUserRepositoryTest(): Unit = runTest(timeout = 30.seconds) {
        val user = UserUIState("1", "alex", "url", "230045320","gef@gmail.com","qwerty777", listOf(),listOf(), listOf(), listOf(), Utils.UserType.CUSTOMER.name)

         userRepository.login(user.userId,true)

        Truth.assertThat(sessionManager.isLoggedIn()).isTrue()
        Truth.assertThat(sessionManager.isRememberMeOn()).isTrue()
        Truth.assertThat(userRepository.getCurrentUser().getOrNull()).isEqualTo(user)

    }

    @Test
    fun signUpUserRepositoryTest(): Unit = runTest (timeout = 30.seconds){
        val user = UserUIState("1", "alex", "url", "230045320","gef@gmail.com","qwerty777", listOf(),listOf(), listOf(), listOf(), Utils.UserType.CUSTOMER.name)

        Truth.assertThat(sessionManager.isLoggedIn()).isFalse()
        Truth.assertThat(sessionManager.isRememberMeOn()).isFalse()
        Truth.assertThat(userRepository.getCurrentUser().getOrNull()).isNotEqualTo(user)

        userRepository.singUp(user,true)

        Truth.assertThat(sessionManager.isLoggedIn()).isTrue()
        Truth.assertThat(sessionManager.isRememberMeOn()).isTrue()
        Truth.assertThat(userRepository.getCurrentUser().getOrNull()).isEqualTo(user)
    }

    @Test
    fun signOutUserRepositoryTest(): Unit = runTest(timeout = 30.seconds) {
        val user = UserUIState("1", "alex", "url", "230045320","gef@gmail.com","qwerty777", listOf(),listOf(), listOf(), listOf(), Utils.UserType.CUSTOMER.name)

        userRepository.singUp(user,true)

        val q1 = sessionManager.isLoggedIn()
        val q2 = sessionManager.isRememberMeOn()
        val q3 = userRepository.getCurrentUser().getOrNull()

        Truth.assertThat(q1).isTrue()
        Truth.assertThat(q2).isTrue()
        Truth.assertThat(q3).isEqualTo(user)

        userRepository.singOut(user.userId)

        val q12 = sessionManager.isLoggedIn()
        val q22 = sessionManager.isRememberMeOn()
        val q32 = userRepository.getCurrentUser().getOrNull()

        Truth.assertThat(q12).isFalse()
        Truth.assertThat(q22).isFalse()
        Truth.assertThat(q32).isNotEqualTo(user)

    }

    @Test
    fun isRememberMeOnUserRepositoryTest(): Unit = runTest(timeout = 30.seconds)  {
        val user = UserUIState("1", "alex", "url", "230045320","gef@gmail.com","qwerty777", listOf(),listOf(), listOf(), listOf(), Utils.UserType.CUSTOMER.name)

        userRepository.login(user.userId,true)

        Truth.assertThat(sessionManager.isRememberMeOn()).isTrue()
    }

    @Test
    fun refreshDataUserRepositoryTest(): Unit = runTest(timeout = 30.seconds) {
        val user = UserUIState("1", "alex", "url", "230045320","gef@gmail.com","qwerty777", listOf(),listOf(), listOf(), listOf(), Utils.UserType.CUSTOMER.name)

        userRepository.singUp(user,true)

        Truth.assertThat(sessionManager.isLoggedIn()).isTrue()
        Truth.assertThat(sessionManager.isRememberMeOn()).isTrue()
        Truth.assertThat(userRepository.getCurrentUser()).isEqualTo(user)
        Truth.assertThat(userDao.getAll().getOrAwaitValue()).isNotEmpty()

        userRepository.refreshData()

        Truth.assertThat(sessionManager.isLoggedIn()).isFalse()
        Truth.assertThat(sessionManager.isRememberMeOn()).isFalse()
        Truth.assertThat(userRepository.getCurrentUser()).isNotEqualTo(user)
        Truth.assertThat(userDao.getAll().getOrAwaitValue()).isNotEmpty()

    }

    @Test
    fun refreshDataHardUserRepositoryTest(): Unit = runTest(timeout = 30.seconds) {
        val user = UserUIState("1", "alex", "url", "230045320","gef@gmail.com","qwerty777", listOf(),listOf(), listOf(), listOf(), Utils.UserType.CUSTOMER.name)

        val list = userDao.getAll().getOrAwaitValue()
        userRepository.singUp(user,true)

        val q1 = sessionManager.isLoggedIn()
        val q2 = sessionManager.isRememberMeOn()
        val q3 = userRepository.getCurrentUser().getOrNull()

        Truth.assertThat(q1).isTrue()
        Truth.assertThat(q2).isTrue()
        Truth.assertThat(q3).isEqualTo(user)
        Truth.assertThat(list).isNotEmpty()

        userRepository.hardRefreshData()

        val q12 = sessionManager.isLoggedIn()
        val q22 = sessionManager.isRememberMeOn()
        val q32 = userRepository.getCurrentUser().getOrNull()

        Truth.assertThat(q12).isFalse()
        Truth.assertThat(q22).isFalse()
        Truth.assertThat(q32).isNotEqualTo(user)
        Truth.assertThat(list).isEmpty()

    }



    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
        afterObserve: () -> Unit = {}
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(value: T) {
                data = value
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }
        this.observeForever(observer)
        try {
            afterObserve.invoke()
            if (!latch.await(time, timeUnit)) {
                throw TimeoutException("LiveData value was never set.")
            }
        } finally {
            this.removeObserver(observer)
        }
        @Suppress("UNCHECKED_CAST")
        return data as T
    }
}