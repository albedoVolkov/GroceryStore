package com.example.grocerystore.data.repository.user

import androidx.annotation.VisibleForTesting
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.grocerystore.GroceryStoreApplication
import com.example.grocerystore.ShoppingAppSessionManager
import com.example.grocerystore.data.helpers.Result
import com.example.grocerystore.data.helpers.UIstates.item.AddressUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.data.source.local.GroceryStoreDatabase
import com.example.grocerystore.data.source.local.user.UserDao
import com.google.android.gms.tasks.Tasks
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.coroutines.CoroutineContext
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
        dataSource = GroceryStoreDatabase.getDataBase(context = ApplicationProvider.getApplicationContext())//Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), GroceryStoreDatabase::class.java).allowMainThreadQueries().build()

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
        val user = UserUIState("1", "1", "1", "1","1", listOf(), AddressUIState("1","1","1","1","1","1","1","1","1","1"), listOf(), listOf(),"1")
        launch {
            userRepository.addUserLocalSource(user)

            val userList = userDao.getAll().getOrAwaitValue()
            Truth.assertThat(userList).contains(user)
            Truth.assertThat(userList.size).isEqualTo(1)
        }

    }

    @Test
    fun deleteUserFromUserRepositoryTest(): Unit = runTest(timeout = 30.seconds) {
        val user = UserUIState("2", "1", "1",
            "modwert404@gmail.com","1",
            listOf(),
            AddressUIState("1","1","1","1","1","1","1","1","1","1"),
            listOf(), listOf(),"1")


        userRepository.addUserLocalSource(user)

        Truth.assertThat(userDao.getAll().getOrAwaitValue()).contains(user)

        userRepository.deleteUserLocalSource("2")

        Truth.assertThat(userDao.getAll().getOrAwaitValue()).doesNotContain(user)

    }

    @Test
    fun getByIdUserFromUserRepositoryTest(): Unit = runTest(timeout = 30.seconds)  {
        val user = UserUIState("2", "1", "1",
            "modwert404@gmail.com","1",
            listOf(),
            AddressUIState("1","1","1","1","1","1","1","1","1","1"),
            listOf(), listOf(),"1")

        userRepository.addUserLocalSource(user)
        val newUserNotNull = (userRepository.getUserById("2") as Result.Success<UserUIState?>).data
            ?: UserUIState("-1", "-1", "-1", "-1","-1", listOf(), AddressUIState("-1","-1","-1","-1","-1","-1","-1","-1","-1","-1"), listOf(), listOf(),"-1")

        Truth.assertThat(newUserNotNull).isEqualTo(user)

    }

    @Test
    fun checkLoginRepositoryTest(): Unit = runTest(timeout = 30.seconds) {
        val user = UserUIState("2", "1", "1",
            "modwert404@gmail.com","qwerty777",
            listOf(),
            AddressUIState("1","1","1","1","1","1","1","1","1","1"),
            listOf(), listOf(),"1")

        userRepository.addUserLocalSource(user)

        val newUser = userRepository.checkLogin("modwert404@gmail.com","qwerty777",false)

        val newUserNotNull = (newUser as Result.Success<UserUIState?>).data
            ?: UserUIState("-1", "-1", "-1", "-1","-1", listOf(), AddressUIState("-1","-1","-1","-1","-1","-1","-1","-1","-1","-1"), listOf(), listOf(),"-1")

        Truth.assertThat(newUserNotNull).isEqualTo(user)

    }

    @Test
    fun loginUserRepositoryTest(): Unit = runTest(timeout = 30.seconds) {
        val user = UserUIState("2", "1", "1",
            "modwert404@gmail.com","1",
            listOf(),
            AddressUIState("1","1","1","1","1","1","1","1","1","1"),
            listOf(), listOf(),"1")

         userRepository.login(user,true)

        Truth.assertThat(sessionManager.isLoggedIn()).isTrue()
        Truth.assertThat(sessionManager.isRememberMeOn()).isTrue()
        Truth.assertThat(sessionManager.getUserData()).isEqualTo(user)

    }

    @Test
    fun signUpUserRepositoryTest(): Unit = runTest (timeout = 30.seconds){
        val user = UserUIState("2", "1", "1",
            "modwert404@gmail.com","1",
            listOf(),
            AddressUIState("1","1","1","1","1","1","1","1","1","1"),
            listOf(), listOf(),"1")

        Truth.assertThat(sessionManager.isLoggedIn()).isFalse()
        Truth.assertThat(sessionManager.isRememberMeOn()).isFalse()
        Truth.assertThat(sessionManager.getUserData()).isNotEqualTo(user)

        userRepository.singUp(user,true)

        Truth.assertThat(sessionManager.isLoggedIn()).isTrue()
        Truth.assertThat(sessionManager.isRememberMeOn()).isTrue()
        Truth.assertThat(sessionManager.getUserData()).isEqualTo(user)

        val oldUser = userDao.getAll().getOrAwaitValue()

        Truth.assertThat(oldUser).contains(user)
    }

    @Test
    fun signOutUserRepositoryTest(): Unit = runTest(timeout = 30.seconds) {
        val user = UserUIState("2", "1", "1",
            "modwert404@gmail.com","1",
            listOf(),
            AddressUIState("1","1","1","1","1","1","1","1","1","1"),
            listOf(), listOf(),"1")

        userRepository.singUp(user,true)

        val q1 = sessionManager.isLoggedIn()
        val q2 = sessionManager.isRememberMeOn()
        val q3 = sessionManager.getUserData()

        Truth.assertThat(q1).isTrue()
        Truth.assertThat(q2).isTrue()
        Truth.assertThat(q3).isEqualTo(user)

        userRepository.singOut("2")

        val q1_2 = sessionManager.isLoggedIn()
        val q2_2 = sessionManager.isRememberMeOn()
        val q3_2 = sessionManager.getUserData()

        Truth.assertThat(q1_2).isFalse()
        Truth.assertThat(q2_2).isFalse()
        Truth.assertThat(q3_2).isNotEqualTo(user)

    }

    @Test
    fun isRememberMeOnUserRepositoryTest(): Unit = runTest(timeout = 30.seconds)  {
        val user = UserUIState("2", "1", "1",
            "modwert404@gmail.com","1",
            listOf(),
            AddressUIState("1","1","1","1","1","1","1","1","1","1"),
            listOf(), listOf(),"1")

        userRepository.login(user,true)

        sessionManager.isRememberMeOn()

        Truth.assertThat(sessionManager.isRememberMeOn()).isTrue()
    }

    @Test
    fun refreshDataUserRepositoryTest(): Unit = runTest(timeout = 30.seconds) {
        val user = UserUIState("2", "1", "1",
            "modwert404@gmail.com","1",
            listOf(),
            AddressUIState("1","1","1","1","1","1","1","1","1","1"),
            listOf(), listOf(),"1")

        userRepository.singUp(user,true)

        Truth.assertThat(sessionManager.isLoggedIn()).isTrue()
        Truth.assertThat(sessionManager.isRememberMeOn()).isTrue()
        Truth.assertThat(sessionManager.getUserData()).isEqualTo(user)
        Truth.assertThat(userDao.getAll().getOrAwaitValue()).isNotEmpty()

        userRepository.refreshData()

        Truth.assertThat(sessionManager.isLoggedIn()).isFalse()
        Truth.assertThat(sessionManager.isRememberMeOn()).isFalse()
        Truth.assertThat(sessionManager.getUserData()).isNotEqualTo(user)
        Truth.assertThat(userDao.getAll().getOrAwaitValue()).isNotEmpty()

    }

    @Test
    fun refreshDataHardUserRepositoryTest(): Unit = runTest(timeout = 30.seconds) {
        val user = UserUIState("2", "1", "1",
            "modwert404@gmail.com","1",
            listOf(),
            AddressUIState("1","1","1","1","1","1","1","1","1","1"),
            listOf(), listOf(),"1")

        val list = userDao.getAll().getOrAwaitValue()
        userRepository.singUp(user,true)

        val q1 = sessionManager.isLoggedIn()
        val q2 = sessionManager.isRememberMeOn()
        val q3 = sessionManager.getUserData()

        Truth.assertThat(q1).isTrue()
        Truth.assertThat(q2).isTrue()
        Truth.assertThat(q3).isEqualTo(user)
        Truth.assertThat(list).isNotEmpty()

        userRepository.hardRefreshData()

        val q1_2 = sessionManager.isLoggedIn()
        val q2_2 = sessionManager.isRememberMeOn()
        val q3_2 = sessionManager.getUserData()

        Truth.assertThat(q1_2).isFalse()
        Truth.assertThat(q2_2).isFalse()
        Truth.assertThat(q3_2).isNotEqualTo(user)
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