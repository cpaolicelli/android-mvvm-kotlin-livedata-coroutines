package com.uillirillino.myapplication.injection.component

import com.uillirillino.myapplication.data.source.local.comment.CommentRemoteDataSource
import com.uillirillino.myapplication.data.source.local.post.PostRemoteDataSource
import com.uillirillino.myapplication.data.source.local.user.UserRemoteDataSource
import com.uillirillino.myapplication.injection.NetworkModule
import com.uillirillino.myapplication.posts.PostsViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Component providing inject() methods for presenters.
 */
@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {

    /**
     * Injects required dependencies into the specified [PostRemoteDataSource].
     * @param postRemoteDataSource PostViewModel in which to inject the dependencies
     */
    fun injectPost(postRemoteDataSource: PostRemoteDataSource)

    /**
     * Injects required dependencies into the specified [CommentRemoteDataSource].
     * @param commentRemoteDataSource PostViewModel in which to inject the dependencies
     */
    fun injectComment(commentRemoteDataSource: CommentRemoteDataSource)

    /**
     * Injects required dependencies into the specified [UserRemoteDataSource].
     * @param userRemoteDataSource PostViewModel in which to inject the dependencies
     */
    fun injectUser(userRemoteDataSource: UserRemoteDataSource)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}