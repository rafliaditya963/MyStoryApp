package com.example.mystoryapp

import com.example.mystoryapp.model.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "id + $i",
                i.toString(),
                "created + $i",
                "name + $i",
                i.toDouble(),
                i.toDouble()
            )
            items.add(story)
        }
        return items
    }
}
