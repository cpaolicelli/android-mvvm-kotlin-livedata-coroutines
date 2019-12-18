package com.uillirillino.myapplication

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.uillirillino.myapplication.data.source.data.source.CommentsDataSource
import com.uillirillino.myapplication.data.source.data.source.PostsDataSource
import com.uillirillino.myapplication.data.source.data.source.UsersDataSource
import com.uillirillino.myapplication.data.source.local.AppDatabase
import com.uillirillino.myapplication.data.source.local.comment.CommentRemoteDataSource
import com.uillirillino.myapplication.data.source.local.comment.CommentsLocalDataSource
import com.uillirillino.myapplication.data.source.local.post.PostRemoteDataSource
import com.uillirillino.myapplication.data.source.local.post.PostsLocalDataSource
import com.uillirillino.myapplication.data.source.local.user.UserRemoteDataSource
import com.uillirillino.myapplication.data.source.local.user.UsersLocalDataSource
import com.uillirillino.myapplication.data.source.repo.*
import com.uillirillino.myapplication.network.PostApi
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * A Service Locator for the [PostsRepository]. This is the mock version, with a
 * [FakePostsRemoteDataSource]
 */
object ServiceLocator {

    private val lock = Any()
    private var database: AppDatabase? = null

    @Volatile
    var postsRepository: PostsRepository? = null
        @VisibleForTesting set

    @Volatile
    var commentsRepository: CommentsRepository? = null
        @VisibleForTesting set

    @Volatile
    var usersRepository: UsersRepository? = null
        @VisibleForTesting set

    fun providePostsRepository(context: Context): PostsRepository {
        synchronized(this) {
            return postsRepository ?: postsRepository ?: createPostsRepository(context)
        }
    }

    fun provideCommentsRepository(context: Context): CommentsRepository {
        synchronized(this) {
            return commentsRepository ?: commentsRepository ?: createCommentsRepository(context)
        }
    }

    fun provideUsersRepository(context: Context): UsersRepository {
        synchronized(this) {
            return usersRepository ?: usersRepository ?: createusersRepository(context)
        }
    }

    // CREATE REPOSITORY

    private fun createPostsRepository(context: Context): PostsRepository {
        return DefaultPostsRepository(createPostRemoteDataSource(), createPostLocalDataSource(context))
    }

    private fun createCommentsRepository(context: Context): CommentsRepository {
        return DefaultCommentsRepository(createCommentRemoteDataSource(), createCommentsLocalDataSource(context))
    }

    private fun createusersRepository(context: Context): UsersRepository {
        return DefaultUsersRepository(createUserRemoteDataSource(), createUsersLocalDataSource(context))
    }

    // CREATE REMOTE DATA SOURCE

    private fun createPostRemoteDataSource(): PostsDataSource {
        return PostRemoteDataSource()
    }

    private fun createCommentRemoteDataSource(): CommentsDataSource {
        return CommentRemoteDataSource()
    }

    private fun createUserRemoteDataSource(): UsersDataSource {
        return UserRemoteDataSource()
    }

    // CREATE LOCAL DATA SOURCE

    private fun createPostLocalDataSource(context: Context): PostsDataSource {
        val database = database ?: createDataBase(context)
        return PostsLocalDataSource(database.postDao())
    }

    private fun createCommentsLocalDataSource(context: Context): CommentsDataSource {
        val database = database ?: createDataBase(context)
        return CommentsLocalDataSource(database.commentDao())
    }

    private fun createUsersLocalDataSource(context: Context): UsersDataSource {
        val database = database ?: createDataBase(context)
        return UsersLocalDataSource(database.userDao())
    }

    // CREATE DATABASE

    private fun createDataBase(context: Context): AppDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "App.db"
        ).build()
        database = result
        return result
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            runBlocking {
                FakePostsRemoteDataSource.deleteAllPosts()
            }
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            postsRepository = null
        }
    }
}
