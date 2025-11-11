package com.example.successmeter.ui.quotes

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Adds vertical space above the first item and between items.
 */
class VerticalSpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val pos = parent.getChildAdapterPosition(view)
        outRect.top = if (pos == 0) space else space / 2
        outRect.bottom = space / 2
    }
}
