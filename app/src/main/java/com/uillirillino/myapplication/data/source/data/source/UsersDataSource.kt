package com.uillirillino.myapplication.data.source.data.source

import com.uillirillino.myapplication.data.Result
import com.uillirillino.myapplication.data.User

/**
 * Main entry point for accessing users data.
 */
interface UsersDataSource {

    suspend fun getUser(userId: Int): Result<User>
}