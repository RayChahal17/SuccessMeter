package com.example.successmeter.ui.goals

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.successmeter.databinding.FragmentGoalsBinding
import com.example.successmeter.domain.model.ChiefAimRank
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Simple Goals screen for now:
 * - Injects GoalsViewModel.
 * - Observes uiState and shows selected rank + chief aims in a TextView.
 * - Has three buttons to simulate PRIMARY / SECONDARY / TERTIARY tabs.
 */
@AndroidEntryPoint
class GoalsFragment : Fragment() {

    // ViewModel scoped to this Fragment. Hilt automatically provides
    // a GoalsViewModel instance using the @HiltViewModel + @Inject constructor.
    private val viewModel: GoalsViewModel by viewModels()

    // Backing field for ViewBinding. Nullable because the Fragment's view
    // lifecycle is shorter than the Fragment's lifecycle itself.
    private var _binding: FragmentGoalsBinding? = null

    // Non-nullable accessor used only between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout using generated binding class instead of
        // calling inflater.inflate(R.layout.fragment_goals, ...).
        _binding = FragmentGoalsBinding.inflate(inflater, container, false)
        // Root view of this Fragment's layout.
        return binding.root
    }

    @SuppressLint("SetTextI18n") // For now we're hardcoding debug text.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- User inputs (fake tabs) ---
        binding.buttonPrimary.setOnClickListener {
            viewModel.onChiefAimSelected(ChiefAimRank.PRIMARY)
        }
        binding.buttonSecondary.setOnClickListener {
            viewModel.onChiefAimSelected(ChiefAimRank.SECONDARY)
        }
        binding.buttonTertiary.setOnClickListener {
            viewModel.onChiefAimSelected(ChiefAimRank.TERTIARY)
        }

        // --- Debug seed button ---
        binding.buttonSeedChiefAims.setOnClickListener {
            viewModel.onDebugSeedChiefAims()
        }

        // --- State collection ---
        // Use the Fragment's viewLifecycleOwner so that the collection stops
        // when the view is destroyed (avoids leaks / crashes).
        viewLifecycleOwner.lifecycleScope.launch {
            // Continuously collect uiState from the ViewModel.
            viewModel.uiState.collect { state ->
                // Extract pieces of state that the UI needs.
                val chiefAims = state.chiefAims
                val count = state.numberOfChiefAims
                val selectedRank = state.selectedChiefAimRank

                if (chiefAims.isEmpty()) {
                    // No data yet: show a friendly empty-state message.
                    binding.textChiefAimsDebug.text = "No chief aims yet ($count)"
                } else {
                    // Header showing which rank is selected (if any).
                    val header = if (selectedRank != null) {
                        "Selected chief aim: $selectedRank\n\n"
                    } else {
                        "No chief aim selected\n\n"
                    }

                    // Build a multi-line string like:
                    // PRIMARY: My Main Aim
                    // SECONDARY: Another Aim
                    val aimsText = chiefAims.joinToString(separator = "\n") { aim ->
                        "${aim.rank}: ${aim.title}"
                    }

                    binding.textChiefAimsDebug.text = header + aimsText
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Important: clear the binding reference when the view is destroyed
        // to avoid leaking the view hierarchy.
        _binding = null
    }
}
