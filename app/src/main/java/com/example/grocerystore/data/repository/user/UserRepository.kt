package com.example.grocerystore.data.repository.user

import android.util.Log
import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.data.source.local.user.UserLocalDataSource
import com.example.grocerystore.data.source.remove.firebase.UserRemoteDataSource
import com.example.grocerystore.services.SessionManager
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val remoteSource: UserRemoteDataSource,
    private val localSource: UserLocalDataSource,
    private val sessionManager: SessionManager,
) : UserRepoInterface {

    private val TAG = "UserRepository"
    private val TAGLIST = "UserList"

//    private fun usersCollectionRef() = firebaseDb.collection(USERS_COLLECTION)
//    private fun emailsCollectionRef() = firebaseDb.collection(USERS_COLLECTION).document(EMAIL_MOBILE_DOC)


    override fun getCurrentUserFlow(): Flow<UserUIState?> {
        Log.d(TAG, "getCurrentUserFlow")
        return localSource.getUserByIdFlow(sessionManager.getUserId())
    }

    override suspend fun getCurrentUser(): Result<UserUIState?> {
        Log.d(TAG, "getCurrentUser")

        val user = localSource.getUserById(sessionManager.getUserId())
        Log.d(TAG, "    getCurrentUser: user = ${user.getOrNull()}")
        return user
    }






    override fun getUserByIdFlow(id: String, hard: Boolean): Flow<UserUIState?> {
        Log.d(TAG, "getUserByIdFlow : id = $id, hard = $hard")
        return localSource.getUserByIdFlow(id)
    }

    override suspend fun getUserById(id: String, hard: Boolean): Result<UserUIState?> {
        Log.d(TAG, "getUserById : id = $id, hard = $hard")
        return localSource.getUserById(id)
    }








    override suspend fun hardRefreshData(): Result<Boolean?> {
        Log.d(TAG, "hardRefreshData")
        sessionManager.deleteLoginSession()
        return localSource.clearUser()
    }

    override suspend fun refreshData(): Result<Boolean?> {
        Log.d(TAG, "refreshData")
        sessionManager.deleteLoginSession()
        return Result.success(true)
    }







    override suspend fun checkLogin(email: String, password: String, hard: Boolean): Result<UserUIState?> {
        Log.d(TAG, "checkLogin : email = $email, password = $password, hard = $hard")
        val oldUser = localSource.getUserByEmail(email)
        Log.d(TAG, "    checkLogin : oldUser = ${oldUser.getOrNull()}")
        return oldUser
    }


    override suspend fun login(userId: String, rememberMe: Boolean): Result<Boolean?> {
        Log.d(TAG, "login : userId = $userId, rememberMe = $rememberMe")

        if (userId != ""){
            sessionManager.updateDataLoginSession(userId, isLogin = true, isRememberMe = rememberMe)

            return Result.success(true)

        }else{ return Result.success(false) }
    }





    override suspend fun singUp(userData: UserUIState, rememberMe: Boolean): Result<Boolean?> {
        Log.d(TAG, "singOut : userData = $userData, rememberMe = $rememberMe")
        addUserLocalSource(userData)

    //        Log.d(TAG, " login: Updating userdata on Remote Source")
    //        remoteSource.addUser(userData)
    //        remoteSource.updateEmailsAndMobiles(userData.email, userData.mobile)

        sessionManager.updateDataLoginSession(userData.userId, isLogin = true, isRememberMe = rememberMe)

        return Result.success(true)
    }


    override suspend fun singOut(userId: String): Result<Boolean?> {
        Log.d(TAG, "singOut : userId = $userId")

        if (userId != "-1" && userId != ""){
            sessionManager.updateDataLoginSession(userId = "-1", isLogin = false)
            return Result.success(true)

        }else{ return Result.success(false) }
    }





    override suspend fun addUserLocalSource(userData: UserUIState): Result<Boolean?> {
        Log.d(TAG, "addUserLocalSource : userData = $userData")
        Log.d(TAGLIST, "addUserLocalSource : list = ${localSource.getAllUsers()}")

        val res = localSource.addUser(userData)
        Log.d(TAGLIST, "    addUserLocalSource : list = ${localSource.getAllUsers()}")
        Log.d(TAG, "    addUserLocalSource : result = ${res.getOrNull()}")
        return res
    }

    override suspend fun deleteUserLocalSource(userId: String): Result<Boolean?> {
        Log.d(TAG, "deleteUserLocalSource : userId = $userId")
        Log.d(TAGLIST, "deleteUserLocalSource : list = ${localSource.getAllUsers()}")

        if (userId != "" && userId != "-1") {

            val oldUser = getUserById(userId)
            Log.d(TAG, "    deleteUserLocalSource : user = $oldUser")

            if (oldUser.isSuccess && oldUser.getOrNull() != null) {
                val res = localSource.deleteUser(userId)
                Log.d(TAGLIST, "    deleteUserLocalSource : list = ${localSource.getAllUsers()}")
                Log.d(TAG, "    deleteUserLocalSource : result = ${res.getOrNull()}")
                return res

            }else{ return Result.success(false) }

        }else{ return Result.success(false) }
    }








    suspend fun likeProduct(productId: String, userId: String): Result<Boolean> {
        Log.d(TAG, "likeProduct : productId = $productId, userId = $userId")

        val oldUser = getUserById(userId)
        Log.d(TAG, "    likeProduct : oldUser - ${oldUser.getOrNull()}")
        if(oldUser.isSuccess && oldUser.getOrNull() != null) {

            val newListLikes = oldUser.getOrNull()!!.likes.toMutableList()
            Log.d(TAG, "    likeProduct : newListLikes - $newListLikes")
            newListLikes.add(productId)

            localSource.updateUser(oldUser.getOrNull()!!.copy(likes = newListLikes))

            Log.d(TAG, "    likeProduct : newListLikes = $newListLikes")
            return Result.success(true)
        }else{ return Result.success(false) }
    }


    suspend fun dislikeProduct(productId: String, userId: String): Result<Boolean> {
        Log.d(TAG, "dislikeProduct : productId = $productId, userId = $userId")

        val oldUser = getUserById(userId)
        Log.d(TAG, "    dislikeProduct : oldUser - ${oldUser.getOrNull()}")
        if(oldUser.isSuccess && oldUser.getOrNull() != null) {

            val newListLikes = oldUser.getOrNull()!!.likes.toMutableList()
            Log.d(TAG, "    dislikeProduct : newListLikes - $newListLikes")

            newListLikes.remove(productId)
            localSource.updateUser(oldUser.getOrNull()!!.copy(likes = newListLikes))

            Log.d(TAG, "    dislikeProduct : newListLikes = $newListLikes")
            return Result.success(true)

        }else{ return Result.success(false) }
    }











    override suspend fun addCartInBasketOfUser(newItem: CartUIState, userId : String): Result<Boolean?> {
        Log.d(TAG, "addCartInBasketOfUser : newItem = $newItem , userId = $userId")
        Log.d(TAGLIST, "addCartInBasketOfUser : list = ${localSource.getAllUsers()}")

        val oldUser = getUserById(userId)
        Log.d(TAG, "    addCartInBasketOfUser : oldUser - ${oldUser.getOrNull()}")
        if(oldUser.isSuccess && oldUser.getOrNull() != null) {

            val carts = oldUser.getOrNull()!!.cart.toMutableList()

            //test is item already in list if yes then increase quantity of item
            var containItem = true
            for ((count,item) in carts.withIndex()){
                if( item.itemData.id == newItem.itemData.id ){
                    carts[count].quantity++
                    containItem = false
                    break
                }
            }
            if(containItem) {
                carts.add(newItem)
            }


            val res = localSource.updateUser(oldUser.getOrNull()!!.copy(cart = carts))
            Log.d(TAGLIST, "    addCartInBasketOfUser : list = ${localSource.getAllUsers()}")
            Log.d(TAG, "    addCartInBasketOfUser : result = $res")
            return res

        }else{ return Result.success(false) }
    }

    override suspend fun addCartInBasketOfCurrentUser(newItem: CartUIState): Result<Boolean?> {
        Log.d(TAG, "addCartInBasketOfCurrentUser : newItem = $newItem , userId = ${sessionManager.getUserId()}")
        return addCartInBasketOfUser(newItem,sessionManager.getUserId())
    }











    override suspend fun deleteCartInBasketOfUser(itemId: String, userId : String): Result<Boolean?>  {
        Log.d(TAGLIST, "deleteCartInBasketOfUser : list = ${localSource.getAllUsers()}")
        Log.d(TAG, "deleteCartInBasketOfUser : itemId = $itemId , userId = $userId")

        val oldUser = getUserById(userId)
        Log.d(TAG, "    deleteCartInBasketOfUser : oldUser - ${oldUser.getOrNull()}")
        if(oldUser.isSuccess && oldUser.getOrNull() != null) {

            val newList = oldUser.getOrNull()!!.cart.toMutableList()
            for (item in newList){
                if( item.cartId == itemId){
                    newList.remove(item)
                    break
                }
            }

            val res = localSource.updateUser(oldUser.getOrNull()!!.copy(cart = newList))
            Log.d(TAGLIST, "    deleteCartInBasketOfUser : list = ${localSource.getAllUsers()}")
            Log.d(TAG, "    deleteCartInBasketOfUser : result = $res")
            return res

        }else{ return Result.success(false) }
    }

    override suspend fun deleteCartInBasketOfCurrentUser(itemId: String): Result<Boolean?>  {
        Log.d(TAG, "deleteCartInBasketOfCurrentUser : itemId = $itemId , userId = ${sessionManager.getUserId()}")
        return deleteCartInBasketOfUser(itemId,sessionManager.getUserId())
    }








    override suspend fun updateCartInBasketOfUser(updatedItem: CartUIState, userId : String): Result<Boolean?>{
        Log.d(TAGLIST, "updateCartInBasketOfUser : list = ${localSource.getAllUsers()}")
        Log.d(TAG, "updateCartInBasketOfUser : updatedItem = $updatedItem , userId = $userId")

        val oldUser = getUserById(userId)
        Log.d(TAG, "    updateCartInBasketOfUser : oldUser - ${oldUser.getOrNull()}")
        if(oldUser.isSuccess && oldUser.getOrNull() != null) {

            val newList = oldUser.getOrNull()!!.cart.toMutableList()
            for (item in newList){
                if( item.cartId == updatedItem.cartId){
                    newList.remove(item)
                    newList.add(updatedItem)
                    break
                }
            }

            val res = localSource.updateUser(oldUser.getOrNull()!!.copy(cart = newList))
            Log.d(TAGLIST, "    updateCartInBasketOfUser : list = ${localSource.getAllUsers()}")
            Log.d(TAG, "    updateCartInBasketOfUser : newList - $newList")
            return res

        }else{ return Result.success(false) }
    }


    override suspend fun updateCartInBasketOfCurrentUser(updatedItem: CartUIState): Result<Boolean?>{
        Log.d(TAG, "updateCartInBasketOfCurrentUser : updatedItem = $updatedItem , userId = ${sessionManager.getUserId()}")
        return updateCartInBasketOfUser(updatedItem,sessionManager.getUserId())
    }


}