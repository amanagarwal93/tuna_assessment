package com.example.tunaassignment.ui.escape_session

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.tunaassignment.R
import com.example.tunaassignment.data.listeners.RoomClick
import com.example.tunaassignment.data.model.EscapeSession
import com.example.tunaassignment.data.model.Month
import com.example.tunaassignment.data.model.request.MovieParams
import com.example.tunaassignment.databinding.FragmentSessionBinding
import com.example.tunaassignment.ui.escapeRoom.EscapeRoomViewModel
import com.example.tunaassignment.utils.Constants
import com.example.tunaassignment.utils.Result
import com.example.tunaassignment.utils.hide
import com.example.tunaassignment.utils.show
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class SessionFragment : Fragment(), RoomClick {

    private var token: String = ""
    private var userId: String = ""
    private var deviceId: String = ""
    private var movieId: String = ""
    private var city: String = ""
    private var countryCode: String = ""
    private var dateList = ArrayList<Month>()
    private var dateList2 = ArrayList<Month>()
    private var session: ArrayList<EscapeSession>? = null
    private lateinit var binding: FragmentSessionBinding
    private val viewModel: EscapeRoomViewModel by viewModels()
    private lateinit var dateAdapter: DateAdapter
    private lateinit var timeAdapter: TimeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSessionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            token = it.getString(Constants.TOKEN, "")
            userId = it.getString(Constants.USER_ID, "")
            deviceId = it.getString(Constants.DEVICE_ID, "")
            movieId = it.getString(Constants.MOVIE_ID, "")
            city = it.getString(Constants.CITY, "")
            countryCode = it.getString(Constants.COUNTRY_CODE, "")

        }

        dateAdapter = DateAdapter(dateList, this)
        timeAdapter = TimeAdapter(emptyList())
        binding.apply {
            dateRecyclerView.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
                adapter = dateAdapter
            }
            timeRecyclerView.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
                adapter = timeAdapter
            }
            locationTitle.text = "$city, $countryCode"
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

        viewModel.getMovieResponse.observe(viewLifecycleOwner) { it ->
            when (it.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    val response = it.data
                    binding.ratingTitle.text = response?.movie_info?.Rating
                    binding.time.text =
                        "${response?.movie_info?.RunTime} ${getString(R.string.min)}"
                    binding.roomPoster.load(response?.movie_info?.image_url)
                    binding.roomTitle.text = response?.movie_info?.Title

                    // getting session list
                    dateList2.clear()
                    session = ArrayList()
                    response?.movie_info?.show_times?.listIterator()?.forEach {
                        addDate(dateList2, it.date)

                        session?.add(EscapeSession(it.sessions))
                    }
                    dateList.clear()
                    // getting date List and set to adapter
                    response?.movie_info?.date_list?.forEach { i -> addDate(dateList, i) }
                    dateAdapter.notifyDataSetChanged()
                }
                Result.Status.LOADING -> binding.progressBar.show()
                else -> binding.progressBar.hide()
            }
        }
    }

    private fun addDate(list: ArrayList<Month>, date: String) {
        val parsedDate = parseDateString(date)
        when {
            parsedDate != null -> {
                val (day, month, year) = parsedDate
                list.add(Month(day = day, month = month, date = year))
            }
        }
    }

    private fun parseDateString(dateString: String): Triple<Int, String, String>? {
        try {
            val formatter =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) DateTimeFormatter.ofPattern(
                    Constants.DATE_FORMAT
                ) else TODO("VERSION.SDK_INT < O")
            val date = LocalDate.parse(dateString, formatter)

            val day = date.dayOfMonth
            val month = date.month.name.take(3)
            val year = date.dayOfWeek.name.take(3)

            return Triple(day, month, year)
        } catch (e: Exception) {
            // Handle parsing exception, if needed
            e.printStackTrace()
            return null
        }
    }

    override fun roomClick(position: Int) {
        val list = ArrayList<String>()
        list.clear()
        dateAdapter.setSelectedItem(position)
        when {
            dateList[position] == dateList2[position] -> {
                session!![position].sessionList.forEach { i -> list.add(i.Showtime) }
                timeAdapter.updateData(list)
            }
        }
    }
}
