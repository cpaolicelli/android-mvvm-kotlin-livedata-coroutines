package com.uillirillino.myapplication

import com.uillirillino.myapplication.data.Post
import com.uillirillino.myapplication.data.source.data.source.PostsDataSource
import java.util.LinkedHashMap
import com.uillirillino.myapplication.data.Result
import java.lang.Exception

/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
object FakePostsRemoteDataSource : PostsDataSource {

    private var POSTS_SERVICE_DATA: LinkedHashMap<Int, Post> = LinkedHashMap()

    override suspend fun getPost(postId: Int): Result<Post> {
        POSTS_SERVICE_DATA[postId]?.let {
            return Result.Success(it)
        }

        return Result.Error(Exception("Could not find post"))
    }

    override suspend fun deleteAllPosts() {
        POSTS_SERVICE_DATA.clear()
    }

    override suspend fun deletePost(postId: Int) {
        POSTS_SERVICE_DATA.remove(postId)
    }

    override suspend fun getPosts(): Result<List<Post>> {
        return Result.Success(POSTS_SERVICE_DATA.values.toList())
    }

    override suspend fun savePost(post: Post) {
        POSTS_SERVICE_DATA[post.id] = post
    }
}
