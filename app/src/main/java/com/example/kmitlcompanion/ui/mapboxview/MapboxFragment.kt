package com.example.kmitlcompanion.ui.mapboxview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.background.LocationService
import com.example.kmitlcompanion.databinding.FragmentMapboxBinding
import com.example.kmitlcompanion.domain.model.Comment
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.ui.mainactivity.utils.BottomBarUtils
import com.example.kmitlcompanion.ui.mapboxview.helpers.ViewHelper
import com.example.kmitlcompanion.ui.mapboxview.utils.DateUtils
import com.google.android.gms.location.*
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MapboxFragment : BaseFragment<FragmentMapboxBinding, MapboxViewModel>() {

    @Inject internal lateinit var helper: ViewHelper
    @Inject lateinit var dateUtils: DateUtils
    @Inject lateinit var bottomBarUtils: BottomBarUtils
    //override lateinit var binding: FragmentMapboxBinding
    private var mapView: MapView? = null

    override val viewModel : MapboxViewModel by viewModels()
    private val navArgs by navArgs<MapboxFragmentArgs>()
    override val layoutId :Int = R.layout.fragment_mapbox

    override fun onReady(savedInstanceState: Bundle?) {
        Log.d("Geofence","LocationId from MapboxFragment = ${navArgs.locationId}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapboxBinding.inflate(inflater,container,false).apply {
            this@MapboxFragment.mapView = mapView
            helper.slider.setup(bottomSheet,this@MapboxFragment.viewModel)
            helper.location.setup(this@MapboxFragment.viewModel,requireContext(),mapView)
            helper.comment.setup(this@MapboxFragment.viewModel,rvComment)
            helper.map.setup(this@MapboxFragment.viewModel,mapView) {
                viewModel = this@MapboxFragment.viewModel
                this@MapboxFragment.viewModel.downloadLocations()
            }
            helper.navigation.setup(requireContext(),this@MapboxFragment.viewModel,mapView,soundButton,maneuverView,tripProgressView,recenter,stop,routeOverview,tripProgressCard)
            setupViewObservers()

        }


        //test
        //val btnShow = binding.btnComment
        val btnAddComment = binding.sendCommend
        val recyclerView = binding.rvComment
        //val commend = binding.commend
        //val cm = binding.b


        /*btnShow.text = "Show Comments " + (viewModel.commentList.value?.size ?: 0)
        btnShow.setOnClickListener {
            recyclerView.isVisible = !recyclerView.isVisible
            btnShow.text = if(recyclerView.isVisible) "Hide Comments" else "Show Comments " + (viewModel.commentList.value?.size ?: 0)
        }*/

        btnAddComment.setOnClickListener {
            val id = (viewModel.commentList.value?.size ?: 0) + 1
            viewModel.addComment(Comment(id, dateUtils.getTime(), "test", binding.commend.text.toString()))
            binding.commend.text.clear()
        }


        //show bottom bar
        bottomBarUtils.bottomMap?.visibility = View.VISIBLE


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button2.setOnClickListener {
            view.findNavController().navigate(MapboxFragmentDirections.actionMapboxFragment2ToCreateMapboxLocationFragment2())
        }
    }

    private fun FragmentMapboxBinding.setupViewObservers(){
        lifecycle.addObserver(helper.map)
        lifecycle.addObserver(helper.location)
        lifecycle.addObserver(helper.navigation)
        this@MapboxFragment.viewModel.run {
            mapInformationResponse.observe(viewLifecycleOwner, Observer { information ->
                this@MapboxFragment.context?.let {
                    helper.map.updateMap(it,information,navArgs.locationId)
                }
                helper.geofence.setup(information)
            })
            bottomSheetState.observe(viewLifecycleOwner, Observer {
                helper.slider.setState(it)
                Log.d("Navigation" , "BottomSheet state change to $it")
                bottomBarUtils.sliderState = it
            })
            currentLocationGps.observe(viewLifecycleOwner, Observer {
                currentLocationGpsTv.text = it
            })

            idLocationLabel.observe(viewLifecycleOwner, Observer {
                idLocationLabelTv.text = it
            })
            nameLocationLabel.observe(viewLifecycleOwner, Observer {
                nameLocationLabelTv.text = it
            })
            descriptionLocationLabel.observe(viewLifecycleOwner, Observer {
                descriptionLocationLabelTv.text = it
            })
            placeLocationLabel.observe(viewLifecycleOwner, Observer {
                placeText.text = it
            })
            addressLocationLabel.observe(viewLifecycleOwner, Observer {
                addressText.text = it
            })
            positionFlyer.observe(viewLifecycleOwner, Observer {
                helper.map.flyToLocation(it)
            })

            imageLink.observe(viewLifecycleOwner, Observer {
                helper.list.setupImageAdapter(viewPager2,it?.toMutableList() ?: mutableListOf())
            })
            permissionGrand.observe(viewLifecycleOwner , Observer {
                if (it) {
                    helper.service.setup(requireContext())
                }
            })

            likeCoutingUpdate.observe(viewLifecycleOwner, Observer {
                pinlikeButton.text = it.toString()
            })

            onClicklikeLocation.observe(viewLifecycleOwner, Observer {
                onClicklikeLocation.value?.let { bool ->
                    if (bool){
                        when(isLiked.value!!){
                            true -> removeLikeLocationQuery(idLocationLabel.value)
                            false -> addLikeLocationQuery(idLocationLabel.value)
                        }
                    }
                }
            })

            isLiked.observe(viewLifecycleOwner, Observer {
                it?.let{ bool ->
                    if (bool){
                        pinlikeButton.background.setTint(ContextCompat.getColor(requireContext(),R.color.kmitl_color))
                        pinlikeButton.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                        pinlikeButton.compoundDrawables[0]?.let { btn ->
                            btn.setTint(ContextCompat.getColor(requireContext(),R.color.white))
                        }
                    }else{
                        pinlikeButton.background.setTint(ContextCompat.getColor(requireContext(),R.color.white))
                        pinlikeButton.setTextColor(ContextCompat.getColor(requireContext(),R.color.kmitl_color))
                        pinlikeButton.compoundDrawables[0]?.let { btn ->
                            btn.setTint(ContextCompat.getColor(requireContext(),R.color.kmitl_color))
                        }
                    }
                }
            })

            commentList.observe(viewLifecycleOwner, Observer {
                helper.comment.update(it.toMutableList())
            })


            applicationMode.observe(viewLifecycleOwner , Observer {
                bottomBarUtils.applicationMode = it
                if (it == 0) {
                    Log.d("Navigation" ,"End Navigation")
                    helper.navigation.stopNavigationEvent()
                }
                else if (it == 1) {
                    Log.d("Navigation","Enter ApplicationMode 1")
                    helper.navigation.startNavigation(requireContext())
                }
            })


            recenterEvent.observe(viewLifecycleOwner, Observer {
                helper.navigation.recenterEvent()
            })

            routeOverViewEvent.observe(viewLifecycleOwner , Observer {
                helper.navigation.routeOverviewEvent()
            })

            soundEvent.observe(viewLifecycleOwner, Observer {
                helper.navigation.soundEvent()
            })

            locationIcon.observe(viewLifecycleOwner, Observer {
                helper.location.updatePuckIcon(requireContext(),it)
            })
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView?.onLowMemory()
    }


}