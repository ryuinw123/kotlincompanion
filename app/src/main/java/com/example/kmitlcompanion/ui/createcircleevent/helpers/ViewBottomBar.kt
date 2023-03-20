package com.example.kmitlcompanion.ui.createcircleevent.helpers

import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.constraintlayout.widget.Guideline
import com.example.kmitlcompanion.presentation.viewmodel.CreateCircleEventViewModel
import java.lang.ref.WeakReference
import javax.inject.Inject

class ViewBottomBar @Inject constructor(

) {

    private val RADIUS_SEEKBAR_DIFFERENCE = 1
    private val RADIUS_SEEKBAR_MAX = 100//150
    private val RADIUS_INITIAL = 5//1

    lateinit var viewModel: CreateCircleEventViewModel
    lateinit var weakGuideline: WeakReference<Guideline>
    lateinit var weakSeekBar: WeakReference<SeekBar>
    fun setup(viewModel: CreateCircleEventViewModel, guideline: Guideline , seekBar: SeekBar) {
        this.viewModel = viewModel
        this.weakGuideline = WeakReference(guideline)
        this.weakSeekBar = WeakReference(seekBar)

        seekBar.max = RADIUS_SEEKBAR_MAX
        seekBar.incrementProgressBy(RADIUS_SEEKBAR_DIFFERENCE)
        seekBar.progress = RADIUS_INITIAL

        viewModel.updateRadius(RADIUS_INITIAL / 100.0) //10.0


        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress == 0) {
                    seekBar.progress = 1
                }
                val radius = seekBar.progress / 100.0 //10.0
                viewModel.updateRadius(radius)
            }

            override fun onStartTrackingTouch(seekbar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekbar: SeekBar?) {
            }

        })

    }







}