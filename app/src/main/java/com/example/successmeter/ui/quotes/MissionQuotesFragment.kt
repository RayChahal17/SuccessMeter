package com.example.successmeter.ui.quotes

// ---------- Imports: keep explicit for clarity ----------
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.successmeter.databinding.FragmentMissionQuotesBinding
import com.example.successmeter.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * MissionQuotesFragment
 * ---------------------
 * Displays the list of motivational quotes.
 * - Uses ViewBinding to access views safely (no findViewById).
 * - Uses Hilt (@AndroidEntryPoint) so the Hilt-backed HomeViewModel can be injected.
 * - Collects the quotes StateFlow with repeatOnLifecycle(STARTED) to avoid leaks and wasted work.
 */
@AndroidEntryPoint
class MissionQuotesFragment : Fragment() {

    // Backing (nullable) binding reference — valid only between onCreateView and onDestroyView.
    private var _binding: FragmentMissionQuotesBinding? = null

    // Non-null shorthand accessor used only while the view exists.
    private val binding get() = _binding!!

    // Fragment-scoped ViewModel (provided by Hilt via @HiltViewModel on HomeViewModel).
    private val vm: HomeViewModel by viewModels()

    // RecyclerView adapter for quotes. Accepts an optional click lambda if needed.
    private val adapter = QuoteAdapter { quote ->
        // Optional: Handle row clicks (e.g., open details, copy text, etc.)
        // For now we keep it no-op. You can add a Snackbar/toast here if you like.
        // Snackbar.make(binding.root, "Clicked: ${quote.author}", Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Inflate the view binding. Do NOT start collecting flows here — the view isn't fully created yet.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMissionQuotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Safe place to access views, set adapters, and start lifecycle-aware collection.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 1) Attach the adapter to the RecyclerView declared in fragment_mission_quotes.xml
        binding.recyclerQuotes.adapter = adapter

        // 2) Collect quotes from the ViewModel with lifecycle awareness.
        // - Use viewLifecycleOwner.lifecycleScope (tied to the view's lifecycle).
        // - repeatOnLifecycle(STARTED) automatically stops collecting when the view is STOPPED/DESTROYED,
        //   preventing memory leaks and unnecessary work.
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.quote.collect { list ->
                    // Submit the new list to the ListAdapter.
                    // DiffUtil inside QuoteAdapter will calculate minimal changes (smooth animations).
                    adapter.submitList(list)
                }
            }
        }

        // (Optional) If you add search/filter UI later:
        // binding.searchInput.doAfterTextChanged { text ->
        //     vm.search(text.toString()) // expose search() in VM & repo
        // }
    }

    /**
     * Clear the binding reference when the view is destroyed.
     * This avoids leaking the view hierarchy when the Fragment is kept on the back stack.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
