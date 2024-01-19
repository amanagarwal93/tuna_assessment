package com.example.tunaassignment.ui.login

import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.tunaassignment.R
import com.example.tunaassignment.data.model.request.LoginParams
import com.example.tunaassignment.databinding.FragmentLoginBinding
import com.example.tunaassignment.utils.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private var token: String = ""
    private var userId: String = ""
    private var memberId: String = ""
    private var deviceId: String = ""
    private var city: String = ""
    private var countryCode: String = ""

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationPermissionCode = 123

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        requestLocationPermission()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deviceId = getDeviceID()
        viewModel.fetchToken(deviceId)
        viewModel.getToken.observe(viewLifecycleOwner) { it ->
            when (it.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    token = it.data?.token!!
                }
                Result.Status.LOADING -> binding.progressBar.show()
                else -> binding.progressBar.hide()
            }
        }

        binding.guestBtn.setOnClickListener {
            getLocation()
            viewModel.fetchUserLogin(
                token,
                LoginParams(
                    device_id = deviceId,
                    device_type = Constants.DEVICE_ID_PARAM,
                    login_type = Constants.DEVICE_ID_PARAM
                )
            )
            viewModel.getLogin.observe(viewLifecycleOwner) {
                when (it.status) {
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        userId = it.data?.user?.id.toString()
                        memberId =
                            when (it.data?.user?.member_id) {
                                null -> Constants.LOCATION_ID
                                else -> it.data.user.member_id.toString()
                            }
                        val bundle = Bundle()
                        bundle.apply {
                            putString(Constants.TOKEN, token)
                            putString(Constants.USER_ID, userId)
                            putString(Constants.DEVICE_ID, deviceId)
                            putString(Constants.MEMBER_ID, memberId)
                            putString(Constants.CITY, city)
                            putString(Constants.COUNTRY_CODE, countryCode)
                        }
                        binding.guestBtn.findNavController()
                            .navigate(R.id.escapeRoomFragment, bundle)
                    }
                    Result.Status.LOADING -> binding.progressBar.show()
                    else -> binding.progressBar.show()
                }
            }
        }
    }

    private fun getDeviceID() = UUID.randomUUID().toString()

    private fun requestLocationPermission() = // Permission granted, proceed to get location
        // Request permissions
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED -> getLocation()
            else -> ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                locationPermissionCode
            )
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // Permission granted, proceed to get location
        // Permission denied
        when (requestCode) {
            locationPermissionCode -> when {
                grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED -> getLocation()
                else -> requireView().snack(getString(R.string.location_permission_denied))
            }
        }
    }

    private fun getLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        // Handle fine location
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())

                        try {
                            val addresses = geocoder.getFromLocation(
                                it.latitude,
                                it.longitude,
                                1
                            )

                            when {
                                addresses?.isNotEmpty()!! -> {
                                    city = addresses[0].locality
                                    countryCode = addresses[0].countryCode
                                }
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    } ?: run {
                        // location not available
                        requireView().snack(getString(R.string.location_not_available))
                    }
                }
                .addOnFailureListener { e ->
                    // Handle failure
                    requireView().snack(e.message.toString())
                }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
