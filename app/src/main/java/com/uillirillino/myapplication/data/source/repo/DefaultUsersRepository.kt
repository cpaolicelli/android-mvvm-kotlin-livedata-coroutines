package com.uillirillino.myapplication.data.source.repo

import com.uillirillino.myapplication.data.Result
import com.uillirillino.myapplication.data.User
import com.uillirillino.myapplication.data.source.data.source.UsersDataSource
import com.uillirillino.myapplication.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Concrete implementation to load users from the data sources into a cache.
 *
 * To simplify the sample, this repository only uses the local data source only if the remote
 * data source fails. Remote is the source of truth.
 */
class DefaultUsersRepository(
    private val usersRemoteDataSource: UsersDataSource,
    private val usersLocalDataSource: UsersDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UsersRepository {

    override suspend fun getAuthor(userId: Int, forceUpdate: Boolean): Result<User> {
        wrapEspressoIdlingResource {
            return withContext(ioDispatcher) {
                // Respond immediately with cache if available
                return@withContext fetchUserFromRemoteOrLocal(userId, forceUpdate)
            }
        }
    }

    private suspend fun fetchUserFromRemoteOrLocal(
        userId: Int,
        forceUpdate: Boolean
    ): Result<User> {
        // Remote first
        when (val author = usersRemoteDataSource.getUser(userId)) {
            is Error -> Timber.w("Remote data source fetch failed")
            is Result.Success -> {
                return author
            }
            else -> throw IllegalStateException()
        }

        // Don't read from local if it's forced
        if (forceUpdate) {
            return Result.Error(Exception("Refresh failed"))
        }

        // Local if remote fails
        val localCount = usersLocalDataSource.getUser(userId)
        if (localCount is Result.Success) return localCount
        return Result.Error(Exception("Error fetching from remote and local"))
    }

}
