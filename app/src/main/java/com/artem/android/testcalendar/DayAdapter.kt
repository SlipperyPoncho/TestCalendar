package com.artem.android.testcalendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DayAdapter(var hours: List<Hour>): RecyclerView.Adapter<DayAdapter.DayHolder>() {

    private var callbacks: CalendarFragment.Callbacks? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        callbacks = recyclerView.context as CalendarFragment.Callbacks?
    }

    inner class DayHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var hour: Hour

        private val timeTextView: TextView = itemView.findViewById(R.id.time_task_text_view)
        private val taskLinearLayout: LinearLayout = itemView.findViewById(R.id.task_layout)
        private val taskNameTextView: TextView = itemView.findViewById(R.id.task_name_text_view)
        private val taskDateTextView: TextView = itemView.findViewById(R.id.task_date_text_view)

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("SetTextI18n")
        fun bind(hour: Hour) {
            this.hour = hour
            timeTextView.text = "${this.hour.dateTime.hour}:00"
            if (this.hour.task != null) {
                taskLinearLayout.visibility = View.VISIBLE
                itemView.isEnabled = true
                taskNameTextView.text = this.hour.task!!.name
                val minuteStart = CalendarUtils.stringTime(this.hour.task!!.dateStart.minute)
                val minuteFinish = CalendarUtils.stringTime(this.hour.task!!.dateFinish.minute)
                taskDateTextView.text = "${this.hour.task!!.dateStart.hour}:$minuteStart - " +
                        "${this.hour.task!!.dateFinish.hour}:$minuteFinish"
            } else {
                taskLinearLayout.visibility = View.INVISIBLE
                itemView.isEnabled = false
            }
        }

        override fun onClick(v: View?) {
            this.hour.task?.let { callbacks?.onNewTaskPressed(it.id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_task, parent, false)
        return DayHolder(view)
    }

    override fun getItemCount() = hours.size

    override fun onBindViewHolder(holder: DayHolder, position: Int) {
        val hour = hours[position]
        holder.bind(hour)
    }
}