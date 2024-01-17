package com.artem.android.testcalendar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artem.android.testcalendar.CalendarUtils.Companion.currentMonthFromDate
import com.artem.android.testcalendar.CalendarUtils.Companion.daysInMonthArray
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class CalendarFragment: Fragment(), OnItemListener {

    interface Callbacks { fun onNewTaskPressed() }

    private var callbacks: Callbacks? = null
    private lateinit var currentMonthTextView: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var dayRecyclerView: RecyclerView
    private lateinit var prevMonthBtn: Button
    private lateinit var nextMonthBtn: Button
    private lateinit var newTaskBtn: Button
    //private var hours = Hour.setHours()
    private var dayAdapter: DayAdapter? = DayAdapter(emptyList())
    //private var hourAdapter: HourAdapter? = HourAdapter(arrayListOf())

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
        //dayRecyclerView.adapter = taskAdapter
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
            callbacks?.onNewTaskPressed()
        }
        setView()
        return view
    }

    private fun setDayAdapter(hours: List<Hour>) {
        dayAdapter = DayAdapter(hours)
        dayRecyclerView.adapter = dayAdapter
    }

//    private fun setHourAdapter(hours: ArrayList<String>) {
//        hourAdapter = HourAdapter(hours)
//        dayRecyclerView.adapter = hourAdapter
//    }

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
        //val dayTasks = Task.tasksForDate(CalendarUtils.selectedDate)
        val hoursList: ArrayList<Hour> = arrayListOf()
//        val hour = Hour(CalendarUtils.selectedDate)
        for (i in 0 until 24) {
            val hour = Hour(LocalDateTime.of(CalendarUtils.selectedDate.toLocalDate(),
                LocalTime.of(CalendarUtils.selectedDate.hour.plus(i), CalendarUtils.selectedDate.minute)))
            for (task in Hour.Task.tasksList) {
                if (task.dateStart.toLocalDate() == hour.dateTime.toLocalDate() &&
                    task.dateStart.hour == hour.dateTime.hour) {
                    hour.task = task
                }
            }
            hoursList.add(hour)
        }
        //setTaskAdapter(Task.tasksForDate(CalendarUtils.selectedDate))
        setDayAdapter(hoursList)
    }

    override fun onItemClick(pos: Int, date: LocalDate) {
//        val message = "Selected date " + date.dayOfMonth + " " + currentMonthFromDate(CalendarUtils.selectedDate)
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        CalendarUtils.selectedDate = LocalDateTime.of(date, LocalTime.of(0,0))
        setView()
    }


    companion object {
        fun newInstance(): CalendarFragment {
            return CalendarFragment()
        }
    }
}