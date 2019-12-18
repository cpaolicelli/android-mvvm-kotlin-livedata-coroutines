package com.uillirillino.myapplication.data.source.repo

import com.uillirillino.myapplication.data.Result
import com.uillirillino.myapplication.data.source.data.source.CommentsDataSource
import com.uillirillino.myapplication.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Concrete implementation to load comments from the data sources into a cache.
 *
 * To simplify the sample, this repository only uses the local data source only if the remote
 * data source fails. Remote is the source of truth.
 */
class DefaultCommentsRepository(
    private val commentsRemoteDataSource: CommentsDataSource,
    private val commentsLocalDataSource: CommentsDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CommentsRepository {

    override suspend fun getCommentsCount(postId: Int, forceUpdate: Boolean): Result<Int> {

        wrapEspressoIdlingResource {
            return withContext(ioDispatcher) {
                // Respond immediately with cache if available
                return@withContext fetchPostFromRemoteOrLocal(postId, forceUpdate)
            }
        }
    }

    private suspend fun fetchPostFromRemoteOrLocal(
        postId: Int,
        forceUpdate: Boolean
    ): Result<Int> {
        // Remote first
        when (val remoteCount = commentsRemoteDataSource.getCommentsCount(postId)) {
            is Error -> Timber.w("Remote data source fetch failed")
            is Result.Success -> {
                return remoteCount
            }
            else -> throw IllegalStateException()
        }

        // Don't read from local if it's forced
        if (forceUpdate) {
            return Result.Error(Exception("Refresh failed"))
        }

        // Local if remote fails
        val localCount = commentsLocalDataSource.getCommentsCount(postId)
        if (localCount is Result.Success) return localCount
        return Result.Error(Exception("Error fetching from remote and local"))
    }
}
