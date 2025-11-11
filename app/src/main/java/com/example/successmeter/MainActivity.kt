package com.example.successmeter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.successmeter.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        binding.bottomNav.setupWithNavController(navHostFragment.navController)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Top inset for toolbar (if present in activity_main)
            binding.toolbar?.setPaddingRelative(
                binding.toolbar?.paddingStart ?: 0,
                bars.top,
                binding.toolbar?.paddingEnd ?: 0,
                binding.toolbar?.paddingBottom ?: 0
            )
            // Bottom inset for bottom nav
            binding.bottomNav.setPaddingRelative(
                binding.bottomNav.paddingStart,
                binding.bottomNav.paddingTop,
                binding.bottomNav.paddingEnd,
                bars.bottom
            )
            insets
        }



    }
}
