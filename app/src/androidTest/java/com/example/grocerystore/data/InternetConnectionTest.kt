package com.example.grocerystore.data


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class InternetConnectionTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


   // private lateinit var internetConnection: InternetConnection


    @Before
    fun setup(){
        //internetConnection = InternetConnection(ApplicationProvider.getApplicationContext())
    }

    @After
    fun closeAll(){
        //internetConnection
    }




//
//    @Test
//    fun insertUserInUserRepositoryTest1(): Unit = runTest{
//            val connection = internetConnection.isOnline() as Status.Available
//            Truth.assertThat(connection.data).isEqualTo("NetworkCapabilities.TRANSPORT_ETHERNET")
//    }
//
//    @Test
//    fun insertUserInUserRepositoryTest2(): Unit = runTest{
//        val connection = internetConnection.isOnline() as Status.Available
//        Truth.assertThat(connection.data).isEqualTo("NetworkCapabilities.TRANSPORT_WIFI")
//    }
//
//    @Test
//    fun insertUserInUserRepositoryTest3(): Unit = runTest{
//        val connection = internetConnection.isOnline() as Status.Available
//        Truth.assertThat(connection.data).isEqualTo("NetworkCapabilities.TRANSPORT_CELLULAR")
//    }





}