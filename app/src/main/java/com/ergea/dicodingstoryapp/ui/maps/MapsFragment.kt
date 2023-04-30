package com.ergea.dicodingstoryapp.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ergea.dicodingstoryapp.R
import com.ergea.dicodingstoryapp.databinding.FragmentMapsBinding
import com.ergea.dicodingstoryapp.wrapper.Resource
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MapsViewModel by viewModels()
    private val boundsBuilder = LatLngBounds.Builder()
    private lateinit var mMap: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        getMyLocation()
        observeData()
        setMapStyle()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getToken().observe(viewLifecycleOwner) {
            viewModel.getAllStories(it, withLocation = 1)
        }
        back()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun back() {
        binding.icBackBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun observeData() {
        viewModel.stories.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.apply {
                        map.isVisible = true
                        animationError.isVisible = false
                        animationEmpty.isVisible = false
                    }
                }
                is Resource.Error -> {
                    binding.apply {
                        map.isVisible = false
                        animationError.isVisible = true
                        animationEmpty.isVisible = false
                    }
                }
                is Resource.Success -> {
                    binding.apply {
                        map.isVisible = true
                        animationError.isVisible = false
                        animationEmpty.isVisible = false
                    }
                    it.data?.listStory?.forEach { story ->
                        val latLng = LatLng(
                            story.lat.toString().toDouble(),
                            story.lon.toString().toDouble()
                        )
                        val marker = mMap.addMarker(
                            MarkerOptions().position(latLng)
                                .title("Story Uploaded by ${story.name}")
                                .snippet(getAddressName(latLng.latitude, latLng.longitude))
                        )
                        marker?.tag = story.id
                        boundsBuilder.include(latLng)
                    }
                    val bounds: LatLngBounds = boundsBuilder.build()
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            bounds,
                            resources.displayMetrics.widthPixels,
                            resources.displayMetrics.heightPixels,
                            300
                        )
                    )
                    mMap.setOnInfoWindowClickListener { marker ->
                        val directions =
                            MapsFragmentDirections.actionMapsFragmentToDetailStoryFragment(marker.tag.toString())
                        findNavController().navigate(directions)
                    }
                }
                is Resource.Empty -> {
                    binding.apply {
                        map.isVisible = false
                        animationError.isVisible = false
                        animationEmpty.isVisible = true
                    }
                }
            }
        }
    }

    private fun setMapStyle() {
        viewModel.getTheme().observe(viewLifecycleOwner) {
            try {
                if (it == true) {
                    val success =
                        mMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                requireContext(),
                                R.raw.map_style
                            )
                        )
                    if (!success) {
                        Log.e("MAPS", "Style parsing failed.")
                    }
                } else {
                    val success =
                        mMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                requireContext(),
                                R.raw.map_style_standard
                            )
                        )
                    if (!success) {
                        Log.e("MAPS", "Style parsing failed.")
                    }
                }
            } catch (exception: Exception) {
                Log.e("MAPS", "Can't find style. Error: ", exception)
            }
        }
    }


    private fun getAddressName(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return addressName
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}