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
import com.example.successmeter.ui.quotes.QuoteAdapter
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
    private val adapter = QuoteAdapter { _, _ -> /* no-op for now */ }

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


    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
