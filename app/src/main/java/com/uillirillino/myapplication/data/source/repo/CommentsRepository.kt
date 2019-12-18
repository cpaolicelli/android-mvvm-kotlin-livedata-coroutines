package com.uillirillino.myapplication.data.source.repo

import com.uillirillino.myapplication.data.Result

/**
 * CommentsRepository
 */
interface CommentsRepository {

    suspend fun getCommentsCount(postId: Int, forceUpdate: Boolean) : Result<Int>
}