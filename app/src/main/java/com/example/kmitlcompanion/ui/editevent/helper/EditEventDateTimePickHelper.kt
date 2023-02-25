package com.example.kmitlcompanion.ui.editevent.helper

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.kmitlcompanion.presentation.viewmodel.CreateEventViewModel
import com.example.kmitlcompanion.presentation.viewmodel.EditEventViewModel
import com.google.android.material.textfield.TextInputEditText
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class EditEventDateTimePickHelper @Inject constructor() {

    private lateinit var viewModel: EditEventViewModel
    private lateinit var context: Context
    private lateinit var calStart: Calendar
    private lateinit var calEnd: Calendar
    private lateinit var startDate : DatePickerDialog.OnDateSetListener
    private lateinit var startTime: TimePickerDialog.OnTimeSetListener
    private lateinit var endDate : DatePickerDialog.OnDateSetListener
    private lateinit var endTime: TimePickerDialog.OnTimeSetListener
    private lateinit var weakStartTimeTextInput : WeakReference<TextInputEditText>
    private lateinit var weakEndTimeTextInput : WeakReference<TextInputEditText>

    fun setup(context: Context,viewModel: EditEventViewModel,
              startTimeTextInput : TextInputEditText,
              endTimeTextInput : TextInputEditText,
              ){
        this.viewModel = viewModel
        this.context = context
        this.calStart = Calendar.getInstance()
        this.calEnd = Calendar.getInstance()
        this.weakStartTimeTextInput = WeakReference(startTimeTextInput)
        this.weakEndTimeTextInput = WeakReference(endTimeTextInput)

        //set Default time
        startTimeTextInput?.setText(SimpleDateFormat("dd/MM/yyyy | HH:mm", Locale.getDefault()).format(calStart.time))
        //this.viewModel.getStartDateTimePick(calStart)

        calEnd.add(Calendar.MINUTE,10)
        endTimeTextInput?.setText(SimpleDateFormat("dd/MM/yyyy | HH:mm", Locale.getDefault()).format(calEnd.time))
        //this.viewModel.getEndDateTimePick(calEnd)
    }

    fun openDateTimePicker() {
        this.startDate = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calStart.set(Calendar.YEAR, year)
            calStart.set(Calendar.MONTH, monthOfYear)
            calStart.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            this.startTime = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                calStart.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calStart.set(Calendar.MINUTE, minute)

                //get start datetime from user
                //this.viewModel.getStartDateTimePick(calStart)
                startTimeTextInput?.setText(SimpleDateFormat("dd/MM/yyyy | HH:mm", Locale.getDefault()).format(calStart.time))

            }
            TimePickerDialog(context, startTime, calStart.get(Calendar.HOUR_OF_DAY), calStart.get(Calendar.MINUTE), true).show()
        }
        DatePickerDialog(context, startDate, calStart.get(Calendar.YEAR), calStart.get(Calendar.MONTH), calStart.get(Calendar.DAY_OF_MONTH)).show()
    }

    fun openEndDateTimePicker() {
        this.endDate = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calEnd.set(Calendar.YEAR, year)
            calEnd.set(Calendar.MONTH, monthOfYear)
            calEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            this.endTime = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                calEnd.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calEnd.set(Calendar.MINUTE, minute)

                //get start datetime from user
                //this.viewModel.getEndDateTimePick(calEnd)
                endTimeTextInput?.setText(SimpleDateFormat("dd/MM/yyyy | HH:mm", Locale.getDefault()).format(calEnd.time))

            }
            TimePickerDialog(context, endTime, calEnd.get(Calendar.HOUR_OF_DAY), calEnd.get(Calendar.MINUTE), true).show()
        }
        DatePickerDialog(context, endDate, calEnd.get(Calendar.YEAR), calEnd.get(Calendar.MONTH), calEnd.get(Calendar.DAY_OF_MONTH)).show()
    }

    private val startTimeTextInput
        get() = this.weakStartTimeTextInput?.get()

    private val endTimeTextInput
        get() = this.weakEndTimeTextInput?.get()
}