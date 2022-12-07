package com.example.kmitlcompanion.ui.mapboxview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.databinding.FragmentMapboxBinding
import com.example.kmitlcompanion.domain.model.Comment
import com.example.kmitlcompanion.ui.mapboxview.helpers.ViewHelper
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import com.example.kmitlcompanion.ui.mainactivity.utils.BottomBarUtils
import com.example.kmitlcompanion.ui.mapboxview.utils.DateUtils
import com.mapbox.maps.MapView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MapboxFragment : BaseFragment<FragmentMapboxBinding, MapboxViewModel>() {

    @Inject internal lateinit var helper: ViewHelper
    @Inject lateinit var dateUtils: DateUtils
    @Inject lateinit var bottomBarUtils: BottomBarUtils
    //override lateinit var binding: FragmentMapboxBinding
    private var mapView: MapView? = null

    override val viewModel : MapboxViewModel by viewModels()


    override val layoutId :Int = R.layout.fragment_mapbox

    override fun onReady(savedInstanceState: Bundle?) {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapboxBinding.inflate(inflater,container,false).apply {
            this@MapboxFragment.mapView = mapView
            helper.slider.setup(bottomSheet,this@MapboxFragment.viewModel)
            helper.map.setup(this@MapboxFragment.viewModel,mapView) {
                viewModel = this@MapboxFragment.viewModel
                this@MapboxFragment.viewModel.downloadLocations()
            }
            helper.comment.setup(this@MapboxFragment.viewModel,rvComment)


            setupViewObservers()
        }

        //test
        val btnShow = binding.btnComment
        val btnAddComment = binding.sendCommend
        val recyclerView = binding.rvComment
        val commend = binding.commend


        btnShow.text = "Show Comments " + (viewModel.commentList.value?.size ?: 0)
        btnShow.setOnClickListener {
            recyclerView.isVisible = !recyclerView.isVisible
            btnShow.text = if(recyclerView.isVisible) "Hide Comments" else "Show Comments " + (viewModel.commentList.value?.size ?: 0)
        }

        btnAddComment.setOnClickListener {
            val id = (viewModel.commentList.value?.size ?: 0) + 1
            viewModel.addComment(Comment(id, dateUtils.getTime(), "test", commend.text.toString()))
            commend.text.clear()
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
        this@MapboxFragment.viewModel.run {
            mapInformationResponse.observe(viewLifecycleOwner, Observer { information ->
                this@MapboxFragment.context?.let {
                    helper.map.updateMap(it,information)
                }
            })
            bottomSheetState.observe(viewLifecycleOwner, Observer {
                helper.slider.setState(it)
                if (it == 5)
                    bottomBarUtils.bottomMap?.visibility = View.VISIBLE
                else
                    bottomBarUtils.bottomMap?.visibility = View.INVISIBLE
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
            positionFlyer.observe(viewLifecycleOwner, Observer {
                helper.map.flyToLocation(it)
            })
            commentList.observe(viewLifecycleOwner, Observer {
                helper.comment.update(it.toMutableList())
            })
            imageLink.observe(viewLifecycleOwner, Observer {
                helper.list.setupImageAdapter(viewPager2,it?.toMutableList() ?: mutableListOf())
            })
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView?.onLowMemory()
    }



}