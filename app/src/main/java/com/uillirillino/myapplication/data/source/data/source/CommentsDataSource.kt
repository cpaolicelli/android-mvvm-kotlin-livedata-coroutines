package com.uillirillino.myapplication.data.source.data.source

import com.uillirillino.myapplication.data.Result

/**
 * Main entry point for accessing comments data.
 */
interface CommentsDataSource {

    suspend fun getCommentsCount(postId: Int): Result<Int>
}