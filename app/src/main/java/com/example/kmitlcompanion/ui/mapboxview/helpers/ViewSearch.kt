package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.domain.model.SearchDetail
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import com.example.kmitlcompanion.ui.mainactivity.utils.BottomBarUtils
import com.example.kmitlcompanion.ui.mapboxview.adapter.OnEachSearchClickListener
import com.example.kmitlcompanion.ui.mapboxview.adapter.SearchAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import java.lang.ref.WeakReference
import javax.inject.Inject


class ViewSearch @Inject constructor(
    private val searchAdapter: SearchAdapter,
    private val bottomBarUtils: BottomBarUtils
){
    private lateinit var viewModel : MapboxViewModel
    //private lateinit var pinButtonWeakReference: WeakReference<Button>
    private lateinit var weakTextTagDes: WeakReference<TextView>
    private lateinit var context: Context
    private lateinit var weakSearch: WeakReference<SearchView>
    private lateinit var weakSearchRecyclerView : WeakReference<RecyclerView>
    private lateinit var weakMaterialCardViewBG : WeakReference<MaterialCardView>
    private lateinit var weekMaterialTagCardView : WeakReference<ConstraintLayout>
    private lateinit var weekTextTagDescription : WeakReference<TextView>
    private lateinit var weekClearTagCardView : WeakReference<MaterialCardView>

    private var isSearch : Boolean = false

    fun setup(viewModel: MapboxViewModel, search: SearchView,
              searchRecyclerView: RecyclerView,
              materialCardViewBG : MaterialCardView,
              tagCardView: ConstraintLayout,
              textTagDes : TextView,
              clearTagCardView: MaterialCardView,
              context: Context) {

        this.context = context
        this.viewModel = viewModel
        //this.pinButtonWeakReference = WeakReference(addPinButton)
        this.weakTextTagDes = WeakReference(textTagDes)

        this.weakSearch = WeakReference(search)
        this.weakSearchRecyclerView = WeakReference(searchRecyclerView)
        this.weakMaterialCardViewBG = WeakReference(materialCardViewBG)
        this.weekMaterialTagCardView = WeakReference(tagCardView)
        this.weekTextTagDescription = WeakReference(textTagDes)
        this.weekClearTagCardView = WeakReference(clearTagCardView)

        this.isSearch = false
        viewModel.updateSearchStatus(false)

        // เกิดมา เติมค่า ***************************
        //val myList = (1..100).map { SearchDetail(109, "", "", "", 0, 0) }.toMutableList()
        searchAdapter.submitList(mutableListOf())
        //searchAdapter.notifyDataSetChanged()

        //search view
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                isSearch = (newText != "")

                //add data
                //if (newText != ""){
                    val tagList = mutableListOf<Int?>()
                    viewModel.itemTagList.value?.forEach {
                        if (it.status){
                            tagList.add(it.value.code)
                        }
                    }
                    //viewModel.appendDataToSearchList(newText,tagList)
                    viewModel.submitSearch(newText,tagList)
                //}

                Log.d("test_query","text query")

                return true
            }
        })

        search.setOnQueryTextFocusChangeListener { _, hasFocus ->
            when (hasFocus){
                true -> viewModel.updateSearchStatus(true)//objectVisbility(View.GONE)
                false -> viewModel.updateSearchStatus(false)//objectVisbility(View.VISIBLE)
            }
        }

        searchAdapter.setOnEachClickListener(object : OnEachSearchClickListener{
            override fun onItemClick(searchDetail: SearchDetail,position: Int) {

                search.setQuery(null,true)////*****************
                search.clearFocus()
                viewModel.resetDataToSearchList()

                Log.d("test_ClickSearch",position.toString())
                //viewModel.updatePositionFlyer(Point.fromLngLat(100.523186,13.736717))

                val allMarker = viewModel.mapInformationResponse.value?.mapPoints
                var targetMarker = allMarker?.firstOrNull { searchDetail.id.toLong() == it.id }

                flyToSearchedLocationDetail(name = targetMarker!!.name,
                                    id = targetMarker!!.id.toString(),
                                    place = targetMarker!!.place,
                                    address = targetMarker!!.address,
                                    location = Point.fromLngLat(targetMarker!!.longitude,targetMarker!!.latitude),
                                    description = targetMarker!!.description,
                                    imageList = targetMarker!!.imageLink
                                    )
            }
        })

        //recycler view
        searchRecyclerView.setHasFixedSize(true)
        searchRecyclerView.layoutManager = LinearLayoutManager(context)
        searchRecyclerView.adapter = searchAdapter
        searchRecyclerView.visibility = View.GONE
    }

    fun objectVisbility(status : Int){
        viewModel.updateBottomSheetState(BottomSheetBehavior.STATE_HIDDEN)
        //bottomBarUtils.bottomMap?.visibility = status
        //this.pinButtonWeakReference.get()?.visibility = status

        //this.pinButtonWeakReference.get()?.visibility = if(status == View.VISIBLE) View.GONE else status
        searchRecyclerView?.visibility = status

        materialCardViewBG?.setCardBackgroundColor(
            if(status == View.VISIBLE) context.getColor(R.color.white) else context.getColor(R.color.transparent))

        materialTagCardView?.setBackgroundColor(
            if(status == View.VISIBLE) context.getColor(R.color.white) else context.getColor(R.color.transparent))

        textTagDescription?.visibility = status
        clearTagCardView?.visibility = status
        //bottomBarUtils.bottomMap?.visibility = if(status == View.VISIBLE) View.GONE else status
    }



    fun addToDataList(mList : MutableList<SearchDetail>){
        //Log.d("test_add_to_data_list",mList.toString())
        //val myList = (1..100).map { SearchDetail(109, "", "", "", 0, 0) }.toMutableList()

        if (viewModel.isSearch.value == true){
            Log.d("test_add_isSearch",isSearch.toString())
            //searchAdapter.submitList()
            searchAdapter.submitList(mList)
            searchAdapter.notifyDataSetChanged()
        }

    }

    //@Copy
    private fun flyToSearchedLocationDetail(name : String, id : String, place : String, address : String, location : Point, description : String, imageList : List<String>) {

        viewModel.getDetailsLocationQuery(id)//get marker details

        viewModel.updateIdLocationLabel(id)
        viewModel.updateCurrentLocationGps(location)
        viewModel.updatePositionFlyer(location)

        viewModel.updateNameLocationLabel(name)
        viewModel.updatePlaceLocationLabel(place)
        viewModel.updateAddressLocationLabel(address)
        viewModel.updateDescriptionLocationLabel(description)
        viewModel.updateImageLink(imageList)

        viewModel.updateBottomSheetState(BottomSheetBehavior.STATE_HALF_EXPANDED)
    }

    fun resetDataToList(){
        //var mList = mutableListOf<SearchDetail>()
        //searchAdapter.submitList(mList)
    }

    fun changeTagSearch(){
        if (viewModel.isSearch.value == true){
            val tagList = mutableListOf<Int?>()
            viewModel.itemTagList.value?.forEach {
                if (it.status){
                    tagList.add(it.value.code)
                }
            }
            viewModel.appendDataToSearchList(search?.query.toString(),tagList)
        }
    }

    private val search
        get() = weakSearch?.get()

    private val searchRecyclerView
        get() = weakSearchRecyclerView?.get()

    private val materialCardViewBG
        get() = weakMaterialCardViewBG?.get()

    private val materialTagCardView
        get() = weekMaterialTagCardView?.get()

    private val textTagDescription
        get() = weekTextTagDescription?.get()

    private val clearTagCardView
        get() = weekClearTagCardView?.get()
}