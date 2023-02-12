package com.example.kmitlcompanion.ui.createmapboxlocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentCreateMapboxLocationBinding
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.presentation.viewmodel.CreateMapboxLocationViewModel
import com.example.kmitlcompanion.ui.createmapboxlocation.helpers.CreateLocationHelper
import com.example.kmitlcompanion.ui.createmapboxlocation.utils.ApiRunnable
import com.example.kmitlcompanion.ui.mainactivity.utils.BottomBarUtils
import com.example.kmitlcompanion.ui.mapboxview.MapboxFragmentArgs
import com.mapbox.maps.MapView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateMapboxLocationFragment : BaseFragment<FragmentCreateMapboxLocationBinding, CreateMapboxLocationViewModel>() {
    @Inject internal lateinit var helper: CreateLocationHelper
    @Inject internal lateinit var bottomBar : BottomBarUtils
    @Inject internal lateinit var apiRunnable: ApiRunnable

    override val layoutId: Int = R.layout.fragment_create_mapbox_location

    override val viewModel: CreateMapboxLocationViewModel by viewModels()
    private val navArgs by navArgs<CreateMapboxLocationFragmentArgs>()
    override fun onReady(savedInstanceState: Bundle?) {

        navArgs.location?.let {
            viewModel.updatePositionFlyer(it)
        }

    }

    //private val viewModel: CreateMapboxLocationViewModel by viewModels()
    private var mapView: MapView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bottomBar.bottomMap?.visibility = View.GONE
        binding = FragmentCreateMapboxLocationBinding.inflate(inflater,container,false)
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
    private fun FragmentCreateMapboxLocationBinding.setupViewObservers() {
        lifecycle.addObserver(helper.map)
        lifecycle.addObserver(helper.camera)
        this@CreateMapboxLocationFragment.viewModel.run {
            currentMapLocation.observe(viewLifecycleOwner, Observer {
                helper.bottomBar.getLocationDetail(apiRunnable)
            })
            currentLocation.observe(viewLifecycleOwner, Observer {
                tvName.text = it.place
                tvAddress.text = it.address
            })
            positionFlyer.observe(viewLifecycleOwner, Observer {
                helper.camera.setCamera(it)
            })

        }

    }


}