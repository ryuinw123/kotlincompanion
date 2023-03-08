package com.example.kmitlcompanion.ui.createpolygonevent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentCreateCircleEventBinding
import com.example.kmitlcompanion.databinding.FragmentCreatePolygonEventBinding
import com.example.kmitlcompanion.presentation.viewmodel.CreatePolygonEventViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.ui.createpolygonevent.helpers.ViewMap
import com.example.kmitlcompanion.ui.mainactivity.utils.BottomBarUtils
import com.example.kmitlcompanion.ui.mapboxview.MapboxFragmentArgs
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreatePolygonEventFragment : BaseFragment<FragmentCreatePolygonEventBinding,CreatePolygonEventViewModel>() {

    @Inject internal lateinit var mapHelper: ViewMap
    @Inject lateinit var bottomBarUtils: BottomBarUtils

    override val viewModel : CreatePolygonEventViewModel by viewModels()

    override val layoutId :Int = R.layout.fragment_create_polygon_event

    private val navArgs by navArgs<CreatePolygonEventFragmentArgs>()


    override fun onReady(savedInstanceState: Bundle?) {
        navArgs.location?.let {
            Log.d("createEvent" , "Polygon position = $it")
            viewModel.updatePositionFlyer(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePolygonEventBinding.inflate(inflater,container,false).apply {
            setupViewObservers()
        }

        val mapView = binding.mapView
        mapHelper.setup(viewModel,mapView) {
            binding.viewModel = this@CreatePolygonEventFragment.viewModel
        }

        ///binding.createLocationButton.visibility = View.INVISIBLE
        bottomBarUtils.bottomMap?.visibility = View.INVISIBLE
        binding.createLocationButton.isEnabled = false


        return binding.root
    }

    private fun FragmentCreatePolygonEventBinding.setupViewObservers() {
        lifecycle.addObserver(mapHelper)
        this@CreatePolygonEventFragment.viewModel.run {
            activePoint.observe(viewLifecycleOwner, Observer {
                //createLocationButton.visibility = View.VISIBLE
                createLocationButton.isEnabled = it.size > 3
                mapHelper.redraw()
            })

            positionFlyer.observe(viewLifecycleOwner, Observer {
                Log.d("createEvent" , "SetPolygonCamera position = $it")
                mapHelper.setCamera(it,18.0)
            })



        }
    }




}