package com.uillirillino.myapplication

import com.uillirillino.myapplication.data.Result
import com.uillirillino.myapplication.data.User
import com.uillirillino.myapplication.data.source.data.source.UsersDataSource
import java.util.*

/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
object FakeUsersRemoteDataSource : UsersDataSource {

    private var USERS_SERVICE_DATA: LinkedHashMap<Int, User> = LinkedHashMap()

    override suspend fun getUser(userId: Int): Result<User> {
        USERS_SERVICE_DATA[userId]?.let {
            return Result.Success(it)
        }

        return Result.Error(Exception("Could not find user"))
    }
}
