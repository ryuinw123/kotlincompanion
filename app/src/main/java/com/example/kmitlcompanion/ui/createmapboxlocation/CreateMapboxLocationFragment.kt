package com.example.kmitlcompanion.ui.createmapboxlocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.kmitlcompanion.databinding.FragmentCreatemapboxlocationBinding
import com.example.kmitlcompanion.presentation.CreateMapboxLocationViewModel
import com.example.kmitlcompanion.ui.createmapboxlocation.helpers.CreateLocationHelper
import com.mapbox.maps.MapView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateMapboxLocationFragment : Fragment() {
    @Inject internal lateinit var helper: CreateLocationHelper
    private val viewModel: CreateMapboxLocationViewModel by viewModels()

    private lateinit var binding : FragmentCreatemapboxlocationBinding
    private var mapView: MapView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatemapboxlocationBinding.inflate(inflater,container,false)
        mapView = binding.mapView
        val mapBoxMap = mapView?.getMapboxMap()

        helper.map.setup(mapView) {
            binding.viewModel = this@CreateMapboxLocationFragment.viewModel
        }
        helper.camera.setup(mapBoxMap,viewModel)
        binding.setupViewObservers()
        return binding.root
    }
    private fun FragmentCreatemapboxlocationBinding.setupViewObservers() {
        lifecycle.addObserver(helper.map)
        lifecycle.addObserver(helper.camera)
        this@CreateMapboxLocationFragment.viewModel.run {
            createLocation.observe(viewLifecycleOwner, Observer {
                helper.camera.createLocation()
            })
            currentMapLocation.observe(viewLifecycleOwner, Observer {
                textView.text = it
            })
        }

    }
}