package com.example.successmeter.ui.quotes

// ---------- Imports: keep explicit for clarity ----------
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.successmeter.databinding.FragmentMissionQuotesBinding
import com.example.successmeter.ui.home.HomeViewModel
import com.google.android.material.snackbar.Snackbar
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

    private var _binding: FragmentMissionQuotesBinding? = null
    private val binding get() = _binding!!

    // ViewModel owns data & mutations
    private val vm: QuotesViewModel by viewModels()

    // Adapter exposes two distinct user intents:
    // 1) onQuoteClick (row)  2) onFavToggle (star)
    private lateinit var adapter: QuoteAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentMissionQuotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 0) Build adapter with clear intent separation
        adapter = QuoteAdapter(
            onFavToggle = { quote, newFav ->
                vm.setFavorite(quote.id, newFav) // business event: “user favored”
            }
        )

        // 1) RecyclerView setup: layout, adapter, perf hints
        binding.recyclerQuotes.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
            adapter = this@MissionQuotesFragment.adapter
            setHasFixedSize(true) // rows are identical height -> diffing is faster
            // Add spacing once (avoid stacking decoration if navigating back)
            if (itemDecorationCount == 0) {
                val px = (12 * resources.displayMetrics.density).toInt()
                addItemDecoration(VerticalSpaceItemDecoration(px))
            }
        }

        // 2) Collect list updates with lifecycle awareness
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.quotes.collect { list -> adapter.submitList(list) }
            }
        }

        // 3) (Optional) Search box wiring if you have one in the layout:
        // binding.etSearch.doAfterTextChanged { vm.setQuery(it?.toString().orEmpty()) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid leaks
    }

    // --- Helpers ---

    // Keep platform details here so your adapter & VM stay clean
    private fun copyToClipboard(label: String, text: String) {
        val cm = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.setPrimaryClip(ClipData.newPlainText(label, text))
    }
}
