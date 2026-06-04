```kotlin
package com.mlbbassistant.ui.heroes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mlbbassistant.data.model.Hero
import com.mlbbassistant.databinding.ItemHeroBinding

class HeroAdapter(
    private val onHeroClick: (Hero) -> Unit = {}
) : ListAdapter<Hero, HeroAdapter.HeroViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val binding = ItemHeroBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HeroViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class HeroViewHolder(
        private val binding: ItemHeroBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(hero: Hero) {
            with(binding) {
                tvHeroName.text = hero.name
                tvHeroRole.text = buildString {
                    append(hero.role.displayName)
                    hero.secondaryRole?.let { append(" / ${it.displayName}") }
                }
                tvWinRate.text = "Win: %.1f%%".format(hero.winRate * 100)
                tvBanRate.text = "Ban: %.1f%%".format(hero.banRate * 100)
                tvLane.text = hero.lane.displayName
                root.setOnClickListener { onHeroClick(hero) }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Hero>() {
            override fun areItemsTheSame(oldItem: Hero, newItem: Hero): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Hero, newItem: Hero): Boolean =
                oldItem == newItem
        }
    }
}
```