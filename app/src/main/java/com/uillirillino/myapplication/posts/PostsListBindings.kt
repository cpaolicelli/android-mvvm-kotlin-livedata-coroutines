package com.uillirillino.myapplication.posts

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uillirillino.myapplication.data.Post

/**
 * [BindingAdapter]s for the [Task]s list.
 */
@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Post>) {
    (listView.adapter as PostsAdapter).submitList(items)
}