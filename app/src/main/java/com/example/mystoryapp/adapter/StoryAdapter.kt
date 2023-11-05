package com.example.mystoryapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystoryapp.model.ListStoryItem
import com.example.mystoryapp.databinding.ItemRowStoryBinding
import com.example.mystoryapp.ui.detail.DetailActivity


class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    inner class StoryViewHolder(val binding: ItemRowStoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        story?.let {
            with(holder.binding) {
                Glide.with(holder.itemView.context)
                    .load(story.photoUrl)
                    .into(ivStory)
                tvName.text = story.name
                tvDescription.text = story.description
            }

            with(holder.itemView) {
                setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            context as Activity,
                            Pair(holder.binding.ivStory, "photo"),
                            Pair(holder.binding.tvName, "name"),
                            Pair(holder.binding.tvDescription, "description"),
                        )

                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.STORY, story)
                    context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    companion object {
         val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id // Gantilah ini dengan kunci perbandingan yang sesuai
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}