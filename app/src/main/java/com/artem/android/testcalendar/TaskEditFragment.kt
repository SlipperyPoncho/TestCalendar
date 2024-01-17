package com.artem.android.testcalendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TaskEditFragment: Fragment() {

    private lateinit var taskNameEditText: EditText
    private lateinit var taskDateTextView: TextView
    private lateinit var taskStartTimeTextView: TextView
    private lateinit var taskFinishTimeTextView: TextView
    private lateinit var taskStartTimeEditText: EditText
    private lateinit var taskFinishTimeEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var saveBtn: Button
    private lateinit var time: LocalTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        time = LocalTime.now()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_edit, container, false)
        taskNameEditText = view.findViewById(R.id.task_name_edit_text)
        taskDateTextView = view.findViewById(R.id.date_text_view)
        taskDateTextView.text = "Date ${CalendarUtils.formattedDate(CalendarUtils.selectedDate)}"
        taskStartTimeTextView = view.findViewById(R.id.time_start_text_view)
        taskFinishTimeTextView = view.findViewById(R.id.time_finish_text_view)
        taskStartTimeEditText = view.findViewById(R.id.start_time_edit_text)
        taskFinishTimeEditText = view.findViewById(R.id.finish_time_edit_text)
        descriptionEditText = view.findViewById(R.id.description_edit_text)
        saveBtn = view.findViewById(R.id.save_btn)
        saveBtn.setOnClickListener {
            val taskName: String = taskNameEditText.text.toString()
            val formatter = DateTimeFormatter.ofPattern("HH:mm")

            val newTask = Hour.Task(
                Hour.Task.tasksList.lastIndex + 1,
                taskName,
                LocalDateTime.of(
                    CalendarUtils.selectedDate.toLocalDate(),
                    LocalTime.parse(taskStartTimeEditText.text, formatter)
                ),
                LocalDateTime.of(
                    CalendarUtils.selectedDate.toLocalDate(),
                    LocalTime.parse(taskFinishTimeEditText.text, formatter)
                ),
                descriptionEditText.text.toString()
            )

            Hour.Task.tasksList.add(newTask)
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        return view
    }

    companion object {
        fun newInstance(): TaskEditFragment {
            return TaskEditFragment()
        }
    }
}