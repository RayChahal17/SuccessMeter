package com.example.successmeter.ui.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.successmeter.R
import com.example.successmeter.databinding.FragmentTodayBinding

/**
 * TodayFragment
 * -----------------
 * Shows a simple grid of empty interval cells.
 * No data logic yet — just the visual scaffold.
 */
class TodayFragment : Fragment() {

    // ViewBinding pattern for Fragments
    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the binding instead of using inflate(R.layout...)
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val span = 6 // Today we show 6 columns (10-min slots). 4/2 will come later.

    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Avoid memory leaks
        _binding = null
    }
}
