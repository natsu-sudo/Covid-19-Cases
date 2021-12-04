package com.assignment.covid_19cases.ui

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.assignment.covid_19cases.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        val place=MapsFragmentArgs.fromBundle(requireArguments()).name
        val lat=MapsFragmentArgs.fromBundle(requireArguments()).lat.toDouble()
        val log=MapsFragmentArgs.fromBundle(requireArguments()).log.toDouble()

        mapFragment?.getMapAsync(getCallBack(place,lat,log))
    }

    private fun getCallBack(place:String,lat:Double,lon:Double): OnMapReadyCallback {
        return OnMapReadyCallback {googleMap ->
            val point = LatLng(lat, lon)
            googleMap.addMarker(MarkerOptions().position(point).title(place))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(point))
        }
    }
}