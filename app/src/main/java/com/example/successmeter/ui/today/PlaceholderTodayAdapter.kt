package com.example.successmeter.ui.today

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.successmeter.R

class PlaceholderTodayAdapter(private val span: Int)
    : RecyclerView.Adapter<PlaceholderTodayAdapter.ViewHolder>() {

    // 24 hours * slots per hour
    private val total = 24 * span

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_interval_cell, parent, false)
        return ViewHolder(v)
    }

    // IMPORTANT: no TODO() here — just a no-op for now
    override fun onBindViewHolder(holder: ViewHolder, position: Int) { /* no data yet */ }

    override fun getItemCount() = total
}
