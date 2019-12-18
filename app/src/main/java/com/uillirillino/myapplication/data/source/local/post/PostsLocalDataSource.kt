package com.uillirillino.myapplication.data.source.local.post

import com.uillirillino.myapplication.data.Post
import com.uillirillino.myapplication.data.Result
import com.uillirillino.myapplication.data.Result.Success
import com.uillirillino.myapplication.data.source.data.source.PostsDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Concrete implementation of a data source as a db.
 */
class PostsLocalDataSource internal constructor(
    private val postsDao: PostsDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PostsDataSource {

    override suspend fun getPosts(): Result<List<Post>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(postsDao.getPosts())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getPost(postId: Int): Result<Post> = withContext(ioDispatcher) {
        try {
            val post = postsDao.getPostById(postId)
            if (post != null) {
                return@withContext Success(post)
            } else {
                return@withContext Result.Error(Exception("Post not found!"))
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }

    override suspend fun savePost(post: Post) = withContext(ioDispatcher) {
        postsDao.insertPost(post)
    }

    override suspend fun deleteAllPosts() = withContext(ioDispatcher) {
        try {
            postsDao.deletePosts()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun deletePost(postId: Int) = withContext<Unit>(ioDispatcher) {
        postsDao.deletePostById(postId)
    }
}
