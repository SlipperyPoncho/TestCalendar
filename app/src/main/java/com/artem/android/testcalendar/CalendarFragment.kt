package com.artem.android.testcalendar

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artem.android.testcalendar.CalendarUtils.Companion.currentMonthFromDate
import com.artem.android.testcalendar.CalendarUtils.Companion.daysInMonthArray
import com.artem.android.testcalendar.Hour.Task.Companion.tasksList
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


private const val TAG = "TaskListFragment"

class CalendarFragment: Fragment(), OnItemListener {

    interface Callbacks { fun onNewTaskPressed(taskId: Int) }

    private var callbacks: Callbacks? = null
    private lateinit var currentMonthTextView: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var dayRecyclerView: RecyclerView
    private lateinit var prevMonthBtn: Button
    private lateinit var nextMonthBtn: Button
    private lateinit var newTaskBtn: Button
    private var dayAdapter: DayAdapter? = DayAdapter(emptyList())

    private val taskListViewModel: TaskListViewModel by lazy {
        ViewModelProvider(this)[TaskListViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CalendarUtils.selectedDate = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        currentMonthTextView = view.findViewById(R.id.current_month_textview)
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView)
        dayRecyclerView = view.findViewById(R.id.day_recycler_view)
        dayRecyclerView.layoutManager = LinearLayoutManager(context)
        prevMonthBtn = view.findViewById(R.id.prev_month_btn)
        prevMonthBtn.setOnClickListener{
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1)
            setView()
        }
        nextMonthBtn = view.findViewById(R.id.next_month_btn)
        nextMonthBtn.setOnClickListener{
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1)
            setView()
        }
        newTaskBtn = view.findViewById(R.id.new_task_btn)
        newTaskBtn.setOnClickListener {
            val task = Hour.Task()
            taskListViewModel.addTask(task)
            callbacks?.onNewTaskPressed(task.id)
        }
        setView()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskListViewModel.taskListLiveData.observe(
            viewLifecycleOwner,
            Observer {
                tasks -> tasks?.let {
                    Log.i(TAG, "Got tasks ${tasks.size}")
                    tasksList = tasks.toMutableList()
                    setDayAdapter(Hour.setHours())
                }
            }
        )
    }

    private fun setDayAdapter(hours: List<Hour>) {
        dayAdapter = DayAdapter(hours)
        dayRecyclerView.adapter = dayAdapter
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun setView() {
        currentMonthTextView.text = currentMonthFromDate(CalendarUtils.selectedDate)
        val daysInMonth: ArrayList<LocalDate?> = daysInMonthArray(CalendarUtils.selectedDate)
        val adapter = CalendarAdapter(daysInMonth, this)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 7)
        calendarRecyclerView.layoutManager = layoutManager
        calendarRecyclerView.adapter = adapter
        setDayAdapter(Hour.setHours())
    }

    override fun onItemClick(pos: Int, date: LocalDate) {
        CalendarUtils.selectedDate = LocalDateTime.of(date, LocalTime.of(0,0))
        setView()
    }


    companion object {
        fun newInstance(): CalendarFragment {
            return CalendarFragment()
        }
    }
}