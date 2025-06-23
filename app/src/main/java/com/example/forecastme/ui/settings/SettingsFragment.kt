package com.example.forecastme.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.forecastme.R
import com.example.forecastme.domain.model.Location
import com.example.forecastme.databinding.FragmentSettingsBinding
import com.example.forecastme.ui.settings.model.UnitSystemUiOption
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var activity: AppCompatActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        activity = (requireActivity() as AppCompatActivity)
        activity.supportActionBar!!.apply {
            title = getString(R.string.settings_screen_action_bar_title)
            subtitle = getString(R.string.settings_screen_action_bar_subtitle)
        }
        with(viewModel) {
            location.observe(viewLifecycleOwner) { location ->
                updateLocation(location)
            }
            unitSystem.observe(viewLifecycleOwner) { unitSystemUiOption ->
                updateUnitSystem(unitSystemUiOption)
            }
        }
    }

    private fun updateLocation(location: Location) {
        binding.textViewLocationSettingValue.text = location.cityName
        binding.layoutLocationSetting.apply {
            setOnClickListener {
                findNavController().navigate(R.id.actionSettingsFragmentToCitySearchFragment)
            }
            isClickable = true
            alpha = 1.0f
        }
    }

    private fun updateUnitSystem(unitSystemUiOption: UnitSystemUiOption) {
        binding.textViewUnitSystemSettingValue.text = getString(unitSystemUiOption.labelResId)
        binding.layoutUnitSystemSetting.setOnClickListener {
            showUnitSystemSelectionDialog()
        }
    }

    private fun showUnitSystemSelectionDialog() {
        val options = UnitSystemUiOption.all()
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.unit_system_selection_dialog_title))
            setItems(options.map { getString(it.labelResId) }.toTypedArray()) { _, index ->
                viewModel.onUnitSystemSelected(options[index].unitSystem)
            }
            show()
        }
    }
}