package com.uillirillino.myapplication.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.uillirillino.myapplication.data.Comment
import com.uillirillino.myapplication.data.Post
import com.uillirillino.myapplication.data.User
import com.uillirillino.myapplication.data.source.local.comment.CommentsDao
import com.uillirillino.myapplication.data.source.local.post.PostsDao
import com.uillirillino.myapplication.data.source.local.user.UsersDao

/**
 * The Room Database that contains the tables.
 *
 * Note that exportSchema should be true in production databases.
 */
@Database(entities = [Post::class, User::class, Comment::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun postDao(): PostsDao

    abstract fun commentDao(): CommentsDao

    abstract fun userDao(): UsersDao
}
