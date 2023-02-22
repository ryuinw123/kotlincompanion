package com.example.kmitlcompanion.ui.createcircleevent

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
import com.example.kmitlcompanion.databinding.FragmentMapboxBinding
import com.example.kmitlcompanion.presentation.viewmodel.CreateCircleEventViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.ui.createcircleevent.helpers.ViewBottomBar
import com.example.kmitlcompanion.ui.createcircleevent.helpers.ViewMap
import com.example.kmitlcompanion.ui.mainactivity.utils.BottomBarUtils
import com.example.kmitlcompanion.ui.mapboxview.MapboxFragmentArgs
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateCircleEventFragment : BaseFragment<FragmentCreateCircleEventBinding,CreateCircleEventViewModel>() {

    @Inject
    internal lateinit var mapHelper: ViewMap

    @Inject
    internal lateinit var bottomBar: ViewBottomBar

    @Inject lateinit var bottomBarUtils: BottomBarUtils

    override val viewModel : CreateCircleEventViewModel by viewModels()

    override val layoutId :Int = R.layout.fragment_create_polygon_event

    private val navArgs by navArgs<CreateCircleEventFragmentArgs>()


    override fun onReady(savedInstanceState: Bundle?) {
        navArgs.location?.let {
            Log.d("createEvent" , "Circle position = $it")
            viewModel.updatePositionFlyer(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateCircleEventBinding.inflate(inflater,container,false).apply {
            mapHelper.setup(this@CreateCircleEventFragment.viewModel,mapView) {
                viewModel = this@CreateCircleEventFragment.viewModel
            }
            bottomBar.setup(this@CreateCircleEventFragment.viewModel,guideline,circleRadiusSeekbar)

            bottomBarUtils.bottomMap?.visibility = View.INVISIBLE
            //createLocationButton.visibility = View.INVISIBLE
            createLocationButton.isEnabled = false

            setupViewObservers()
        }






        return binding.root
    }

    private fun FragmentCreateCircleEventBinding.setupViewObservers() {
        lifecycle.addObserver(mapHelper)
        this@CreateCircleEventFragment.viewModel.run {
            currentRadius.observe(viewLifecycleOwner, Observer {
                circleRadiusTextview.text = "Radius : $it km"
                mapHelper.redraw()
            })

            currentPin.observe(viewLifecycleOwner,Observer{
                mapHelper.redraw()
                //createLocationButton.visibility = View.VISIBLE
                createLocationButton.isEnabled = true
            })

            positionFlyer.observe(viewLifecycleOwner, Observer {
                Log.d("createEvent" , "Set Circle position = $it")
                mapHelper.setCamera(it)
            })

        }
    }
}