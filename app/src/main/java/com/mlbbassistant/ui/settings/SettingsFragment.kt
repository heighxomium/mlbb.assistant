```kotlin
package com.mlbbassistant.ui.settings

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.mlbbassistant.R
import com.mlbbassistant.databinding.FragmentSettingsBinding
import com.mlbbassistant.overlay.OverlayService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    private var suppressSwitchListener = false

    private val overlayPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (!isAdded || _binding == null) return@registerForActivityResult
        if (Settings.canDrawOverlays(requireContext())) {
            viewModel.setOverlayEnabled(true)
            startOverlayService()
        } else {
            updateOverlaySwitch(false)
            showSnackbar(R.string.settings_overlay_permission_denied)
        }
    }

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* non-critical */ }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupControls()
        observeState()
    }

    private fun setupControls() {
        binding.switchOverlay.setOnCheckedChangeListener { _, isChecked ->
            if (suppressSwitchListener) return@setOnCheckedChangeListener
            if (isChecked) requestPermissionsAndEnable() else disableOverlay()
        }

        binding.sliderOpacity.addOnChangeListener { _, value, fromUser ->
            if (fromUser) viewModel.setOverlayOpacity(value)
        }

        binding.sliderSuggestions.addOnChangeListener { _, value, fromUser ->
            if (fromUser) viewModel.setSuggestionCount(value.toInt())
        }

        binding.btnSaveWeights.setOnClickListener {
            viewModel.setWeights(
                binding.sliderWeightMeta.value,
                binding.sliderWeightCounter.value,
                binding.sliderWeightSynergy.value
            )
            showSnackbar(R.string.settings_weights_saved)
        }

        binding.etApiUrl.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveApiUrl()
                true
            } else false
        }

        binding.btnSaveApiUrl.setOnClickListener { saveApiUrl() }
        binding.btnClearApiUrl.setOnClickListener {
            binding.etApiUrl.setText("")
            viewModel.setApiUrl("")
            showSnackbar(R.string.settings_api_url_cleared)
        }
    }

    private fun saveApiUrl() {
        val url = binding.etApiUrl.text?.toString().orEmpty().trim()
        viewModel.setApiUrl(url)
        hideKeyboard()
        val msg = if (url.isBlank()) R.string.settings_api_url_cleared else R.string.settings_api_url_saved
        showSnackbar(msg)
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.overlayEnabled.collect { enabled ->
                        updateOverlaySwitch(enabled)
                        binding.groupOverlayControls.isVisible = enabled
                    }
                }
                launch { viewModel.overlayOpacity.collect { binding.sliderOpacity.value = it } }
                launch { viewModel.suggestionCount.collect { binding.sliderSuggestions.value = it.toFloat() } }
                launch { viewModel.weightMeta.collect { binding.sliderWeightMeta.value = it } }
                launch { viewModel.weightCounter.collect { binding.sliderWeightCounter.value = it } }
                launch { viewModel.weightSynergy.collect { binding.sliderWeightSynergy.value = it } }
                launch {
                    viewModel.apiUrl.collect { url ->
                        if (!binding.etApiUrl.hasFocus()) {
                            binding.etApiUrl.setText(url)
                        }
                    }
                }
            }
        }
    }

    private fun requestPermissionsAndEnable() {
        if (!Settings.canDrawOverlays(requireContext())) {
            overlayPermissionLauncher.launch(
                Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                    data = Uri.parse("package:${requireContext().packageName}")
                }
            )
        } else {
            viewModel.setOverlayEnabled(true)
            startOverlayService()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun disableOverlay() {
        viewModel.setOverlayEnabled(false)
        requireContext().stopService(Intent(requireContext(), OverlayService::class.java))
    }

    private fun startOverlayService() {
        runCatching {
            requireContext().startForegroundService(
                Intent(requireContext(), OverlayService::class.java)
            )
        }.onFailure {
            showSnackbar(R.string.settings_overlay_start_failed)
        }
    }

    private fun updateOverlaySwitch(isChecked: Boolean) {
        suppressSwitchListener = true
        binding.switchOverlay.isChecked = isChecked
        suppressSwitchListener = false
    }

    private fun showSnackbar(messageResId: Int) {
        Snackbar.make(binding.root, messageResId, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
```