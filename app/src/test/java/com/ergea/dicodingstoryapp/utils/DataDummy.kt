package com.ergea.dicodingstoryapp.utils

import com.ergea.dicodingstoryapp.data.remote.model.story.ListStoryItem

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

object DataDummy {
    fun generateDummyStory(): MutableList<ListStoryItem> {
        val listStory : MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..10) {
            val story = ListStoryItem(
                id = "story-WaAk1Es3P0BgSvkM",
                name = "xx",
                description = "avasc",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1682445695008_Ztw-k8EP.png",
                createdAt = "2023-04-25T18:01:35.009Z",
                lat = 37,
                lon = -122
            )
            listStory.add(story)
        }
        return listStory
    }

    fun emptyStory(): MutableList<ListStoryItem> {
        return arrayListOf()
    }
}