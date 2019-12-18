package com.uillirillino.myapplication.data.source.repo

import com.uillirillino.myapplication.data.Post
import com.uillirillino.myapplication.data.source.data.source.PostsDataSource
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import com.uillirillino.myapplication.data.Result.Success
import com.uillirillino.myapplication.data.Result
import com.uillirillino.myapplication.util.EspressoIdlingResource
import com.uillirillino.myapplication.util.wrapEspressoIdlingResource

/**
 * Concrete implementation to load posts from the data sources into a cache.
 *
 * To simplify the sample, this repository only uses the local data source only if the remote
 * data source fails. Remote is the source of truth.
 */
class DefaultPostsRepository(
    private val postsRemoteDataSource: PostsDataSource,
    private val postsLocalDataSource: PostsDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PostsRepository {

    private var cachedPosts: ConcurrentMap<Int, Post>? = null

    override suspend fun getPosts(forceUpdate: Boolean): Result<List<Post>> {

        wrapEspressoIdlingResource {

            return withContext(ioDispatcher) {
                // Respond immediately with cache if available and not dirty
                if (!forceUpdate) {
                    cachedPosts?.let { cachedPosts ->
                        return@withContext Success(
                            cachedPosts.values.sortedBy { it.id })
                    }
                }

                try {
                    val newPosts = fetchPostsFromRemoteOrLocal(forceUpdate)


                    // Refresh the cache with the new posts
                    (newPosts as? Success)?.let { refreshCache(it.data) }

                    cachedPosts?.values?.let { posts ->
                        return@withContext Success(posts.sortedBy { it.id })
                    }

                    (newPosts as? Success)?.let {
                        if (it.data.isEmpty()) {
                            return@withContext Success(it.data)
                        }
                    }
                } catch (e: Exception) {
                    return@withContext Result.Error(Exception("Illegal state"))
                }

                return@withContext Result.Error(Exception("Illegal state"))
            }
        }
    }

    private suspend fun fetchPostsFromRemoteOrLocal(forceUpdate: Boolean): Result<List<Post>> {
        // Remote first
        when (val remotePosts = postsRemoteDataSource.getPosts()) {
            is Error -> {
                Timber.w("Remote data source fetch failed")
            }
            is Success -> {
                refreshLocalDataSource(remotePosts.data)
                return remotePosts
            }
            else -> throw IllegalStateException()
        }

        // Don't read from local if it's forced
        if (forceUpdate) {
            return Result.Error(Exception("Can't force refresh: remote data source is unavailable"))
        }

        // Local if remote fails
        val localPosts = postsLocalDataSource.getPosts()
        if (localPosts is Success) return localPosts
        return Result.Error(Exception("Error fetching from remote and local"))
    }

    override suspend fun getPost(postId: Int, forceUpdate: Boolean): Result<Post> {

        wrapEspressoIdlingResource {

            return withContext(ioDispatcher) {
                // Respond immediately with cache if available
                if (!forceUpdate) {
                    getPostWithId(postId)?.let {
                        EspressoIdlingResource.decrement() // Set app as idle.
                        return@withContext Success(it)
                    }
                }

                val newPost = fetchPostFromRemoteOrLocal(postId, forceUpdate)

                // Refresh the cache with the new posts
                (newPost as? Success)?.let { cachePost(it.data) }

                return@withContext newPost
            }
        }
    }

    private suspend fun fetchPostFromRemoteOrLocal(
        postId: Int,
        forceUpdate: Boolean
    ): Result<Post> {
        // Remote first
        when (val remotePost = postsRemoteDataSource.getPost(postId)) {
            is Error -> Timber.w("Remote data source fetch failed")
            is Success -> {
                refreshLocalDataSource(remotePost.data)
                return remotePost
            }
            else -> throw IllegalStateException()
        }

        // Don't read from local if it's forced
        if (forceUpdate) {
            return Result.Error(Exception("Refresh failed"))
        }

        // Local if remote fails
        val localPosts = postsLocalDataSource.getPost(postId)
        if (localPosts is Success) return localPosts
        return Result.Error(Exception("Error fetching from remote and local"))
    }

    private fun refreshCache(posts: List<Post>) {
        cachedPosts?.clear()
        posts.sortedBy { it.id }.forEach {
            cacheAndPerform(it) {}
        }
    }

    private suspend fun refreshLocalDataSource(post: Post) {
        postsLocalDataSource.savePost(post)
    }

    private suspend fun refreshLocalDataSource(posts: List<Post>) {
        postsLocalDataSource.deleteAllPosts()
        for (post in posts) {
            postsLocalDataSource.savePost(post)
        }
    }

    private fun getPostWithId(id: Int) = cachedPosts?.get(id)

    private fun cachePost(post: Post): Post {
        val cachedPost = Post(post.id, post.userId, post.title, post.body)
        // Create if it doesn't exist.
        if (cachedPosts == null) {
            cachedPosts = ConcurrentHashMap()
        }
        cachedPosts?.put(cachedPost.id, cachedPost)
        return cachedPost
    }

    private inline fun cacheAndPerform(post: Post, perform: (Post) -> Unit) {
        val cachedPost = cachePost(post)
        perform(cachedPost)
    }
}
