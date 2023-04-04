package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Location
import android.support.v4.os.IResultReceiver.Default
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import com.example.kmitlcompanion.ui.mapboxview.utils.LocationUtils
import com.example.kmitlcompanion.ui.mapboxview.utils.ToasterUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.models.Bearing
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.bindgen.Expected
import com.mapbox.geojson.Point
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.Image
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.navigation.base.TimeFormat
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.extensions.applyLanguageAndVoiceUnitOptions
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.base.route.RouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.base.route.RouterOrigin
import com.mapbox.navigation.base.trip.model.RouteLegProgress
import com.mapbox.navigation.base.trip.model.RouteProgress
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.core.arrival.ArrivalObserver
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.formatter.MapboxDistanceFormatter
import com.mapbox.navigation.core.replay.MapboxReplayer
import com.mapbox.navigation.core.replay.ReplayLocationEngine
import com.mapbox.navigation.core.replay.route.ReplayProgressObserver
import com.mapbox.navigation.core.replay.route.ReplayRouteMapper
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.mapbox.navigation.core.trip.session.RouteProgressObserver
import com.mapbox.navigation.core.trip.session.VoiceInstructionsObserver
import com.mapbox.navigation.ui.base.util.MapboxNavigationConsumer
import com.mapbox.navigation.ui.maneuver.api.MapboxManeuverApi
import com.mapbox.navigation.ui.maneuver.view.MapboxManeuverView
import com.mapbox.navigation.ui.maps.camera.NavigationCamera
import com.mapbox.navigation.ui.maps.camera.data.MapboxNavigationViewportDataSource
import com.mapbox.navigation.ui.maps.camera.lifecycle.NavigationBasicGesturesHandler
import com.mapbox.navigation.ui.maps.camera.state.NavigationCameraState
import com.mapbox.navigation.ui.maps.camera.transition.NavigationCameraTransitionOptions
import com.mapbox.navigation.ui.maps.camera.view.MapboxRecenterButton
import com.mapbox.navigation.ui.maps.camera.view.MapboxRouteOverviewButton
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowApi
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowView
import com.mapbox.navigation.ui.maps.route.arrow.model.RouteArrowOptions
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineOptions
import com.mapbox.navigation.ui.maps.route.line.model.RouteLine
import com.mapbox.navigation.ui.tripprogress.api.MapboxTripProgressApi
import com.mapbox.navigation.ui.tripprogress.model.*
import com.mapbox.navigation.ui.tripprogress.view.MapboxTripProgressView
import com.mapbox.navigation.ui.voice.api.MapboxSpeechApi
import com.mapbox.navigation.ui.voice.api.MapboxVoiceInstructionsPlayer
import com.mapbox.navigation.ui.voice.model.SpeechAnnouncement
import com.mapbox.navigation.ui.voice.model.SpeechError
import com.mapbox.navigation.ui.voice.model.SpeechValue
import com.mapbox.navigation.ui.voice.model.SpeechVolume
import com.mapbox.navigation.ui.voice.view.MapboxSoundButton
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.util.*
import javax.inject.Inject

