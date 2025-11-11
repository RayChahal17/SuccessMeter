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

    // Swap HomeViewModel → QuotesViewModel
    private val vm: QuotesViewModel by viewModels()

    private val adapter = QuoteAdapter { quote ->
        // Example: copy to clipboard + increment uses
        val cm = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.setPrimaryClip(ClipData.newPlainText("Quote", "“${quote.text}” — ${quote.author ?: ""}"))
        Snackbar.make(binding.root, "Copied", Snackbar.LENGTH_SHORT).show()
        vm.incrementUses(quote.id)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentMissionQuotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 1) LayoutManager (required) – vertical list
        binding.recyclerQuotes.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(requireContext())

        // 2) Adapter
        binding.recyclerQuotes.adapter = adapter

        // 3) Optional performance hint (size doesn’t change when content updates)
        binding.recyclerQuotes.setHasFixedSize(true)

        // 4) Optional spacing
        val px = (12 * resources.displayMetrics.density).toInt()
        binding.recyclerQuotes.addItemDecoration(VerticalSpaceItemDecoration(px))

        // 5) Collect your flow as before…
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.quotes.collect { list -> adapter.submitList(list) }
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}

