package com.uillirillino.myapplication.data.source.local.user

import android.content.res.Resources
import com.uillirillino.myapplication.data.Result
import com.uillirillino.myapplication.data.User
import com.uillirillino.myapplication.data.source.data.source.UsersDataSource
import com.uillirillino.myapplication.network.UserApi
import com.uillirillino.myapplication.util.BaseRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRemoteDataSource internal constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UsersDataSource, BaseRemoteDataSource() {

    @Inject
    lateinit var userApi: UserApi

    override suspend fun getUser(userId: Int): Result<User> = withContext(ioDispatcher) {
        return@withContext try {
            val response = userApi.getUser(userId).body()

            if (response!= null) {
                Result.Success(response[0])
            } else {
                Result.Error(Resources.NotFoundException("User not found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}