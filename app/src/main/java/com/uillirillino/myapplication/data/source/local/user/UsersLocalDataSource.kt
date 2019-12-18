package com.uillirillino.myapplication.data.source.local.user

import com.uillirillino.myapplication.data.Result
import com.uillirillino.myapplication.data.User
import com.uillirillino.myapplication.data.source.data.source.UsersDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.uillirillino.myapplication.data.Result.Success

/**
 * Concrete implementation of a data source as a db.
 */
class UsersLocalDataSource internal constructor(
    private val usersDao: UsersDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UsersDataSource {

    override suspend fun getUser(userId: Int): Result<User> = withContext(ioDispatcher) {
        try {
            val user = usersDao.getUserById(userId)
            if (user != null) {
                return@withContext Success(user)
            } else {
                return@withContext Result.Error(Exception("User not found!"))
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }
}
