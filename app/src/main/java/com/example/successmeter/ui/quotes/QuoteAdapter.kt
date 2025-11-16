package com.example.successmeter.ui.quotes

import android.util.Log.v
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.animation.core.animate
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.successmeter.R
import com.example.successmeter.data.local.db.entity.QuoteEntity
import com.example.successmeter.databinding.ItemQuoteBinding

class QuoteAdapter(

    private val onFavToggle: (QuoteEntity, Boolean) -> Unit

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

        h.binding.btnFav.setImageResource(
            if (item.isFavorite) R.drawable.ic_star else R.drawable.ic_star_border_24
        )

        // Handle star taps:
        // 1) small tap animation (delight)
        // 2) emit the intent (quote, newFavState) to the VM via callback

        h.binding.btnFav.setOnClickListener { v ->
            v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(80).withEndAction {
                v.animate().scaleX(1f).scaleY(1f).setDuration(80).start()
            }.start()
            onFavToggle(item, !item.isFavorite)
        }
    }

    // Optional smoother animations:
    // init { setHasStableIds(true) }
    // override fun getItemId(position: Int) = getItem(position).id
}
