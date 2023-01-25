package com.example.kmitlcompanion.ui.mapboxview.helpers

import android.content.Context
import android.location.Location
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.presentation.viewmodel.MapboxViewModel
import com.mapbox.api.directions.v5.models.Bearing
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxMap
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.extensions.applyLanguageAndVoiceUnitOptions
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.base.route.*
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.trip.session.RouteProgressObserver
import com.mapbox.navigation.ui.app.internal.SharedApp
import com.mapbox.navigation.ui.app.internal.navigation.NavigationState
import com.mapbox.navigation.ui.app.internal.navigation.NavigationStateAction
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowApi
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowView
import com.mapbox.navigation.ui.maps.route.arrow.model.RouteArrowOptions
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineOptions
import com.mapbox.navigation.ui.maps.route.line.model.RouteLine
import java.lang.ref.WeakReference
import javax.inject.Inject

class ViewRoute @Inject constructor(
    private val context: Context
) : DefaultLifecycleObserver {
    lateinit var mapboxNavigation: MapboxNavigation
    lateinit var viewModel : MapboxViewModel

    private lateinit var weakMapboxMap: WeakReference<MapboxMap>

    private lateinit var routeLineApi: MapboxRouteLineApi

    private lateinit var routeLineView: MapboxRouteLineView

    private lateinit var routeArrowView: MapboxRouteArrowView

    private val routeArrowApi: MapboxRouteArrowApi = MapboxRouteArrowApi()

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
        /*val maneuvers = maneuverApi.getManeuvers(routeProgress)
        maneuvers.fold(
            { error ->
                Toast.makeText(
                    this@TurnByTurnExperienceActivity,
                    error.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            },
            {
                binding.maneuverView.visibility = View.VISIBLE
                binding.maneuverView.renderManeuvers(maneuvers)
            }
        )*/

// update bottom trip progress summary
        /*binding.tripProgressView.render(
            tripProgressApi.getTripProgress(routeProgress)
        )*/
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

// remove the route reference from camera position evaluations
            //viewportDataSource.clearRouteData()
            //viewportDataSource.evaluate()
        }
    }

    fun setup(viewModel: MapboxViewModel ,context: Context, mapboxMap: MapboxMap) {
        this.viewModel = viewModel
        mapboxNavigation = if (MapboxNavigationProvider.isCreated()) {
            MapboxNavigationProvider.retrieve()
        } else {
            MapboxNavigationProvider.create(
                NavigationOptions.Builder(context)
                    .accessToken(context.getString(R.string.mapbox_access_token))
                    .build()
            )
        }

        weakMapboxMap = WeakReference(mapboxMap)

        val mapboxRouteLineOptions = MapboxRouteLineOptions.Builder(context)
            .withRouteLineBelowLayerId("road-label")
            .build()
        routeLineApi = MapboxRouteLineApi(mapboxRouteLineOptions)
        routeLineView = MapboxRouteLineView(mapboxRouteLineOptions)

        val routeArrowOptions = RouteArrowOptions.Builder(context).build()
        routeArrowView = MapboxRouteArrowView(routeArrowOptions)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        mapboxNavigation.registerRoutesObserver(routesObserver)
        mapboxNavigation.registerRouteProgressObserver(routeProgressObserver)


    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        mapboxNavigation.unregisterRoutesObserver(routesObserver)
        mapboxNavigation.unregisterRouteProgressObserver(routeProgressObserver)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        MapboxNavigationProvider.destroy()
    }

    fun findRoute(destination: Point) {
        val originLocation = viewModel.userLocation.value
        val originPoint = originLocation?.let {
            Point.fromLngLat(it.longitude(), it.latitude())
        } ?: return

        Log.d("Navigation" , "Find route")

// execute a route request
// it's recommended to use the
// applyDefaultNavigationOptions and applyLanguageAndVoiceUnitOptions
// that make sure the route request is optimized
// to allow for support of all of the Navigation SDK features
        mapboxNavigation.requestRoutes(
            routeOptions = RouteOptions
                .builder()
                .applyDefaultNavigationOptions()
                .applyLanguageAndVoiceUnitOptions(context)
                .coordinatesList(listOf(originPoint, destination))
// provide the bearing for the origin of the request to ensure
// that the returned route faces in the direction of the current user movement
                .bearingsList(
                    listOf(
                        Bearing.builder()
                            .degrees(45.0)
                            .build(),
                        null
                    )
                )
                .alternatives(true)
                .layersList(listOf(mapboxNavigation.getZLevel(), null))
                .build(),
            callback = object : NavigationRouterCallback {
                override fun onCanceled(routeOptions: RouteOptions, routerOrigin: RouterOrigin) {
                }

                override fun onFailure(reasons: List<RouterFailure>, routeOptions: RouteOptions) {
                }


                override fun onRoutesReady(
                    routes: List<NavigationRoute>,
                    routerOrigin: RouterOrigin
                ) {
                    mapboxNavigation.setNavigationRoutes(routes)
                    SharedApp.store.dispatch(
                        NavigationStateAction.Update(NavigationState.ActiveNavigation)
                    )
                    Log.d("Navigation" , "Route Ready")
                }

            }
        )
    }

    private val mapboxMap
        get() = weakMapboxMap?.get()






}