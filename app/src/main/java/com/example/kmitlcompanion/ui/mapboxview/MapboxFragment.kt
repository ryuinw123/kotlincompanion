package com.example.kmitlcompanion.ui.mapboxview

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentMapboxBinding
import com.example.kmitlcompanion.domain.model.Comment
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.ui.mainactivity.utils.BottomBarUtils
import com.example.kmitlcompanion.ui.mapboxview.helpers.ViewHelper
import com.example.kmitlcompanion.ui.mapboxview.utils.DateUtils
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
            helper.location.setup(this@MapboxFragment.viewModel,mapView)
            helper.comment.setup(this@MapboxFragment.viewModel,rvComment,requireContext(),AlertDialog.Builder(requireContext()))
            //helper.bottomComment.setup(bottomSheetMenu,this@MapboxFragment.viewModel)
            helper.map.setup(this@MapboxFragment.viewModel,mapView) {
                viewModel = this@MapboxFragment.viewModel
                this@MapboxFragment.viewModel.downloadLocations()
            }
            helper.route.setup(this@MapboxFragment.viewModel,requireContext(),mapView.getMapboxMap())

            setupViewObservers()

        }

        val btnAddComment = binding.sendCommend
        val recyclerView = binding.rvComment

        binding.commend.doAfterTextChanged {
            var checkValid = (it.toString().replace(Regex("[\\s\\n]+"), "") != "")
            binding.sendCommend.isEnabled = checkValid
            binding.editCommentBtn.isEnabled = checkValid
        }

        binding.sendCommend.setOnClickListener {
            //val commentId = (viewModel.commentList.value?.size ?: 0) + 1
            viewModel.addComment(Comment(
                0,
                dateUtils.shinGetTime(),
                "",
                binding.commend.text.toString(),
                0,
                0,
                false,
                false,
                true
            ))
            binding.commend.text.clear()
        }

        binding.editCommentBtn.setOnClickListener {
            viewModel.editCommentUpdate(binding.commend.text.toString())
            binding.editCommentBtn.visibility = View.GONE
            binding.cancelEditCommentBtn.visibility = View.GONE
            binding.sendCommend.visibility = View.VISIBLE
            binding.commend.text.clear()
            binding.commend.clearFocus()
        }

        binding.cancelEditCommentBtn.setOnClickListener {
            binding.editCommentBtn.visibility = View.GONE
            binding.cancelEditCommentBtn.visibility = View.GONE
            binding.sendCommend.visibility = View.VISIBLE
            binding.commend.text.clear()
            binding.commend.clearFocus()
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
        lifecycle.addObserver(helper.route)

        this@MapboxFragment.viewModel.run {
            mapInformationResponse.observe(viewLifecycleOwner, Observer { information ->
                this@MapboxFragment.context?.let {
                    helper.map.updateMap(it,information,navArgs.locationId)
                }
                helper.geofence.setup(information)
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

            //For Comment
            commentList.observe(viewLifecycleOwner, Observer {
                binding.editCommentBtn.visibility = View.GONE
                binding.cancelEditCommentBtn.visibility = View.GONE
                binding.sendCommend.visibility = View.VISIBLE
                binding.commend.text.clear()
                binding.commend.clearFocus()
                helper.comment.update(it.toMutableList())
            })
            editComment.observe(viewLifecycleOwner, Observer {
                Log.d("test_bm","hello gon")
                binding.commend.clearFocus()
                editComment?.value?.let {
                    editCommentBtn.visibility = View.VISIBLE
                    cancelEditCommentBtn.visibility = View.VISIBLE
                    sendCommend.visibility = View.GONE
                    if (editComment.value?.message != ""){
                        binding.commend.setText(editComment.value?.message)
                    }
//                    nestedScrollView.post {
//                        nestedScrollView.smoothScrollTo(0,requireView().top - commendArea.top)
//                    }
                }
            })


            //For ???
            navigationEvent.observe(viewLifecycleOwner , Observer {
                val point = Point.fromLngLat(18.04779052,59.293153231)
                helper.route.findRoute(point)
            })
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView?.onLowMemory()
    }


}