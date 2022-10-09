package com.example.kmitlcompanion.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentIdentityloginBinding
import com.example.kmitlcompanion.presentation.IdentityloginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IdentityloginFragment : Fragment() {

    private val viewModel: IdentityloginViewModel by viewModels()
    private lateinit var binding: FragmentIdentityloginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity?)?.getSupportActionBar()?.hide()
        binding = FragmentIdentityloginBinding.inflate(inflater,container, false).apply{
            viewModel = this@IdentityloginFragment.viewModel
        }

        return binding.root
    }



//
//    companion object {
//        fun newInstance() = IdentityloginFragment()
//    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(IdentityloginViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}