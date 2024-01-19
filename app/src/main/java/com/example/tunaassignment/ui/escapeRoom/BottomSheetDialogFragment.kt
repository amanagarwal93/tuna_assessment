package com.example.tunaassignment.ui.escapeRoom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import coil.load
import com.example.tunaassignment.R
import com.example.tunaassignment.data.model.request.MovieParams
import com.example.tunaassignment.databinding.BottomSheetBinding
import com.example.tunaassignment.ui.escape_session.SessionFragment
import com.example.tunaassignment.utils.Constants
import com.example.tunaassignment.utils.Result
import com.example.tunaassignment.utils.hide
import com.example.tunaassignment.utils.show
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetBinding
    private val viewModel: EscapeRoomViewModel by viewModels()
    private var token: String = ""
    private var userId: String = ""
    private var movieId: String = ""
    private var deviceId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = BottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            token = it.getString(Constants.TOKEN, "")
            userId = it.getString(Constants.USER_ID, "")
            deviceId = it.getString(Constants.DEVICE_ID, "")
            movieId = it.getString(Constants.MOVIE_ID, "")
        }
        viewModel.fetchMovieInfo(
            token,
            userId,
            MovieParams(
                deviceId,
                device_type = Constants.DEVICE_ID_PARAM,
                location_id = Constants.LOCATION_ID,
                movieId
            )
        )
        viewModel.getMovieResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Result.Status.SUCCESS -> {
                    binding.apply {
                        progressBar.hide()
                        val response = it.data
                        title.text = response?.movie_info?.Title
                        timeText.text =
                            response?.movie_info?.RunTime.toString() + " " + getString(R.string.minutes)
                        userDetails.text = response?.er_tickets
                        heading.text = response?.movie_info?.Synopsis
                        imageView.load(response?.movie_info?.image_url)
                    }
                }
                Result.Status.LOADING -> binding.progressBar.show()
                else -> binding.progressBar.hide()
            }
        }
        binding.closeIcon.setOnClickListener { dismiss() }

        binding.bookBtn.setOnClickListener {
            dismiss()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            val firstFragment = SessionFragment()
            firstFragment.arguments = arguments
            transaction.replace(R.id.nav_container, firstFragment).commit()
        }
    }
}
