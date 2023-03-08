package com.example.kmitlcompanion.ui.mapboxview

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentMapboxBinding
import com.example.kmitlcompanion.domain.model.ScreenSize
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import com.example.kmitlcompanion.ui.BaseFragment
import com.example.kmitlcompanion.ui.mainactivity.utils.BottomBarUtils
import com.example.kmitlcompanion.ui.mapboxview.helpers.ViewHelper
import com.example.kmitlcompanion.ui.mapboxview.utils.DateUtils
import com.mapbox.maps.MapView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.Disposable
import java.text.SimpleDateFormat
import java.util.*
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

    private lateinit var disposable: Disposable

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
            helper.comment.setup(this@MapboxFragment.viewModel,commend, sendCommend, editCommentBtn,
                                cancelEditCommentBtn, rvComment,AlertDialog.Builder(requireContext()))
            helper.search.setup(this@MapboxFragment.viewModel, searchView, searchRecyclerview,
                                materialCardViewBG, materialTagCardView, textTagDescription, clearTagCardView,
                                requireContext())
            helper.bookMark.setup(this@MapboxFragment.viewModel)
            helper.location.setup(this@MapboxFragment.viewModel,requireContext(),mapView)
            helper.map.setup(this@MapboxFragment.viewModel,mapView) {
                viewModel = this@MapboxFragment.viewModel
                this@MapboxFragment.viewModel.downloadLocationsEvent()
                this@MapboxFragment.viewModel.downloadLocations()
            }
            helper.like.setup(this@MapboxFragment.viewModel,requireContext())
            helper.navigation.setup(requireContext(),this@MapboxFragment.viewModel,mapView,soundButton,maneuverView,tripProgressView,recenter,stop,routeOverview,tripProgressCard)
            helper.tag.setup(mapView, this@MapboxFragment.viewModel, tagRecyclerView,clearTagCardView
                , requireContext())
            helper.createSheet.setup(requireContext(),this@MapboxFragment.viewModel)
            helper.editDeleteMarker.setup(this@MapboxFragment.viewModel,requireContext(),editDeleteMarkerButton)
            helper.googleCalendar.setup(this@MapboxFragment.viewModel,requireContext())

            //test
            setupViewObservers()
            //setSeachBarOnQueryTextFocusChange()
        }


        //show bottom bar
        bottomBarUtils.bottomMap?.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button2.setOnClickListener{
            helper.createSheet.openCreateSheetDialog()
        }

        val displayMetrics = requireContext().resources.displayMetrics
        val displayHeight = (displayMetrics.heightPixels / displayMetrics.density).toDouble()
        val displayWidth = (displayMetrics.widthPixels / displayMetrics.density).toDouble()
        viewModel.setScreenSize(ScreenSize(displayWidth,displayHeight))

    }

    private fun FragmentMapboxBinding.setupViewObservers(){
        lifecycle.addObserver(helper.map)
        lifecycle.addObserver(helper.location)
        lifecycle.addObserver(helper.navigation)
        this@MapboxFragment.viewModel.run {
            mapInformationResponse.observe(viewLifecycleOwner, Observer { information ->
                this@MapboxFragment.context?.let {
                    helper.map.updateMap(information)
                }
                //helper.geofence.setup(information)
            })

            mapEventResponse.observe(viewLifecycleOwner, Observer { information ->
                this@MapboxFragment.context?.let {
                    helper.map.updateEvent(information,navArgs.id)
                    helper.location.updateEventPermission(requireContext())
                }
            })

            flyLocationOnStart.observe(viewLifecycleOwner, Observer {
                if (navArgs.id == 0L){
                    helper.map.flyMapOnReady()
                    flyLocationOnStart.removeObservers(viewLifecycleOwner)
                }
            })

            //Refresh Map & Event
            refreshEventLocations.observe(viewLifecycleOwner, Observer {
                if (it == true) {
                    clearItemTag()
                    downloadLocationsEvent()
                    refreshEventLocations.value = false

                }
            })

            refreshMapLocations.observe(viewLifecycleOwner, Observer {
                if (it == true) {
                    clearItemTag()
                    downloadLocations()
                    refreshMapLocations.value = false

                }
            })

            bottomSheetState.observe(viewLifecycleOwner, Observer {
                helper.slider.setState(it)
                Log.d("Navigation" , "BottomSheet state change to $it")
                if (isSearch.value == false){
                    bottomBarUtils.sliderState = it
                }
            })
            currentLocationGps.observe(viewLifecycleOwner, Observer {
                currentLocationGpsTv.text = it
            })

            iconLocation.observe(viewLifecycleOwner, Observer {
                binding.imageView5.setImageResource(it)
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
            pitchFlyer.observe(viewLifecycleOwner, Observer {
                helper.map.flyToPitch(it)
                Log.d("test_pitch",it.toString())
            })

            imageLink.observe(viewLifecycleOwner, Observer {
                helper.list.setupImageAdapter(viewPager2,it?.toMutableList() ?: mutableListOf())
            })

            permissionGrand.observe(viewLifecycleOwner , Observer {
                if (it) {
                    //Log.d("test_noti","gant")
                    helper.service.setup(requireContext(),this@MapboxFragment.viewModel)
                }
            })

            likeCoutingUpdate.observe(viewLifecycleOwner, Observer {
                pinlikeButton.text = it.toString()
            })

            onClicklikeLocation.observe(viewLifecycleOwner, Observer {
                onClicklikeLocation.value?.let { bool ->
                    if (bool){
                        when(isLiked.value!!){
                            true -> helper.like.removeLikeLocationQueryHelper(idLocationLabel.value)
                            false -> helper.like.addLikeLocationQueryHelper(idLocationLabel.value)
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



            //update create bottomsheet event
            createAreaEvent.observe(viewLifecycleOwner,Observer {
                helper.createSheet.dropBottomSheet()
                helper.createSheet.openCreateEventDialog()
            })
            createPinEvent.observe(viewLifecycleOwner,Observer {
                helper.createSheet.dropBottomSheet()
                viewModel?.goToCreateMapBox()
            })
            createCircleAreaEvent.observe(viewLifecycleOwner,Observer {
                helper.createSheet.dropBottomSheet()
                viewModel?.gotoCreateCircleEvent()
            })
            createPolygonAreaEvent.observe(viewLifecycleOwner,Observer {
                helper.createSheet.dropBottomSheet()
                viewModel?.gotoCreatePolygonEvent()
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

            //ForBookmark
            isMarkerBookmarked.observe(viewLifecycleOwner, Observer {
                isMarkerBookmarked?.value?.let {
                    Log.d("test_ismarkerbookmarked",it.toString())
                    if (it){
                        pinbookmarkButton.background.setTint(ContextCompat.getColor(requireContext(),R.color.kmitl_color))
                        pinbookmarkButton.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                        pinbookmarkButton.compoundDrawables[0]?.let { btn ->
                            btn.setTint(ContextCompat.getColor(requireContext(),R.color.white))
                        }
                    }else{
                        pinbookmarkButton.background.setTint(ContextCompat.getColor(requireContext(),R.color.white))
                        pinbookmarkButton.setTextColor(ContextCompat.getColor(requireContext(),R.color.kmitl_color))
                        pinbookmarkButton.compoundDrawables[0]?.let { btn ->
                            btn.setTint(ContextCompat.getColor(requireContext(),R.color.kmitl_color))
                        }
                    }
                }
            })

            onBookmarkClick.observe(viewLifecycleOwner, Observer {
                onBookmarkClick.value?.let { it ->
                    if (it){
                        Log.d("test_event_bookmark","onclick")
                        helper.bookMark.onBookmarkClickHelper()
                        onBookmarkClick.value = false
                    }
                }
            })

            updateBookMark.observe(viewLifecycleOwner, Observer {
                updateBookMark.value?.let {
                    if (it){
                        Log.d("test_updatebookmark","update")
                        //getAllBookMarker()
                        helper.bookMark.updateBookmark(idLocationLabel.value?.toInt(),isMarkerBookmarked.value,eventState.value)
                        updateBookMark.value = false
                    }
                }
            })


            //For Nataviation
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


            //For search
            submitSearchValue.observe(viewLifecycleOwner, Observer {
                Log.d("test_ss","search")
                if (isSearch.value == true){
                    Log.d("test_ss","search api")
                    appendDataToSearchList(it.first,it.second)
                }
            })

            appendSearchList.observe(viewLifecycleOwner, Observer {
                helper.search.addToDataList(it)
            })

            resetSearchList.observe(viewLifecycleOwner, Observer {
                helper.search.resetDataToList()
            })

            isSearch.observe(viewLifecycleOwner, Observer {
                if (it){
                    val tagList = mutableListOf<Int?>()
                    itemTagList.value?.forEach {
                        if (it.status){
                            tagList.add(it.value.code)
                        }
                    }
                    button2.visibility = View.GONE
                    resetPosition.visibility = View.GONE
                    tdPosition.visibility = View.GONE
                    appendDataToSearchList("",tagList)
                    helper.search.objectVisbility(View.VISIBLE)
                    bottomBarUtils.bottomMap?.visibility = View.GONE
                }else {
                    helper.search.objectVisbility(View.GONE)
                    bottomBarUtils.bottomMap?.visibility = View.VISIBLE
                    button2.visibility = View.VISIBLE
                    resetPosition.visibility = View.VISIBLE
                    tdPosition.visibility = View.VISIBLE
                }
            })


            //For Tag
            itemTagList.observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()){
                    if (it.last().value.code == 999999){
                        it.removeLast()
                        helper.tag.addItem(it)
                        helper.search.changeTagSearch()
                        if (isSearch.value == false){
                            helper.tag.setTagDisplay(it)
                        }
                        Log.d("test_additemTag","right here")
                    }
                }
            })

            tagFly.observe(viewLifecycleOwner, Observer {
                helper.map.flyToLocation(it.first,it.second,0.0,pitchFlyer.value!!,2000)
            })


            //For Event
            eventState.observe(viewLifecycleOwner, Observer {
                val visibility = if (it == true) View.GONE else View.VISIBLE
                binding.eventTimeLayout.visibility = if (it == false) View.GONE else View.VISIBLE
                binding.navigation.visibility = visibility
                binding.placeText.visibility = visibility
                binding.addressText.visibility = visibility
                binding.commentZone.visibility = visibility
                binding.constraintLayout3.visibility = visibility
                binding.pinToGoogleCalendar.visibility = if (it == false) View.GONE else View.VISIBLE
            })

            eventBindEnd.observe(viewLifecycleOwner, Observer {
                binding.eventEndValue.text = it
            })

            eventBindStart.observe(viewLifecycleOwner, Observer {
                binding.eventStartValue.text = it
            })

            displayTimer.observe(viewLifecycleOwner, Observer {
                //Log.d("test_time",it)
                eventRemainValue.text = it
                //binding.eventStartValue.text = it
            })

            //Edit Delte Marker
            isMyPin.observe(viewLifecycleOwner, Observer {
                if (it != null){
                    binding.editDeleteMarkerButton.visibility = if(it == true) View.VISIBLE else View.GONE
                }
                Log.d("test_mypin",isMyPin.value.toString())
            })

            createdUserName.observe(viewLifecycleOwner, Observer {
                binding.createdUserName.text = it
            })

            showMarkerMenuEvent.observe(viewLifecycleOwner, Observer {
                if (it.peekContent()){
                    helper.editDeleteMarker.showMarkerMenu()
                    showMarkerMenuEvent.value = com.example.kmitlcompanion.presentation.eventobserver.Event(content = false)
                }
            })

            confirmDeleteMarkerDialog.observe(viewLifecycleOwner, Observer {
                if (it == true){
                    helper.editDeleteMarker.showConfirmDeleteDialog()
                    confirmDeleteMarkerDialog.value = false
                    Log.d("test_bug",confirmDeleteMarkerDialog.value.toString())
                }
            })

            editMarkerTrigger.observe(viewLifecycleOwner, Observer {
                if (it == true){
                    helper.editDeleteMarker.hideMarkerMenu()
                    if (eventState.value == true){
                        goToEditEvent()
                    }else if (eventState.value == false){
                        goToEditMarker()
                    }
                    editMarkerTrigger.value = false
                }
            })

            //For open google calendar
            triggerGoogleCalender.observe(viewLifecycleOwner, Observer {
                if (it == true){
                    helper.googleCalendar.startGoogleCalendar()
                    triggerGoogleCalender.value = false
                }
            })

        }
    }


    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView?.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("test_debug" ,"onDestroyView")

        // Clear the reference to the MapboxMap object
//        this@MapboxFragment.mapView.let {
//            it
//        }
        mapView?.onDestroy()
        mapView = null

    }

    override fun onDestroy() {
        mapView = null
        Log.d("test_debug" ,"onDestroy")
        super.onDestroy()
    }





}