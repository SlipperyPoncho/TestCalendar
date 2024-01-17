package com.artem.android.testcalendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

private const val REQUEST_TIME = 0
private const val DIALOG_TIME = "DialogTime"

class TaskEditFragment: Fragment(), TimePickerFragment.Callbacks {

    private lateinit var taskNameEditText: EditText
    private lateinit var taskDateTextView: TextView
    private lateinit var taskStartTimeTextView: TextView
    private lateinit var taskFinishTimeTextView: TextView
    private lateinit var pickStartTimeBtn: Button
    private lateinit var pickFinishTimeBtn: Button
    private lateinit var taskDescriptionEditText: EditText
    private lateinit var saveBtn: Button
    private lateinit var time: LocalTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        time = LocalTime.now()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_edit, container, false)
        taskNameEditText = view.findViewById(R.id.task_name_edit_text)
        taskDateTextView = view.findViewById(R.id.date_text_view)
        taskStartTimeTextView = view.findViewById(R.id.time_start_text_view)
        taskFinishTimeTextView = view.findViewById(R.id.time_finish_text_view)
        taskDescriptionEditText = view.findViewById(R.id.description_edit_text)
        pickStartTimeBtn = view.findViewById(R.id.pick_start_time_btn)
        pickFinishTimeBtn = view.findViewById(R.id.pick_finish_time_btn)
        saveBtn = view.findViewById(R.id.save_btn)
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        taskDateTextView.text = "Date ${CalendarUtils.formattedDate(CalendarUtils.selectedDate)}"

        val taskNameWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                saveBtn.isEnabled = taskDescriptionEditText.text.toString() != "" &&
                        taskNameEditText.text.toString() != ""
            }
            override fun afterTextChanged(s: Editable?) {}
        }
        taskNameEditText.addTextChangedListener(taskNameWatcher)

        val taskDescriptionWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                saveBtn.isEnabled = taskNameEditText.text.toString() != "" &&
                        taskDescriptionEditText.text.toString() != ""
            }
            override fun afterTextChanged(s: Editable?) {}
        }
        taskDescriptionEditText.addTextChangedListener(taskDescriptionWatcher)

        saveBtn.setOnClickListener {
            val taskName: String = taskNameEditText.text.toString()
            val formatter = DateTimeFormatter.ofPattern("HH:mm")

            val newTask = Hour.Task(
                Hour.Task.tasksList.lastIndex + 1,
                taskName,
                LocalDateTime.of(
                    CalendarUtils.selectedDate.toLocalDate(),
                    LocalTime.parse(pickStartTimeBtn.text, formatter)
                ),
                LocalDateTime.of(
                    CalendarUtils.selectedDate.toLocalDate(),
                    LocalTime.parse(pickFinishTimeBtn.text, formatter)
                ),
                taskDescriptionEditText.text.toString()
            )

            Hour.Task.tasksList.add(newTask)
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        pickStartTimeBtn.setOnClickListener {
            TimePickerFragment.newInstance(LocalTime.parse(pickStartTimeBtn.text,
                DateTimeFormatter.ofPattern("HH:mm")), "start").apply {
                setTargetFragment(this@TaskEditFragment, REQUEST_TIME)
                show(this@TaskEditFragment.requireFragmentManager(), DIALOG_TIME)
            }
        }

        pickFinishTimeBtn.setOnClickListener {
            TimePickerFragment.newInstance(LocalTime.parse(pickFinishTimeBtn.text,
                DateTimeFormatter.ofPattern("HH:mm")), "finish").apply {
                setTargetFragment(this@TaskEditFragment, REQUEST_TIME)
                show(this@TaskEditFragment.requireFragmentManager(), DIALOG_TIME)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSelected(time: LocalTime, flag: String) {
        if (flag == "start") {
            pickStartTimeBtn.text = CalendarUtils.stringTime(time.hour) + ":" +
                    CalendarUtils.stringTime(time.minute)
            pickFinishTimeBtn.text = "${CalendarUtils.stringTime(
                pickStartTimeBtn.text.substring(0,2).toInt() + 1)}:" +
                    pickStartTimeBtn.text.substring(3,5)
        } else if (flag == "finish") {
            pickFinishTimeBtn.text = "${CalendarUtils.stringTime(time.hour)}:" +
                    CalendarUtils.stringTime(time.minute)
        }
    }

    companion object {
        fun newInstance(): TaskEditFragment {
            return TaskEditFragment()
        }
    }
}