package com.example.tunaassignment.ui.escapeRoom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tunaassignment.R
import com.example.tunaassignment.data.listeners.RoomClick
import com.example.tunaassignment.data.model.request.EscapeRoomParams
import com.example.tunaassignment.data.model.response.EscapeRoomsMovy
import com.example.tunaassignment.databinding.FragmentEscapeRoomBinding
import com.example.tunaassignment.utils.Constants
import com.example.tunaassignment.utils.Result
import com.example.tunaassignment.utils.hide
import com.example.tunaassignment.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EscapeRoomFragment : Fragment(), RoomClick {

    private lateinit var binding: FragmentEscapeRoomBinding
    private val viewModel: EscapeRoomViewModel by viewModels()
    private var token: String = ""
    private var userId: String = ""
    private var memberId: String = ""
    private var deviceId: String = ""
    private var tickets: String = ""
    private var city: String = ""
    private var countryCode: String = ""
    private lateinit var escapeRoomAdapter: EscapeRoomAdapter
    private var escapeRoomList = mutableListOf<EscapeRoomsMovy>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEscapeRoomBinding.inflate(inflater, container, false)
        // If you want to set the toolbar as the action bar for the activity
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            token = it.getString(Constants.TOKEN, "")
            userId = it.getString(Constants.USER_ID, "")
            deviceId = it.getString(Constants.DEVICE_ID, "")
            memberId = it.getString(Constants.MEMBER_ID, "")
            city = it.getString(Constants.CITY, "")
            countryCode = it.getString(Constants.COUNTRY_CODE, "")

        }

        escapeRoomAdapter = EscapeRoomAdapter(escapeRoomList, this)
        binding.apply {
            recyclerView.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = escapeRoomAdapter
            }
            locationTitle.text = "$city,$countryCode"
        }


        viewModel.fetchEscapeRoom(
            token, userId, EscapeRoomParams(
                deviceId,
                device_type = Constants.DEVICE_ID_PARAM,
                location_id = Constants.LOCATION_ID,
                login_type = Constants.DEVICE_ID_PARAM,
                memberId
            )
        )
        escapeRoomList.clear()
        viewModel.getEscapeRoom.observe(viewLifecycleOwner)
        { it ->
            when (it.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    tickets = it?.data?.er_tickets!!
                    escapeRoomList.addAll(it.data.escape_rooms_movies)
                    escapeRoomAdapter.notifyDataSetChanged()
                }
                Result.Status.LOADING -> binding.progressBar.show()
                else -> binding.progressBar.hide()
            }
        }
    }

    override fun roomClick(position: Int) {
        val bundle = Bundle()
        bundle.apply {
            putString(Constants.TOKEN, token)
            putString(Constants.USER_ID, userId)
            putString(Constants.DEVICE_ID, deviceId)
            putString(Constants.MOVIE_ID, escapeRoomList[position].ID)
            putString(Constants.CITY, city)
            putString(Constants.COUNTRY_CODE, countryCode)
        }
        requireView().findNavController().navigate(R.id.bottomSheetDialogFragment, bundle)
    }
}
