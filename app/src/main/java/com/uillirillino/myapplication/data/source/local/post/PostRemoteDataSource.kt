package com.uillirillino.myapplication.data.source.local.post

import android.content.res.Resources
import com.uillirillino.myapplication.data.Post
import com.uillirillino.myapplication.data.Result
import com.uillirillino.myapplication.data.source.data.source.PostsDataSource
import com.uillirillino.myapplication.network.PostApi
import com.uillirillino.myapplication.util.BaseRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostRemoteDataSource internal constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PostsDataSource, BaseRemoteDataSource() {

    @Inject
    lateinit var postApi: PostApi

    override suspend fun getPosts(): Result<List<Post>> = withContext(ioDispatcher) {
        return@withContext try {
            val response = postApi.getPosts()
            val body = response.body()

            if (body != null) {
                Result.Success(body)
            } else {
                Result.Error(Resources.NotFoundException("Posts not found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getPost(postId: Int): Result<Post> = withContext(ioDispatcher) {
        return@withContext try {
            val response = postApi.getPost(postId).body()

            if (response!= null) {
                Result.Success(response[0])
            } else {
                Result.Error(Resources.NotFoundException("Post not found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun savePost(post: Post) {
        return
    }

    override suspend fun deleteAllPosts() {
        return
    }

    override suspend fun deletePost(postId: Int) {
        return
    }
}