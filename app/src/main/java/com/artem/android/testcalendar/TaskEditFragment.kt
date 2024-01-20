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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.artem.android.testcalendar.Hour.Task.Companion.tasksList
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private const val REQUEST_TIME = 0
private const val DIALOG_TIME = "DialogTime"
private const val ARG_TASK_ID = "task_id"

class TaskEditFragment: Fragment(), TimePickerFragment.Callbacks {

    private lateinit var task: Hour.Task
    private lateinit var taskNameEditText: EditText
    private lateinit var taskDateTextView: TextView
    private lateinit var taskStartTimeTextView: TextView
    private lateinit var taskFinishTimeTextView: TextView
    private lateinit var pickStartTimeBtn: Button
    private lateinit var pickFinishTimeBtn: Button
    private lateinit var taskDescriptionEditText: EditText
    private lateinit var saveBtn: Button
    private lateinit var time: LocalTime
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    private val taskEditViewModel: TaskEditViewModel by lazy {
        ViewModelProvider(this)[TaskEditViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        time = LocalTime.now()
        task = Hour.Task()
        val taskId: Int = arguments?.getSerializable(ARG_TASK_ID) as Int
        taskEditViewModel.loadTask(taskId)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskEditViewModel.taskLiveData.observe(
            viewLifecycleOwner,
            Observer {
                task -> task?.let {
                    this.task = task
                    updateUI()
                }
            }
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        taskDateTextView.text = "Date ${CalendarUtils.formattedDate(CalendarUtils.selectedDate)}"

        val taskNameWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                task.name = taskNameEditText.text.toString()
                saveBtn.isEnabled = taskDescriptionEditText.text.toString() != "" &&
                        taskNameEditText.text.toString() != ""
            }
            override fun afterTextChanged(s: Editable?) {}
        }
        taskNameEditText.addTextChangedListener(taskNameWatcher)

        val taskDescriptionWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                task.description = taskDescriptionEditText.text.toString()
                saveBtn.isEnabled = taskNameEditText.text.toString() != "" &&
                        taskDescriptionEditText.text.toString() != ""
            }
            override fun afterTextChanged(s: Editable?) {}
        }
        taskDescriptionEditText.addTextChangedListener(taskDescriptionWatcher)

        saveBtn.setOnClickListener {
            if (task.id < tasksList.lastIndex + 1) {
                tasksList.removeAt(task.id)
                tasksList.add(task)
            } else {
                task.id = tasksList.lastIndex + 1
                tasksList.add(task)
            }
            taskEditViewModel.saveTask(task)
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
    private fun updateUI() {
        taskNameEditText.setText(task.name)
        pickStartTimeBtn.text = "${CalendarUtils.stringTime(task.dateStart.hour)}:" +
                CalendarUtils.stringTime(task.dateStart.minute)
        pickFinishTimeBtn.text = "${CalendarUtils.stringTime(task.dateFinish.hour)}:" +
                CalendarUtils.stringTime(task.dateFinish.minute)
        taskDescriptionEditText.setText(task.description)
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSelected(time: LocalTime, flag: String) {
        if (flag == "start") {
            pickStartTimeBtn.text = CalendarUtils.stringTime(time.hour) + ":" +
                    CalendarUtils.stringTime(time.minute)
            task.dateStart = LocalDateTime.of(
                CalendarUtils.selectedDate.toLocalDate(),
                LocalTime.parse(pickStartTimeBtn.text, formatter)
            )
            pickFinishTimeBtn.text = "${CalendarUtils.stringTime(
                pickStartTimeBtn.text.substring(0,2).toInt() + 1)}:" +
                    pickStartTimeBtn.text.substring(3,5)
            task.dateFinish = LocalDateTime.of(
                CalendarUtils.selectedDate.toLocalDate(),
                LocalTime.parse(pickFinishTimeBtn.text, formatter)
            )
        } else if (flag == "finish") {
            pickFinishTimeBtn.text = "${CalendarUtils.stringTime(time.hour)}:" +
                    CalendarUtils.stringTime(time.minute)
            task.dateFinish = LocalDateTime.of(
                CalendarUtils.selectedDate.toLocalDate(),
                LocalTime.parse(pickFinishTimeBtn.text, formatter)
            )
        }
    }

    companion object {
        fun newInstance(taskId: Int): TaskEditFragment {
            val args = Bundle().apply { putSerializable(ARG_TASK_ID, taskId) }
            return TaskEditFragment().apply { arguments = args }
        }
    }
}