package com.example.kmitlcompanion.ui.mapboxview

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.DrawableRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.databinding.FragmentMapboxBinding
import com.example.kmitlcompanion.domain.model.Comment
import com.example.kmitlcompanion.ui.mapboxview.helpers.ViewHelper
import com.example.kmitlcompanion.presentation.MapboxViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.kmitlcompanion.ui.mapboxview.adapter.CommentAdapter
import com.example.kmitlcompanion.ui.mapboxview.adapter.CommentClickListener
import com.example.kmitlcompanion.ui.mapboxview.utils.DateUtils
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MapboxFragment : BaseFragment<FragmentMapboxBinding, MapboxViewModel>() {

    @Inject internal lateinit var helper: ViewHelper
    @Inject lateinit var dateUtils: DateUtils
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
            helper.slider.setup(bottomSheet)
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
        val bottomNavigationView = requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator_bottom_nav)
        bottomNavigationView.visibility = View.VISIBLE

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
                    val mock = ArrayList<Int>()
                    mock.add(R.drawable.red_marker)
                    mock.add(R.drawable.wave_bar)
                    mock.add(R.drawable.ic_assistant_navigation_fill0_wght300_grad0_opsz48)
                    mock.add(R.drawable.ic_launcher_background)
                    helper.list.setupImageAdapter(viewPager2,mock)
                }
            })
            bottomSheetState.observe(viewLifecycleOwner, Observer {
                helper.slider.setState(it)
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
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView?.onLowMemory()
    }



}