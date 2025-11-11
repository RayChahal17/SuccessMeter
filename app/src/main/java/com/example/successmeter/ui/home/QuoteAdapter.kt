package com.example.successmeter.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.successmeter.data.local.db.entity.QuoteEntity
import com.example.successmeter.databinding.ItemQuoteBinding

/**
 * QuoteAdapter:
 * - Extends ListAdapter so we get submitList(...) built-in (no manual notifyDataSetChanged()).
 * - Uses DiffUtil to compute minimal updates (smooth animations, no flicker).
 * - Binds QuoteEntity to item_quote.xml via ItemQuoteBinding.
 *
 * You can optionally pass an onItemClick lambda for row taps.
 */
class QuoteAdapter(
    private val onItemClick: (QuoteEntity) -> Unit = {}
) : ListAdapter<QuoteEntity, QuoteAdapter.VH>(Diff) {

    /**
     * DiffUtil: tells RecyclerView how to decide
     * 1) if two items represent the same row (identity),
     * 2) if their contents changed (for partial updates).
     *
     * Here we use Room's primary key (id) for identity,
     * and data class equals for content comparison.
     */
    object Diff : DiffUtil.ItemCallback<QuoteEntity>() {
        override fun areItemsTheSame(oldItem: QuoteEntity, newItem: QuoteEntity): Boolean {
            // Same database row? (unique identity)
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: QuoteEntity, newItem: QuoteEntity): Boolean {
            // Same field values? (data classes have proper equals)
            return oldItem == newItem
        }
    }
    /**
     * ViewHolder just holds a binding reference so onBind can access views safely.
     * ItemQuoteBinding corresponds to res/layout/item_quote.xml.
     */
    class VH(val binding: ItemQuoteBinding) : RecyclerView.ViewHolder(binding.root)
    /**
     * Called when a NEW row view is needed.
     * Inflate the XML -> get a binding -> wrap it in a VH.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemQuoteBinding.inflate(inflater, parent, false)
        return VH(binding)
    }
    /**
     * Called to display data at a position.
     * We pull the item from ListAdapter's internal list via getItem(position),
     * then bind its fields to the TextViews.
     */
    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)  // <-- the current QuoteEntity
        holder.binding.tvQuote.text = item.text
        holder.binding.tvAuthor.text = item.author?.let { "— $it" } ?: ""

        // Optional row click
        holder.binding.root.setOnClickListener { onItemClick(item) }
    }
    // (Optional) If you want extra-smooth change animations, enable stable IDs:
    // init { setHasStableIds(true) }
    // override fun getItemId(position: Int): Long = getItem(position).id
}
