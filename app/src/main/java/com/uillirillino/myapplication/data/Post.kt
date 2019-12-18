package com.uillirillino.myapplication.data

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bumptech.glide.Glide
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


/**
 * Immutable model class for a Post. In order to compile with Room, we can't use @JvmOverloads to
 * generate multiple constructors.
 *
 * @param id        unique identifier of the post
 * @param userId    id of the author
 * @param title     title of the post
 * @param body      body of the post
 */
@Entity(tableName = "posts")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Post @JvmOverloads   constructor(
    @JsonProperty("id")
    @PrimaryKey @ColumnInfo(name = "id") var id: Int = 0,
    @JsonProperty("userId")
    @ColumnInfo(name = "userId") var userId: Int = 0,
    @JsonProperty("title")
    @ColumnInfo(name = "title") var title: String = "",
    @JsonProperty("body")
    @ColumnInfo(name = "body") var body: String = "",
    @JsonIgnore
    val imageUrl: String = "https://picsum.photos/id/$id/1200/1200"
) {
    companion object {
        @JvmStatic
        @BindingAdapter("bind:imageUrl")
        fun loadImage(view: ImageView, imageUrl: String) {
            Glide.with(view.context)
                .load(imageUrl)
                .into(view)
        }
    }
}
