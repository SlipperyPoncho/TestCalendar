package com.artem.android.testcalendar

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.time.LocalTime

class TimePickerFragment: DialogFragment() {
    interface Callbacks {
        fun onTimeSelected(time: LocalTime, flag: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val flag = arguments?.getSerializable(ARG_FLAG) as String
        val time = arguments?.getSerializable(ARG_TIME) as LocalTime

        val timeListener = TimePickerDialog.OnTimeSetListener {
            _: TimePicker, hour: Int, minute: Int ->
            val resultTime: LocalTime = LocalTime.of(hour, minute)

            targetFragment.let {
                fragment -> (fragment as Callbacks).onTimeSelected(resultTime, flag)
            }
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
        private const val ARG_FLAG = "flag"

        fun newInstance(time: LocalTime, flag: String): TimePickerFragment {
            val args = Bundle().apply { putSerializable(ARG_TIME, time)
            putSerializable(ARG_FLAG, flag)}
            return TimePickerFragment().apply { arguments = args }
        }
    }
}