package com.artem.android.testcalendar

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.artem.android.testcalendar.Hour.Companion.stringTime
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

class TaskEditFragment: Fragment() {

    private lateinit var taskNameEditText: EditText
    private lateinit var taskDateTextView: TextView
    private lateinit var taskStartTimeTextView: TextView
    private lateinit var taskFinishTimeTextView: TextView
    private lateinit var pickStartTimeBtn: Button
    private lateinit var pickFinishTimeBtn: Button
    private lateinit var taskDescriptionEditText: EditText
    private lateinit var saveBtn: Button
    private lateinit var selectedDate: LocalDateTime

    private val taskEditViewModel: TaskEditViewModel by lazy {
        ViewModelProvider(this)[TaskEditViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        taskEditViewModel.task = Task()
        val taskId: UUID = arguments?.getSerializable(ARG_TASK_ID) as UUID
        selectedDate = arguments?.getSerializable(ARG_SELECTED_DATE) as LocalDateTime
        taskEditViewModel.loadTask(taskId)
        activity?.onBackPressedDispatcher?.addCallback(onBackInvokedCallback)
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
                    taskEditViewModel.task = task
                    updateUI()
                }
            }
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        taskDateTextView.text = "Date ${taskEditViewModel.formattedDate(selectedDate)}"

        taskNameEditText.addTextChangedListener(taskEditViewModel.taskNameWatcher)
        taskDescriptionEditText.addTextChangedListener(taskEditViewModel.taskDescriptionWatcher)

        saveBtn.setOnClickListener {
            taskEditViewModel.task.dateStart = LocalDateTime.of(
                selectedDate.toLocalDate(),
                LocalTime.parse(pickStartTimeBtn.text, taskEditViewModel.formatter)
            )
            taskEditViewModel.task.dateFinish = LocalDateTime.of(
                selectedDate.toLocalDate(),
                LocalTime.parse(pickFinishTimeBtn.text, taskEditViewModel.formatter)
            )
            taskEditViewModel.updateTask(taskEditViewModel.task)
            parentFragmentManager.popBackStack()
        }

        pickStartTimeBtn.setOnClickListener {
            childFragmentManager.setFragmentResultListener("requestKey", viewLifecycleOwner) {
                _, bundle ->
                    val result = bundle.getSerializable("bundleKey") as LocalTime
                    taskEditViewModel.task.dateStart = LocalDateTime.of(
                        selectedDate.toLocalDate(),
                        result
                    )
                    updateUI()
            }
            val showTime = TimePickerFragment.newInstance(taskEditViewModel.task.dateStart.toLocalTime())
            showTime.show(this@TaskEditFragment.childFragmentManager, DIALOG_TIME)
        }

        pickFinishTimeBtn.setOnClickListener {
            childFragmentManager.setFragmentResultListener("requestKey", viewLifecycleOwner) {
                    _, bundle ->
                        val result = bundle.getSerializable("bundleKey") as LocalTime
                        taskEditViewModel.task.dateFinish = LocalDateTime.of(
                            selectedDate.toLocalDate(),
                            result
                        )
                        updateUI()
            }
            val showTime = TimePickerFragment.newInstance(taskEditViewModel.task.dateFinish.toLocalTime())
            showTime.show(this@TaskEditFragment.childFragmentManager, DIALOG_TIME)
        }
    }

    private val onBackInvokedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (taskEditViewModel.task.name == "" || taskEditViewModel.task.description == "") {
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setMessage("If you return task info would be lost")
                    .setTitle("Warning!")
                    .setPositiveButton("Ok") { _, _ ->
                        run {
                            taskEditViewModel.deleteTask(taskEditViewModel.task)
                            parentFragmentManager.popBackStack()
                        }
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.cancel()
                    }
                    .show()
            } else {
                parentFragmentManager.popBackStack()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_task_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_task -> {
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to delete this task?")
                    .setTitle("Warning!")
                    .setPositiveButton("Yes") { _, _ ->
                        run {
                            taskEditViewModel.deleteTask(taskEditViewModel.task)
                            parentFragmentManager.popBackStack()
                        }
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.cancel()
                    }
                    .show()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        taskNameEditText.setText(taskEditViewModel.task.name)
        pickStartTimeBtn.text = "${stringTime(taskEditViewModel.task.dateStart.hour)}:" +
                stringTime(taskEditViewModel.task.dateStart.minute)
        pickFinishTimeBtn.text = "${stringTime(taskEditViewModel.task.dateFinish.hour)}:" +
                stringTime(taskEditViewModel.task.dateFinish.minute)
        taskDescriptionEditText.setText(taskEditViewModel.task.description)
    }

    companion object {
        private const val DIALOG_TIME = "DialogTime"
        private const val ARG_TASK_ID = "task_id"
        private const val ARG_SELECTED_DATE = "selected_date"

        fun newInstance(taskId: UUID, selectedDate: LocalDateTime): TaskEditFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TASK_ID, taskId)
                putSerializable(ARG_SELECTED_DATE, selectedDate)
            }
            return TaskEditFragment().apply { arguments = args }
        }
    }
}