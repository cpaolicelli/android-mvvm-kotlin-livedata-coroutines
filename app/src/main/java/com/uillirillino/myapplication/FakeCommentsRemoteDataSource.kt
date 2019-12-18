package com.uillirillino.myapplication

import com.uillirillino.myapplication.data.Result
import com.uillirillino.myapplication.data.source.data.source.CommentsDataSource

/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
object FakeCommentsRemoteDataSource : CommentsDataSource {

    private var POSTS_SERVICE_DATA: Int = 0

    override suspend fun getCommentsCount(postId: Int): Result<Int> {
        POSTS_SERVICE_DATA.let {
            return Result.Success(it)
        }
    }
}
