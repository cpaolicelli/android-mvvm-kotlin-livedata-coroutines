package com.uillirillino.myapplication.data.source.local.comment

import android.content.res.Resources
import com.uillirillino.myapplication.data.Result
import com.uillirillino.myapplication.data.source.data.source.CommentsDataSource
import com.uillirillino.myapplication.network.CommentApi
import com.uillirillino.myapplication.util.BaseRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CommentRemoteDataSource internal constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CommentsDataSource, BaseRemoteDataSource() {

    @Inject
    lateinit var commentApi: CommentApi

    override suspend fun getCommentsCount(postId: Int): Result<Int> = withContext(ioDispatcher) {
        return@withContext try {
            val response = commentApi.getComments(postId).body()

            if (response != null) {
                Result.Success(response.size)
            } else {
                Result.Error(Resources.NotFoundException("Comments not found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}