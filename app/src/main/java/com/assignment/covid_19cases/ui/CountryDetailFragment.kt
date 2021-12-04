package com.assignment.covid_19cases.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.assignment.covid_19cases.R
import com.assignment.covid_19cases.databinding.FragmentCountrDetailBinding
import com.assignment.covid_19cases.viewmodel.CountryListViewModel
import com.assignment.covid_19cases.viewmodel.DetailViewModel
import com.assignment.covid_19cases.viewmodel.DetailViewModelFactory

private const val TAG = "CountryDetailFragment"
class CountryDetailFragment : Fragment() {

    lateinit var detailViewModel: DetailViewModel
    lateinit var binding:FragmentCountrDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val getCountryCode=CountryDetailFragmentArgs.fromBundle(requireArguments()).countryCode
        Log.d(TAG, "onCreate: $getCountryCode")
        detailViewModel=ViewModelProvider(this,DetailViewModelFactory(getCountryCode))[DetailViewModel::class.java]
        detailViewModel.getCountryCasesFromNetwork()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentCountrDetailBinding.inflate(inflater,container,false)
        binding.viewModel = detailViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.markerOnMap.setOnClickListener {
            findNavController().navigate(CountryDetailFragmentDirections.actionCountryDetailFragmentToMapsFragment(detailViewModel.countryDetail.value?.longitude!!,detailViewModel.countryDetail.value?.latitude!!,
            detailViewModel.countryDetail.value?.country!!))
        }

    }


}