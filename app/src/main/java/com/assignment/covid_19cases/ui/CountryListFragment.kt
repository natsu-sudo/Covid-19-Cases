package com.assignment.covid_19cases.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assignment.covid_19cases.R
import com.assignment.covid_19cases.adapter.CountryListAdapter
import com.assignment.covid_19cases.databinding.FragmentCountryListBinding
import com.assignment.covid_19cases.pojo.ErrorCode
import com.assignment.covid_19cases.pojo.Status
import com.assignment.covid_19cases.viewmodel.CountryListViewModel
import com.assignment.covid_19cases.viewmodel.CountryListViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar


private const val TAG = "CountryListFragment"
class CountryListFragment : Fragment() {

    lateinit var binding:FragmentCountryListBinding
    lateinit var countryListViewModel:CountryListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        countryListViewModel=ViewModelProvider(this,CountryListViewModelFactory(requireContext()))[CountryListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentCountryListBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val locale:String = getCurrentLocations()
        Log.d(TAG, "onViewCreated Country Name: $locale")
        binding.currentLocationFlag
        Glide.with(view)
            .load(getString(R.string.flag_url,getCurrentLocations()))
            .error(R.drawable.ic_launcher_background)
            .into(binding.currentLocationFlag)
        binding.listRecycler.apply {
            layoutManager=LinearLayoutManager(context)
            adapter=CountryListAdapter{
                findNavController().navigate(CountryListFragmentDirections.actionCountryListFragmentToCountryDetailFragment(it))
            }
            isNestedScrollingEnabled = true;
            setHasFixedSize(false);
            hasFixedSize()
            addItemDecoration(DividerItemDecoration(activity, RecyclerView.VERTICAL))
        }

        countryListViewModel.status.observe(viewLifecycleOwner, Observer { loadingStatus ->
            when (loadingStatus?.status) {
                (Status.LOADING) -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d(TAG, "onViewCreated: Status loading")
                }
                (Status.SUCCESS) -> {
                    binding.progressBar.visibility = View.GONE
                }
                (Status.ERROR) -> {
                    binding.loadingStatusText.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    showError(loadingStatus.error, loadingStatus.message)
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
            binding.swipeUp.isRefreshing = false
        })

        binding.swipeUp.setOnRefreshListener {
            Log.d(TAG, "onViewCreated: " + countryListViewModel.getList.value?.isEmpty())
            if (countryListViewModel.getList.value?.isEmpty() != true && isOnline(requireActivity())){
                countryListViewModel.deleteData()
            }else{
                Snackbar.make(binding.root, getString(R.string.network_error), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.ok)) {

                    }
                    .show()
                binding.swipeUp.isRefreshing=false
            }

        }

        countryListViewModel.getList.observe(viewLifecycleOwner, Observer {
            (binding.listRecycler.adapter as CountryListAdapter).submitList(it)
            if (it.isEmpty()) {
                countryListViewModel.fetchFromNetwork()
            }
        })




    }

    private fun getCurrentLocations(): String {
        val tm = requireActivity().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.networkCountryIso
    }

    private fun showError(errorCode: ErrorCode?, message: String?) {
        Log.d(TAG, "showError: ")
        when (errorCode) {
            ErrorCode.NO_DATA -> binding.loadingStatusText.text = getString(R.string.error_no_data)
            ErrorCode.NETWORK_ERROR -> binding.loadingStatusText.text =
                getString(R.string.error_network)
            ErrorCode.UNKNOWN_ERROR -> binding.loadingStatusText.text =
                getString(R.string.error_unknown, message)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }






}