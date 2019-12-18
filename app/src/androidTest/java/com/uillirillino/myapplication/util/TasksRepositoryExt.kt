package com.uillirillino.myapplication.util

import com.uillirillino.myapplication.data.source.repo.PostsRepository
import kotlinx.coroutines.runBlocking

/**
 * A blocking version of PostsRepository.saveTask to minimize the number of times we have to
 * explicitly add <code>runBlocking { ... }</code> in our tests
 */
fun PostsRepository.getPosts() = runBlocking {
    this@getPosts.getPosts()
}
