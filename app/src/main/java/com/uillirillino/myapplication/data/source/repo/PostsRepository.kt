package com.uillirillino.myapplication.data.source.repo

import com.uillirillino.myapplication.data.Post
import com.uillirillino.myapplication.data.Result

/**
 * PostsRepository
 */
interface PostsRepository {

    suspend fun getPosts(forceUpdate: Boolean = false): Result<List<Post>>

    suspend fun getPost(postId: Int, forceUpdate: Boolean = false): Result<Post>
}