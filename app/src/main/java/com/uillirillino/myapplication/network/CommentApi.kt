package com.uillirillino.myapplication.network

import com.uillirillino.myapplication.data.Comment
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * The interface which provides methods to get result of webservices
 */
interface CommentApi {
    /**
     * Get the list of the pots from the API
     */
    @GET("/comments")
    suspend fun getComments(@Query("postId") postId: Int): Response<List<Comment>>
}