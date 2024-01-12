package com.artem.android.testcalendar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artem.android.testcalendar.CalendarUtils.Companion.currentMonthFromDate
import com.artem.android.testcalendar.CalendarUtils.Companion.daysInMonthArray
import java.time.LocalDate

class CalendarFragment: Fragment(), OnItemListener {

    interface Callbacks { fun onNewTaskPressed() }

    private var callbacks: Callbacks? = null
    private lateinit var currentMonthTextView: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var prevMonthBtn: Button
    private lateinit var nextMonthBtn: Button
    private lateinit var newTaskBtn: Button
    private var taskAdapter: TaskAdapter? = TaskAdapter(emptyList())

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CalendarUtils.selectedDate = LocalDate.now()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        currentMonthTextView = view.findViewById(R.id.current_month_textview)
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView)
        taskRecyclerView = view.findViewById(R.id.tasks_recycler_view)
        taskRecyclerView.layoutManager = LinearLayoutManager(context)
        taskRecyclerView.adapter = taskAdapter
        prevMonthBtn = view.findViewById(R.id.prev_month_btn)
        prevMonthBtn.setOnClickListener{
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1)
            setMonthView()
        }
        nextMonthBtn = view.findViewById(R.id.next_month_btn)
        nextMonthBtn.setOnClickListener{
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1)
            setMonthView()
        }
        newTaskBtn = view.findViewById(R.id.new_task_btn)
        newTaskBtn.setOnClickListener {
            callbacks?.onNewTaskPressed()
        }
        setMonthView()
        updateUI(Task.tasksForDate(CalendarUtils.selectedDate))
        return view
    }

    private fun updateUI(tasks: List<Task>) {
        taskAdapter = TaskAdapter(tasks)
        taskRecyclerView.adapter = taskAdapter
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun setMonthView() {
        currentMonthTextView.text = currentMonthFromDate(CalendarUtils.selectedDate)
        val daysInMonth: ArrayList<LocalDate?> = daysInMonthArray(CalendarUtils.selectedDate)
        val adapter = CalendarAdapter(daysInMonth, this)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 7)
        calendarRecyclerView.layoutManager = layoutManager
        calendarRecyclerView.adapter = adapter
        updateUI(Task.tasksForDate(CalendarUtils.selectedDate))
    }

    override fun onItemClick(pos: Int, date: LocalDate) {
//        val message = "Selected date " + date.dayOfMonth + " " + currentMonthFromDate(CalendarUtils.selectedDate)
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        CalendarUtils.selectedDate = date
        setMonthView()
    }


    companion object {
        fun newInstance(): CalendarFragment {
            return CalendarFragment()
        }
    }
}