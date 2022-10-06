package com.example.kmitlcompanion.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.kmitlcompanion.R
import com.example.kmitlcompanion.databinding.FragmentIdentityloginBinding
import com.example.kmitlcompanion.presentation.IdentityloginViewModel

class IdentityloginFragment : Fragment() {

    private lateinit var viewModel: IdentityloginViewModel
    private lateinit var binding: FragmentIdentityloginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)?.getSupportActionBar()?.hide()
        binding = FragmentIdentityloginBinding.inflate(inflater,container, false)

        return binding.root
    }


    companion object {
        fun newInstance() = IdentityloginFragment()
    }



//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(IdentityloginViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}