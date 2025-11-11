package com.example.successmeter.ui.intervals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.successmeter.databinding.FragmentIntervalsBinding

/**
 * IntervalsFragment
 * -----------------
 * Shows a simple grid of empty interval cells.
 * No data logic yet — just the visual scaffold.
 */
class IntervalsFragment : Fragment() {

    // ViewBinding pattern for Fragments
    private var _binding: FragmentIntervalsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the binding instead of using inflate(R.layout...)
        _binding = FragmentIntervalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val span = 6 // Today we show 6 columns (10-min slots). 4/2 will come later.

        // Set up the RecyclerView grid and attach the simple adapter
        binding.recyclerIntervals.layoutManager = GridLayoutManager(requireContext(), span)
        binding.recyclerIntervals.adapter = PlaceholderIntervalsAdapter(span)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Avoid memory leaks
        _binding = null
    }
}
