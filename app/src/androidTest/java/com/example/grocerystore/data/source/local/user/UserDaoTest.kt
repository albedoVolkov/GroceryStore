package com.example.grocerystore.data.source.local.user

import androidx.annotation.VisibleForTesting
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.grocerystore.data.helpers.UIstates.item.AddressUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.data.source.local.GroceryStoreDatabase
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class UserDaoTest {
//C:\Albert\programming\projects\GroceryStore\app\build\reports\androidTests\connected\debug
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    private lateinit var dataSource: GroceryStoreDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setup(){
        dataSource = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GroceryStoreDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userDao = dataSource.userDao()
    }

    @After
    fun tearDataSource(){
        dataSource.close()
    }

    @Test
    fun insertUserInDataSourceTest(): Unit = runBlockingTest {
        val user = UserUIState("1", "1", "1",
            "1","1",
            listOf(),
            AddressUIState("1","1","1","1","1","1","1","1","1","1"),
            listOf(), listOf(),"1")

        userDao.insert(user)
        val userList = userDao.getAll().getOrAwaitValue()
         assertThat(userList).contains(user)

    }

    @Test
    fun getByEmailUserFromDataSourceTest(): Unit = runBlockingTest {
        val user = UserUIState("2", "1", "1",
            "modwert404@gmail.com","1",
            listOf(),
            AddressUIState("1","1","1","1","1","1","1","1","1","1"),
            listOf(), listOf(),"1")

        userDao.insert(user)

        val newUser = userDao.getByEmail("modwert404@gmail.com")
        val oldUser = userDao.getAll().getOrAwaitValue()[0]

        assertThat(oldUser).isEqualTo(newUser)

    }

    @Test
    fun getByIdUserFromDataSourceTest(): Unit = runBlockingTest {
        val user = UserUIState("2", "1", "1",
            "modwert404@gmail.com","1",
            listOf(),
            AddressUIState("1","1","1","1","1","1","1","1","1","1"),
            listOf(), listOf(),"1")

        val user2 = UserUIState("3", "1", "1",
            "modwert404@gmail.com","1",
            listOf(),
            AddressUIState("1","1","1","1","1","1","1","1","1","1"),
            listOf(), listOf(),"1")

        userDao.insert(user)

        val newUser = userDao.getById("2")
        val oldUser = userDao.getAll().getOrAwaitValue()[0]

        assertThat(oldUser).isEqualTo(newUser)

    }

    @Test
    fun deleteUserFromDataSourceTest(): Unit = runBlockingTest {
        val user = UserUIState("2", "1", "1",
            "modwert404@gmail.com","1",
            listOf(),
            AddressUIState("1","1","1","1","1","1","1","1","1","1"),
            listOf(), listOf(),"1")

        userDao.insert(user)
        userDao.delete("2")

        val list = userDao.getAll().getOrAwaitValue()

        assertThat(list).doesNotContain(user)

    }

    @Test
    fun updateUserFromDataSourceTest(): Unit = runBlockingTest {
        val user = UserUIState("2", "1", "1",
            "modwert404@gmail.com","1",
            listOf(),
            AddressUIState("1","1","1","1","1","1","1","1","1","1"),
            listOf(), listOf(),"1")

        userDao.insert(user)
        val oldUser = userDao.getById("2")
        userDao.update(user.copy(name = "3"))
        val newUser = userDao.getById("2")

        assertThat(oldUser).isNotEqualTo(newUser)

    }

    @Test
    fun clearUserInDataSourceTest(): Unit = runBlockingTest {
        val user = UserUIState("2", "1", "1",
            "modwert404@gmail.com","1",
            listOf(),
            AddressUIState("1","1","1","1","1","1","1","1","1","1"),
            listOf(), listOf(),"1")

        val user2 = UserUIState("3", "1", "1",
            "modwert404@gmail.com","1",
            listOf(),
            AddressUIState("1","1","1","1","1","1","1","1","1","1"),
            listOf(), listOf(),"1")

        val user3 = UserUIState("4", "1", "1",
            "modwert404@gmail.com","1",
            listOf(),
            AddressUIState("1","1","1","1","1","1","1","1","1","1"),
            listOf(), listOf(),"1")

        userDao.insert(user)
        userDao.insert(user2)
        userDao.insert(user3)
        userDao.clear()

        val list = userDao.getAll().getOrAwaitValue()

        assertThat(list).isEmpty()

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