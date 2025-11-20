package com.example.successmeter.ui.goals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.successmeter.R
import com.example.successmeter.databinding.FragmentGoalsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

@AndroidEntryPoint

class GoalsFragment : Fragment() {
    // Hilt-provided ViewModel scoped to this Fragment.
    private val viewModel: GoalsViewModel by viewModels()
    private var _binding: FragmentGoalsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the binding instead of using inflate(R.layout...)
        _binding = FragmentGoalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Collect the chiefAims StateFlow and update the TextView whenever it changes.
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.chiefAims.collect {
                chiefAims ->
                if(chiefAims.isEmpty()){
                    binding.textChiefAimsDebug.text = "No chief aims yet"
                } else {
                    binding.textChiefAimsDebug.text = chiefAims.joinToString(separator = "\n") { aim ->
                        "${aim.rank}: ${aim.title}"
                }
            }
            }
        }
    }


    }

