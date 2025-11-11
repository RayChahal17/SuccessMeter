package com.example.successmeter.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.successmeter.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val vm: HomeViewModel by viewModels()
    //This gets a ViewModel scoped to this Fragment.
    //Hilt will build it (because the VM is annotated with @HiltViewModel and has @Inject constructor).
    //Benefit: survives config changes (rotation), so data/state isn’t lost.
    private val adapter = QuoteAdapter()
    //Your RecyclerView adapter that knows how to display each row (quotes).

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    //Inflate the XML for this screen using ViewBinding.
    //FragmentHomeBinding is auto-generated from fragment_home.xml.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvQuotes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvQuotes.adapter = adapter
        //Hook up the RecyclerView:
        //LinearLayoutManager = vertical list
        //set the adapter

        binding.fabAdd.setOnClickListener { vm.addSample() }
        //When the FAB is tapped, call the ViewModel method that inserts a sample quote.
        //ViewModel writes to Room; Room emits a new list; UI updates automatically (thanks to Flow).

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.recent.collect { list -> adapter.submitList(list) }
            }
        }
        //Collecting the Flow safely:
        //viewLifecycleOwner.lifecycleScope runs coroutines tied to the view’s lifecycle (not the fragment object), so they cancel when the view is destroyed.
        //repeatOnLifecycle(STARTED) means:
        //When the view is STARTED or RESUMED, start collecting vm.recent.
        //When the view moves to STOPPED (e.g., you navigate away), stop collecting automatically (avoid leaks and unnecessary work).
        //adapter.submitList(list) feeds the latest list to ListAdapter (DiffUtil does efficient updates).
        //rule of thumb: repeatOnLifecycle(STARTED) is the recommended, leak-free way to collect a Flow in a Fragment.
    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
