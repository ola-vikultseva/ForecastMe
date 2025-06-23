package com.example.forecastme.ui.citysearch

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forecastme.R
import com.example.forecastme.databinding.FragmentCitySearchBinding
import com.example.forecastme.ui.citysearch.model.CityItem
import com.example.forecastme.ui.citysearch.model.CitySearchUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CitySearchFragment : Fragment() {

    private var _binding: FragmentCitySearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CitySearchViewModel by viewModels()
    private lateinit var citySearchAdapter: CitySearchAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCitySearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        setupSearchToolbar()
        citySearchAdapter = CitySearchAdapter(emptyList()) { cityItem ->
            viewModel.onCityItemClicked(cityItem)
            findNavController().navigateUp()
        }
        binding.recyclerViewCitySearch.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = citySearchAdapter
        }
        viewModel.citySearch.observe(viewLifecycleOwner) { citySearchUiState ->
            when (citySearchUiState) {
                is CitySearchUiState.Empty -> {
                    hideLoading()
                    hideNoMatchesMessage()
                    hideData()
                    hideError()
                }
                is CitySearchUiState.Loading -> {
                    hideNoMatchesMessage()
                    hideData()
                    hideError()
                    showLoading()
                }
                is CitySearchUiState.NoMatches -> {
                    hideLoading()
                    hideData()
                    hideError()
                    showNoMatchesMessage()
                }
                is CitySearchUiState.Data -> {
                    hideLoading()
                    hideNoMatchesMessage()
                    hideError()
                    showData(citySearchUiState.cityList)
                }
                is CitySearchUiState.Error -> {
                    hideLoading()
                    hideNoMatchesMessage()
                    hideData()
                    showError(citySearchUiState.errorMessage)
                }
            }
        }
    }

    private fun setupSearchToolbar() {
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.city_search_screen_action_bar_title)
        val themedContext = ContextThemeWrapper(requireContext(), R.style.ToolbarTheme)
        val searchView = SearchView(themedContext).apply {
            layoutParams = Toolbar.LayoutParams(
                Toolbar.LayoutParams.MATCH_PARENT,
                Toolbar.LayoutParams.WRAP_CONTENT
            )
            isIconified = false
            queryHint = getString(R.string.city_search_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(query: String): Boolean {
                    viewModel.onQueryTextChanged(query)
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.onQueryTextChanged(query)
                    return true
                }
            })
        }
        toolbar.addView(searchView)
        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                toolbar.removeView(searchView)
            }
        })
    }

    private fun showLoading() {
        binding.layoutLoading.root.isVisible = true
    }

    private fun hideLoading() {
        binding.layoutLoading.root.isVisible = false
    }

    private fun showNoMatchesMessage() {
        binding.textViewNoMatchingCities.isVisible = true
    }

    private fun hideNoMatchesMessage() {
        binding.textViewNoMatchingCities.isVisible = false
    }

    private fun showData(cityList: List<CityItem>) {
        citySearchAdapter.apply {
            this.cityList = cityList
            notifyDataSetChanged()
        }
        binding.recyclerViewCitySearch.isVisible = true
    }

    private fun hideData() {
        binding.recyclerViewCitySearch.isVisible = false
    }

    private fun showError(errorMessage: String) {
        with(binding) {
            layoutError.textViewErrorMessage.text = errorMessage
            layoutError.root.isVisible = true
        }
    }

    private fun hideError() {
        binding.layoutError.root.isVisible = false
    }
}