package com.uillirillino.myapplication.network

import com.uillirillino.myapplication.data.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * The interface which provides methods to get result of webservices
 */
interface UserApi {

    /**
     * Get the list of the pots from the API
     */
    @GET("/users")
    suspend fun getUser(@Query("id") id: Int): Response<List<User>>
}