package com.uillirillino.myapplication.data.source.local.comment

import com.uillirillino.myapplication.data.Result
import com.uillirillino.myapplication.data.source.data.source.CommentsDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Concrete implementation of a data source as a db.
 */
class CommentsLocalDataSource internal constructor(
    private val commentsDao: CommentsDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CommentsDataSource {

    override suspend fun getCommentsCount(postId: Int): Result<Int> = withContext(ioDispatcher) {
        try {
            val postCount = commentsDao.getCommentsCount(postId)
            if (postCount != null) {
                return@withContext Result.Success(postCount)
            } else {
                return@withContext Result.Error(Exception("Comments not found!"))
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }
}
