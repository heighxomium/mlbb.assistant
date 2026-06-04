```kotlin
package com.mlbbassistant.ui.draft

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mlbbassistant.data.model.DraftSuggestion
import com.mlbbassistant.databinding.ItemSuggestionBinding

class SuggestionAdapter(
    private val onSuggestionClick: (DraftSuggestion) -> Unit
) : ListAdapter<DraftSuggestion, SuggestionAdapter.SuggestionViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        val binding = ItemSuggestionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SuggestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        getItem(position)?.let { suggestion ->
            holder.bind(suggestion, position + 1)
        }
    }

    inner class SuggestionViewHolder(
        private val binding: ItemSuggestionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(suggestion: DraftSuggestion, rank: Int) {
            with(binding) {
                tvRank.text = "#$rank"
                tvSuggestionName.text = suggestion.hero.name
                tvSuggestionRole.text = suggestion.hero.role.displayName
                tvScore.text = "${"%.0f".format(suggestion.score * 100)}pts"
                tvReason.text = suggestion.reason
                progressScore.progress = (suggestion.score * 100).toInt()
                root.setOnClickListener { onSuggestionClick(suggestion) }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DraftSuggestion>() {
            override fun areItemsTheSame(oldItem: DraftSuggestion, newItem: DraftSuggestion): Boolean {
                return oldItem.hero.id == newItem.hero.id
            }

            override fun areContentsTheSame(oldItem: DraftSuggestion, newItem: DraftSuggestion): Boolean {
                return oldItem == newItem
            }
        }
    }
}
```