package com.artem.android.testcalendar.ui.timepicker

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.time.LocalTime

class TimePickerFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val time = arguments?.getSerializable(ARG_TIME) as LocalTime

        val timeListener = TimePickerDialog.OnTimeSetListener {
            _: TimePicker, hour: Int, minute: Int ->
            val resultTime: LocalTime = LocalTime.of(hour, minute)

            val result = Bundle().apply {
                putSerializable("bundleKey", resultTime)
            }
            parentFragmentManager.setFragmentResult("requestKey", result)
        }

        return TimePickerDialog(
            requireContext(),
            timeListener,
            time.hour,
            time.minute,
            true
        )
    }

    companion object {
        private const val ARG_TIME = "time"

        fun newInstance(time: LocalTime): TimePickerFragment {
            val args = Bundle().apply { putSerializable(ARG_TIME, time) }
            return TimePickerFragment().apply { arguments = args }
        }
    }
}