package com.example.kmitlcompanion.ui.createmapboxlocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.databinding.FragmentCreatemapboxlocationBinding
import com.example.kmitlcompanion.presentation.CreateMapboxLocationViewModel
import com.example.kmitlcompanion.ui.createmapboxlocation.helpers.CreateLocationHelper
import com.example.kmitlcompanion.ui.createmapboxlocation.utils.ApiRunnable
import com.example.kmitlcompanion.ui.mainactivity.helper.NavHelper
import com.example.kmitlcompanion.ui.mainactivity.utils.BottomBarUtils
import com.mapbox.maps.MapView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateMapboxLocationFragment : BaseFragment<FragmentCreatemapboxlocationBinding, CreateMapboxLocationViewModel>() {
    @Inject internal lateinit var helper: CreateLocationHelper
    @Inject internal lateinit var bottomBar : BottomBarUtils
    @Inject internal lateinit var apiRunnable: ApiRunnable

    override val layoutId: Int = R.layout.fragment_createmapboxlocation

    override val viewModel: CreateMapboxLocationViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {

    }

    //private val viewModel: CreateMapboxLocationViewModel by viewModels()
    private var mapView: MapView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bottomBar.bottomMap?.visibility = View.GONE
        binding = FragmentCreatemapboxlocationBinding.inflate(inflater,container,false)
        mapView = binding.mapView
        val mapBoxMap = mapView?.getMapboxMap()

        helper.map.setup(mapView) {
            binding.viewModel = this@CreateMapboxLocationFragment.viewModel
        }
        apiRunnable.setup(viewModel)
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
                helper.bottomBar.getLocationDetail(apiRunnable)
            })
            currentLocationName.observe(viewLifecycleOwner, Observer {
                tvName.text = it.place
                tvAddress.text = it.address
            })

        }

    }


}