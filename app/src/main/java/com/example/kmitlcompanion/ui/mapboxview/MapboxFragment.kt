package com.example.kmitlcompanion.ui.mapboxview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kmitlcompanion.databinding.FragmentMapboxBinding
import com.mapbox.maps.MapView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapboxFragment : Fragment() {

    private lateinit var binding: FragmentMapboxBinding
    private var mapView: MapView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapboxBinding.inflate(inflater,container,false)
        mapView = binding.mapView

        return binding.root
    }
}