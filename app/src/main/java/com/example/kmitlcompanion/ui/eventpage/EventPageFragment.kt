package com.example.kmitlcompanion.ui.eventpage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.presentation.EventPageViewModel

class EventPageFragment : Fragment() {

    companion object {
        fun newInstance() = EventPageFragment()
    }

    private lateinit var viewModel: EventPageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event_page, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EventPageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}