class ViewNavigation @Inject constructor(
    private val toasterUtil: ToasterUtil,
    private val applicationContext : Context,
    private val locationUtils: LocationUtils
) : DefaultLifecycleObserver {

    private companion object {
        private const val BUTTON_ANIMATION_DURATION = 1500L
    }

    private val mapboxReplayer = MapboxReplayer()
    private val replayLocationEngine = ReplayLocationEngine(mapboxReplayer)
    private val replayProgressObserver = ReplayProgressObserver(mapboxReplayer)
    private lateinit var weakMapboxMap : WeakReference<MapboxMap>
    private lateinit var weakMapView: WeakReference<MapView>
    private lateinit var weakSoundButton : WeakReference<MapboxSoundButton>
    private lateinit var weakManeuverView : WeakReference<MapboxManeuverView>
    private lateinit var weakTripProgressView : WeakReference<MapboxTripProgressView>
    private lateinit var weakRecenter : WeakReference<MapboxRecenterButton>
    private lateinit var weakStop: WeakReference<ImageView>
    private lateinit var weakRouteOverview : WeakReference<MapboxRouteOverviewButton>
    private lateinit var weakTripProgressCard : WeakReference<CardView>
    private lateinit var mapboxNavigation: MapboxNavigation
    private lateinit var navigationCamera: NavigationCamera
    private lateinit var viewportDataSource: MapboxNavigationViewportDataSource
    private lateinit var viewModel : MapboxViewModel

    private var navigateState = false

    private val pixelDensity = Resources.getSystem().displayMetrics.density
    private val overviewPadding: EdgeInsets by lazy {
        EdgeInsets(
            140.0 * pixelDensity,
            40.0 * pixelDensity,
            120.0 * pixelDensity,
            40.0 * pixelDensity
        )
    }
    private val landscapeOverviewPadding: EdgeInsets by lazy {
        EdgeInsets(
            30.0 * pixelDensity,
            380.0 * pixelDensity,
            110.0 * pixelDensity,
            20.0 * pixelDensity
        )
    }
    private val followingPadding: EdgeInsets by lazy {
        EdgeInsets(
            180.0 * pixelDensity,
            40.0 * pixelDensity,
            150.0 * pixelDensity,
            40.0 * pixelDensity
        )
    }
    private val landscapeFollowingPadding: EdgeInsets by lazy {
        EdgeInsets(
            30.0 * pixelDensity,
            380.0 * pixelDensity,
            110.0 * pixelDensity,
            40.0 * pixelDensity
        )
    }
    private lateinit var maneuverApi: MapboxManeuverApi
    private lateinit var tripProgressApi: MapboxTripProgressApi
    private lateinit var routeLineApi: MapboxRouteLineApi
    private lateinit var routeLineView: MapboxRouteLineView
    private val routeArrowApi: MapboxRouteArrowApi = MapboxRouteArrowApi()
    private lateinit var routeArrowView: MapboxRouteArrowView
    private var isVoiceInstructionsMuted = false
        set(value) {
            field = value
            if (value) {
                soundButton?.muteAndExtend(BUTTON_ANIMATION_DURATION)
                voiceInstructionsPlayer.volume(SpeechVolume(0f))
            } else {
                soundButton?.unmuteAndExtend(BUTTON_ANIMATION_DURATION)
                voiceInstructionsPlayer.volume(SpeechVolume(1f))
            }
        }
    private lateinit var speechApi: MapboxSpeechApi
    private lateinit var voiceInstructionsPlayer: MapboxVoiceInstructionsPlayer
    private val voiceInstructionsObserver = VoiceInstructionsObserver { voiceInstructions ->
        speechApi.generate(voiceInstructions, speechCallback)
    }
    private val speechCallback =
        MapboxNavigationConsumer<Expected<SpeechError, SpeechValue>> { expected ->
            expected.fold(
                { error ->
// play the instruction via fallback text-to-speech engine
                    voiceInstructionsPlayer.play(
                        error.fallback,
                        voiceInstructionsPlayerCallback
                    )
                },
                { value ->
// play the sound file from the external generator
                    voiceInstructionsPlayer.play(
                        value.announcement,
                        voiceInstructionsPlayerCallback
                    )
                }
            )
        }
    private val voiceInstructionsPlayerCallback =
        MapboxNavigationConsumer<SpeechAnnouncement> { value ->
// remove already consumed file to free-up space
            speechApi.clean(value)
        }

    private val navigationLocationProvider = NavigationLocationProvider()
    private val locationObserver = object : LocationObserver {
        var firstLocationUpdateReceived = false

        override fun onNewRawLocation(rawLocation: Location) {
// not handled
        }

        override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
            val enhancedLocation = locationMatcherResult.enhancedLocation
// update location puck's position on the map
            navigationLocationProvider.changePosition(
                location = enhancedLocation,
                keyPoints = locationMatcherResult.keyPoints,
            )

// update camera position to account for new location
            viewportDataSource.onLocationChanged(enhancedLocation)
            viewportDataSource.evaluate()

// if this is the first location update the activity has received,
// it's best to immediately move the camera to the current user location
            if (!firstLocationUpdateReceived) {
                firstLocationUpdateReceived = true
                navigationCamera.requestNavigationCameraToOverview(
                    stateTransitionOptions = NavigationCameraTransitionOptions.Builder()
                        .maxDuration(0) // instant transition
                        .build()
                )
            }
        }
    }

    private val routeProgressObserver = RouteProgressObserver { routeProgress ->
// update the camera position to account for the progressed fragment of the route
        //viewportDataSource.onRouteProgressChanged(routeProgress)
        //viewportDataSource.evaluate()

// draw the upcoming maneuver arrow on the map
        val style = mapboxMap?.getStyle()
        if (style != null) {
            val maneuverArrowResult = routeArrowApi.addUpcomingManeuverArrow(routeProgress)
            routeArrowView.renderManeuverUpdate(style, maneuverArrowResult)
        }

// update top banner with maneuver instructions
        val maneuvers = maneuverApi.getManeuvers(routeProgress)
        maneuvers.fold(
            { error ->
                toasterUtil.showToast(error.errorMessage,Toast.LENGTH_SHORT)
            },
            {
                if (viewModel.applicationMode.value == 1) {
                    maneuverView?.visibility = View.VISIBLE
                    maneuverView!!.renderManeuvers(maneuvers)
                }
            }
        )

// update bottom trip progress summary
        tripProgressView?.render(
            tripProgressApi.getTripProgress(routeProgress)
        )
    }
    private val routesObserver = RoutesObserver { routeUpdateResult ->
        if (routeUpdateResult.routes.isNotEmpty()) {
// generate route geometries asynchronously and render them
            val routeLines = routeUpdateResult.routes.map { RouteLine(it, null) }

            routeLineApi.setRoutes(
                routeLines
            ) { value ->
                mapboxMap?.getStyle()?.apply {
                    routeLineView.renderRouteDrawData(this, value)
                }
            }

// update the camera position to account for the new route
            //viewportDataSource.onRouteChanged(routeUpdateResult.routes.first())
            //viewportDataSource.evaluate()
        } else {
// remove the route line and route arrow from the map
            val style = mapboxMap?.getStyle()
            if (style != null) {
                routeLineApi.clearRouteLine { value ->
                    routeLineView.renderClearRouteLineValue(
                        style,
                        value
                    )
                }
                routeArrowView.render(style, routeArrowApi.clearArrows())
            }
        }
    }

    private val arrivalObserver = object : ArrivalObserver {

        override fun onWaypointArrival(routeProgress: RouteProgress) {
            // do something when the user arrives at a waypoint
            Log.d("Navigation", "WayPoint Arrive")
        }

        override fun onNextRouteLegStart(routeLegProgress: RouteLegProgress) {
            // do something when the user starts a new leg
            Log.d("Navigation", "Next Arrive")
        }

        override fun onFinalDestinationArrival(routeProgress: RouteProgress) {
            Log.d("Navigation", "Final Arrive")
            viewModel.updateApplicationMode(0)
        }
    }

    @SuppressLint("MissingPermission")
    fun setup(context: Context, viewModel: MapboxViewModel, mapView: MapView, soundButton: MapboxSoundButton, maneuverView: MapboxManeuverView, tripProgressView: MapboxTripProgressView , mapboxRecenterButton: MapboxRecenterButton,stop : ImageView , routeOverviewButton: MapboxRouteOverviewButton , tripProgressCard : CardView) {
        this.viewModel = viewModel
        this.weakMapboxMap = WeakReference(mapView.getMapboxMap())
        this.weakMapView = WeakReference(mapView)
        this.weakSoundButton = WeakReference(soundButton)
        this.weakManeuverView = WeakReference(maneuverView)
        this.weakTripProgressView = WeakReference(tripProgressView)
        this.weakRecenter = WeakReference(mapboxRecenterButton)
        this.weakStop = WeakReference(stop)
        this.weakTripProgressCard = WeakReference(tripProgressCard)
        this.weakRouteOverview = WeakReference(routeOverviewButton)

        mapView.location.setLocationProvider(navigationLocationProvider)

        mapboxNavigation = if (MapboxNavigationProvider.isCreated()) {
            MapboxNavigationProvider.retrieve()
        } else {
            MapboxNavigationProvider.create(
                NavigationOptions.Builder(applicationContext)
                    .accessToken(context.getString(R.string.mapbox_access_token))
// comment out the location engine setting block to disable simulation
                    //.locationEngine(replayLocationEngine)
                    .build()
            )
        }
        viewportDataSource = MapboxNavigationViewportDataSource(mapboxMap!!)

        navigationCamera = NavigationCamera(
            mapboxMap!!,
            mapView.camera,
            viewportDataSource
        )

        mapView.camera.addCameraAnimationsLifecycleListener(
            NavigationBasicGesturesHandler(navigationCamera)
        )

        navigationCamera.registerNavigationCameraStateChangeObserver { navigationCameraState ->
// shows/hide the recenter button depending on the camera state
            when (navigationCameraState) {
                NavigationCameraState.TRANSITION_TO_FOLLOWING,
                NavigationCameraState.FOLLOWING -> recenter?.visibility = View.INVISIBLE
                NavigationCameraState.TRANSITION_TO_OVERVIEW,
                NavigationCameraState.OVERVIEW,
                NavigationCameraState.IDLE -> recenter?.visibility = if(!navigateState) View.INVISIBLE else View.VISIBLE
            }
        }
// set the padding values depending on screen orientation and visible view layout
        if (context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewportDataSource.overviewPadding = landscapeOverviewPadding
        } else {
            viewportDataSource.overviewPadding = overviewPadding
        }
        if (context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewportDataSource.followingPadding = landscapeFollowingPadding
        } else {
            viewportDataSource.followingPadding = followingPadding
        }

        val distanceFormatterOptions = mapboxNavigation.navigationOptions.distanceFormatterOptions
        maneuverApi = MapboxManeuverApi(
            MapboxDistanceFormatter(distanceFormatterOptions)
        )
        tripProgressApi = MapboxTripProgressApi(
            TripProgressUpdateFormatter.Builder(context)
                .distanceRemainingFormatter(
                    DistanceRemainingFormatter(distanceFormatterOptions)
                )
                .timeRemainingFormatter(
                    TimeRemainingFormatter(context)
                )
                .percentRouteTraveledFormatter(
                    PercentDistanceTraveledFormatter()
                )
                .estimatedTimeToArrivalFormatter(
                    EstimatedTimeToArrivalFormatter(context, TimeFormat.NONE_SPECIFIED)
                )
                .build()
        )
        speechApi = MapboxSpeechApi(
            context,
            context.getString(R.string.mapbox_access_token),
            Locale.US.language
        )
        voiceInstructionsPlayer = MapboxVoiceInstructionsPlayer(
            context,
            context.getString(R.string.mapbox_access_token),
            Locale.US.language
        )
        val mapboxRouteLineOptions = MapboxRouteLineOptions.Builder(context)
            .withVanishingRouteLineEnabled(true)
            .withRouteLineBelowLayerId("road-label")
            .build()
        routeLineApi = MapboxRouteLineApi(mapboxRouteLineOptions)
        routeLineView = MapboxRouteLineView(mapboxRouteLineOptions)

        val routeArrowOptions = RouteArrowOptions.Builder(context).build()
        routeArrowView = MapboxRouteArrowView(routeArrowOptions)



        this.soundButton?.unmute()

        mapboxNavigation.startTripSession()


    }

    fun soundEvent() {
        isVoiceInstructionsMuted = !isVoiceInstructionsMuted
    }

    fun routeOverviewEvent() {
        navigationCamera.requestNavigationCameraToOverview()
        recenter?.showTextAndExtend(BUTTON_ANIMATION_DURATION)
    }

    fun stopNavigationEvent() {
        viewModel.updateLocationIcon(ViewLocation.MODE_NORMAL)
        clearRouteAndStopNavigation()
    }

    fun recenterEvent() {
        navigationCamera.requestNavigationCameraToFollowing()
        routeOverview?.showTextAndExtend(BUTTON_ANIMATION_DURATION)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        mapboxNavigation.registerRoutesObserver(routesObserver)
        mapboxNavigation.registerRouteProgressObserver(routeProgressObserver)
        mapboxNavigation.registerLocationObserver(locationObserver)
        mapboxNavigation.registerVoiceInstructionsObserver(voiceInstructionsObserver)
        mapboxNavigation.registerRouteProgressObserver(replayProgressObserver)
        mapboxNavigation.registerArrivalObserver(arrivalObserver)

        if (mapboxNavigation.getRoutes().isEmpty()) {
// if simulation is enabled (ReplayLocationEngine set to NavigationOptions)
// but we're not simulating yet,
// push a single location sample to establish origin
            mapboxReplayer.pushEvents(
                listOf(
                    ReplayRouteMapper.mapToUpdateLocation(
                        eventTimestamp = 0.0,
                        point = Point.fromLngLat( 100.77889748563068,13.730149446823802)
                    )
                )
            )
            mapboxReplayer.playFirstLocation()
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        mapboxNavigation.unregisterRoutesObserver(routesObserver)
        mapboxNavigation.unregisterRouteProgressObserver(routeProgressObserver)
        mapboxNavigation.unregisterLocationObserver(locationObserver)
        mapboxNavigation.unregisterVoiceInstructionsObserver(voiceInstructionsObserver)
        mapboxNavigation.unregisterRouteProgressObserver(replayProgressObserver)
        mapboxNavigation.unregisterArrivalObserver(arrivalObserver)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        MapboxNavigationProvider.destroy()
        mapboxReplayer.finish()
        maneuverApi.cancel()
        routeLineApi.cancel()
        routeLineView.cancel()
        speechApi.cancel()
        voiceInstructionsPlayer.shutdown()
    }

    fun startNavigation(context: Context) {
        viewModel.updateLocationIcon(ViewLocation.MODE_NAVIGATION)
        viewModel.updateBottomSheetState(BottomSheetBehavior.STATE_HIDDEN)
        val locationId = viewModel.idLocationLabel.value?.toLong()
        val locationLst = viewModel.mapInformationResponse.value?.mapPoints
        Log.d("Navigation" , "locationId = $locationId , locationLst = $locationLst")
        locationId?.let { locationLst?.let { it1 ->
            val destination = locationUtils.queryLocation(it, it1)
            val point = Point.fromLngLat(destination!!.longitude,destination!!.latitude)
            findRoute(context,point)
        } }
    }

    private fun findRoute(context: Context,destination: Point) {
        val originLocation = navigationLocationProvider.lastLocation
        val originPoint = originLocation?.let {
            Point.fromLngLat(it.longitude, it.latitude)
        } ?: return

// execute a route request
// it's recommended to use the
// applyDefaultNavigationOptions and applyLanguageAndVoiceUnitOptions
// that make sure the route request is optimized
// to allow for support of all of the Navigation SDK features
        mapboxNavigation.requestRoutes(
            RouteOptions.builder()
                .applyDefaultNavigationOptions(DirectionsCriteria.PROFILE_WALKING)
                .applyLanguageAndVoiceUnitOptions(context)
                //.alternatives(true)
                .coordinatesList(listOf(originPoint, destination))
// provide the bearing for the origin of the request to ensure
// that the returned route faces in the direction of the current user movement
                .bearingsList(
                    listOf(
                        Bearing.builder()
                            .angle(originLocation.bearing.toDouble())
                            .degrees(45.0)
                            .build(),
                        null
                    )
                )
                .layersList(listOf(mapboxNavigation.getZLevel(), null))
                .build(),
            object : RouterCallback {
                override fun onRoutesReady(
                    routes: List<DirectionsRoute>,
                    routerOrigin: RouterOrigin
                ) {
                    setRouteAndStartNavigation(routes)
                }

                override fun onFailure(
                    reasons: List<RouterFailure>,
                    routeOptions: RouteOptions
                ) {
// no impl
                }

                override fun onCanceled(routeOptions: RouteOptions, routerOrigin: RouterOrigin) {
// no impl
                }
            }
        )
    }

    private fun setRouteAndStartNavigation(routes: List<DirectionsRoute>) {
// set routes, where the first route in the list is the primary route that
// will be used for active guidance
        mapboxNavigation.setRoutes(routes)

// start location simulation along the primary route
        startSimulation(routes.first())

// show UI elements
        soundButton?.visibility = View.VISIBLE
        routeOverview?.visibility = View.VISIBLE
        tripProgressCard?.visibility = View.VISIBLE
        recenter?.visibility = View.VISIBLE
        navigateState = true
        mapView?.compass?.enabled = false

// move the camera to overview when new route is available
        navigationCamera.requestNavigationCameraToOverview()
    }

    private fun clearRouteAndStopNavigation() {
// clear
        mapboxNavigation.setRoutes(listOf())

// stop simulation
        mapboxReplayer.stop()

// hide UI elements
        soundButton?.visibility = View.INVISIBLE
        maneuverView?.visibility = View.INVISIBLE
        routeOverview?.visibility = View.INVISIBLE
        tripProgressCard?.visibility = View.INVISIBLE
        recenter?.visibility = View.INVISIBLE
        navigateState = false
        mapView?.compass?.enabled = true

    }

    private fun startSimulation(route: DirectionsRoute) {
        mapboxReplayer.run {
            stop()
            clearEvents()
            val replayEvents = ReplayRouteMapper().mapDirectionsRouteGeometry(route)
            pushEvents(replayEvents)
            if (!replayEvents.isNullOrEmpty()){
                seekTo(replayEvents.first())
            }

            play()
        }
    }



    private val mapboxMap
        get() = weakMapboxMap?.get()

    private val mapView
        get() = weakMapView?.get()

    private val soundButton
        get() = weakSoundButton?.get()

    private val maneuverView
        get() = weakManeuverView?.get()

    private val tripProgressView
        get() = weakTripProgressView?.get()

    private val recenter
        get() = weakRecenter?.get()

    private val stop
        get() = weakStop?.get()

    private val routeOverview
        get() = weakRouteOverview?.get()

    private val tripProgressCard
        get() = weakTripProgressCard?.get()

}