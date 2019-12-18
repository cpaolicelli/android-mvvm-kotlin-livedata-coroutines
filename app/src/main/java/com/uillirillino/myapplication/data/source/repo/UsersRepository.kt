package com.uillirillino.myapplication.data.source.repo

import com.uillirillino.myapplication.data.Result
import com.uillirillino.myapplication.data.User

/**
 * UsersRepository
 */
interface UsersRepository {

    suspend fun getAuthor(userId: Int, forceUpdate: Boolean) : Result<User>
}