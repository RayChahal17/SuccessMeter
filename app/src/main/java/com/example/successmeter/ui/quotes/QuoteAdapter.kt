package com.example.successmeter.ui.quotes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.successmeter.data.local.db.entity.QuoteEntity
import com.example.successmeter.databinding.ItemQuoteBinding

class QuoteAdapter(
    private val onItemClick: (QuoteEntity) -> Unit = {}
) : ListAdapter<QuoteEntity, QuoteAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<QuoteEntity>() {
        override fun areItemsTheSame(a: QuoteEntity, b: QuoteEntity) = a.id == b.id
        override fun areContentsTheSame(a: QuoteEntity, b: QuoteEntity) = a == b
    }

    class VH(val binding: ItemQuoteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemQuoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val item = getItem(pos)
        h.binding.tvQuote.text = item.text
        h.binding.tvAuthor.text = item.author
            ?.takeIf { it.isNotBlank() }
            ?.let { "— $it" } ?: ""

        h.binding.root.setOnClickListener { onItemClick(item) }
    }

    // Optional smoother animations:
    // init { setHasStableIds(true) }
    // override fun getItemId(position: Int) = getItem(position).id
}
