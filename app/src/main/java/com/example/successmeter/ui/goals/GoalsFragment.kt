package com.example.successmeter.ui.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.successmeter.databinding.FragmentGoalsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// Marks this Fragment as a Hilt injection target.
// Required so that `by viewModels()` can obtain a Hilt-backed ViewModel.
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Use the Fragment's viewLifecycleOwner so that the collection stops
        // when the view is destroyed (avoids leaks / crashes).
        viewLifecycleOwner.lifecycleScope.launch {
            // Continuously collect uiState from the ViewModel.
            viewModel.uiState.collect { state ->
                // Extract the list of chief aims from the state.
                val chiefAims = state.chiefAims
                val count = state.numberOfChiefAims


                if (chiefAims.isEmpty()) {
                    // No data yet: show a friendly empty-state message.
                    binding.textChiefAimsDebug.text = "No chief aims yet ${count}"
                } else {
                    // Build a multi-line string like:
                    // PRIMARY: My Main Aim
                    // SECONDARY: Another Aim
                    binding.textChiefAimsDebug.text =
                        chiefAims.joinToString(separator = "\n") { aim ->
                            "${aim.rank}: ${aim.title}"
                        }
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
