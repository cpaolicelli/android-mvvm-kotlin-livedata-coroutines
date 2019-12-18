package com.uillirillino.myapplication.data.source.data.source

import com.uillirillino.myapplication.data.Post
import com.uillirillino.myapplication.data.Result

/**
 * Main entry point for accessing posts data.
 */
interface PostsDataSource {

    suspend fun getPosts(): Result<List<Post>>

    suspend fun getPost(postId: Int): Result<Post>

    suspend fun savePost(post: Post)

    suspend fun deleteAllPosts()

    suspend fun deletePost(postId: Int)
}