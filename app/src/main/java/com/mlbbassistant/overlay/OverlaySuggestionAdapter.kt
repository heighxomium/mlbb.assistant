```kotlin
package com.mlbbassistant.overlay

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mlbbassistant.data.model.DraftSuggestion
import com.mlbbassistant.databinding.ItemOverlaySuggestionBinding

class OverlaySuggestionAdapter :
    ListAdapter<DraftSuggestion, OverlaySuggestionAdapter.OverlayViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverlayViewHolder {
        val binding = ItemOverlaySuggestionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OverlayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OverlayViewHolder, position: Int) {
        getItem(position)?.let { suggestion ->
            holder.bind(suggestion, position + 1)
        }
    }

    class OverlayViewHolder(
        private val binding: ItemOverlaySuggestionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(suggestion: DraftSuggestion, rank: Int) {
            binding.apply {
                tvOverlayRank.text = "$rank."
                tvOverlayName.text = suggestion.hero.name
                tvOverlayScore.text = "%.0f".format(suggestion.score * 100)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DraftSuggestion>() {
            override fun areItemsTheSame(oldItem: DraftSuggestion, newItem: DraftSuggestion): Boolean =
                oldItem.hero.id == newItem.hero.id

            override fun areContentsTheSame(oldItem: DraftSuggestion, newItem: DraftSuggestion): Boolean =
                oldItem == newItem
        }
    }
}
```