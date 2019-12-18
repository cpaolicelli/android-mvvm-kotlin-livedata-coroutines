package com.uillirillino.myapplication.network

import com.uillirillino.myapplication.data.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * The interface which provides methods to get result of webservices
 */
interface PostApi {
    /**
     * Get the list of the pots from the API
     */
    @GET("/posts")
    suspend fun getPosts(): Response<List<Post>>

    /**
     * Get a post with id from the API
     */
    @GET("/posts")
    suspend fun getPost(@Query("id") id: Int): Response<List<Post>>
}