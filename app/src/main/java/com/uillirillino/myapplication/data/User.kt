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
 * Immutable model class for a User. In order to compile with Room, we can't use @JvmOverloads to
 * generate multiple constructors.
 *
 * @param id       unique identifier of the user
 * @param name     name of the user
 * @param username username of the user
 */
@Entity(tableName = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
data class User @JvmOverloads constructor(
    @JsonProperty("id")
    @PrimaryKey @ColumnInfo(name = "id") var id: Int = 0,
    @JsonProperty("name")
    @ColumnInfo(name = "name") var name: String = "",
    @JsonProperty("username")
    @ColumnInfo(name = "username") var username: String = "",
    @JsonIgnore
    val imageUrl: String = "https://i.pravatar.cc/150?img=$id"
) {
    companion object {
        @JvmStatic
        @BindingAdapter("bind:userImageUrl")
        fun loadUserImage(view: ImageView, imageUrl: String) {
            Glide.with(view.context)
                .load(imageUrl)
                .into(view)
        }
    }
}