package com.uillirillino.myapplication.source

import androidx.annotation.VisibleForTesting
import com.uillirillino.myapplication.data.Post
import com.uillirillino.myapplication.data.source.repo.PostsRepository
import java.util.LinkedHashMap
import com.uillirillino.myapplication.data.Result

/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
class FakeRepository : PostsRepository {

    var postsServiceData: LinkedHashMap<Int, Post> = LinkedHashMap()

    private var shouldReturnError = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getPost(postId: Int, forceUpdate: Boolean): Result<Post> {
        if (shouldReturnError) {
            return Result.Error(Exception("Test exception"))
        }
        postsServiceData[postId]?.let {
            return Result.Success(it)
        }
        return Result.Error(Exception("Could not find post"))
    }

    override suspend fun getPosts(forceUpdate: Boolean): Result<List<Post>> {
        if (shouldReturnError) {
            return Result.Error(Exception("Test exception"))
        }
        return Result.Success(postsServiceData.values.toList())
    }

    @VisibleForTesting
    fun addPosts(vararg posts: Post) {
        for (post in posts) {
            postsServiceData[post.id] = post
        }
    }
}
