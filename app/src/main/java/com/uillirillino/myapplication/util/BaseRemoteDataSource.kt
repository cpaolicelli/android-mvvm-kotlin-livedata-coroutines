package com.uillirillino.myapplication.util

import com.uillirillino.myapplication.data.source.local.comment.CommentRemoteDataSource
import com.uillirillino.myapplication.data.source.local.post.PostRemoteDataSource
import com.uillirillino.myapplication.data.source.local.user.UserRemoteDataSource
import com.uillirillino.myapplication.injection.NetworkModule
import com.uillirillino.myapplication.injection.component.DaggerViewModelInjector
import com.uillirillino.myapplication.injection.component.ViewModelInjector

abstract class BaseRemoteDataSource {

    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }


    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is PostRemoteDataSource -> injector.injectPost(this)
            is CommentRemoteDataSource -> injector.injectComment(this)
            is UserRemoteDataSource -> injector.injectUser(this)
        }
    }
}
