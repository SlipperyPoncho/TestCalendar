package com.artem.android.testcalendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DayAdapter(var hours: List<Hour>): RecyclerView.Adapter<DayAdapter.DayHolder>() {

    inner class DayHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var hour: Hour
        private val timeTextView: TextView = itemView.findViewById(R.id.time_task_text_view)
        private val taskLinearLayout: LinearLayout = itemView.findViewById(R.id.task_layout)
        private val taskNameTextView: TextView = itemView.findViewById(R.id.task_name_text_view)
        private val taskDateTextView: TextView = itemView.findViewById(R.id.task_date_text_view)

        @SuppressLint("SetTextI18n")
        fun bind(hour: Hour) {
            this.hour = hour
            timeTextView.text = "${this.hour.dateTime.hour}:00"
            if (this.hour.task != null) {
                taskLinearLayout.visibility = View.VISIBLE
                taskNameTextView.text = this.hour.task!!.name
                val minuteStart = normalizeMinutes(this.hour.task!!.dateStart.minute)
                val minuteFinish = normalizeMinutes(this.hour.task!!.dateFinish.minute)
                taskDateTextView.text = "${this.hour.task!!.dateStart.hour}:$minuteStart - " +
                        "${this.hour.task!!.dateFinish.hour}:$minuteFinish"
            } else {
                taskLinearLayout.visibility = View.INVISIBLE
            }
        }

        private fun normalizeMinutes(minute: Int): String {
            var minuteStr = minute.toString()
            if (minuteStr.toInt() < 10) {
                minuteStr = "0$minuteStr"
            }
            return minuteStr
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