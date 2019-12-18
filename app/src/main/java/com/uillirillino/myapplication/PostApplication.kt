package com.uillirillino.myapplication

import android.app.Application
import com.uillirillino.myapplication.data.source.repo.CommentsRepository
import com.uillirillino.myapplication.data.source.repo.PostsRepository
import com.uillirillino.myapplication.data.source.repo.UsersRepository
import com.uillirillino.myapplication.network.PostApi
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject

/**
 * An application that lazily provides a repository. Note that this Service Locator pattern is
 * used to simplify the sample. Consider a Dependency Injection framework.
 *
 * Also, sets up Timber in the DEBUG BuildConfig. Read Timber's documentation for production setups.
 */
class PostApplication : Application() {


    @Inject
    lateinit var postApi: PostApi

    // Depends on the flavor,
    val postsRepository: PostsRepository
        get() = ServiceLocator.providePostsRepository(this)

    val commentsRepository: CommentsRepository
        get() = ServiceLocator.provideCommentsRepository(this)

    val usersRepository: UsersRepository
        get() = ServiceLocator.provideUsersRepository(this)

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
    }
}
