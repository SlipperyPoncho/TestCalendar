package com.artem.android.testcalendar.ui.calendarfragment.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.artem.android.testcalendar.R
import com.artem.android.testcalendar.ui.calendarfragment.OnItemListener
import java.time.LocalDate
import java.time.LocalDateTime

class CalendarAdapter(date: LocalDateTime, days: ArrayList<LocalDate?>, onItemListener: OnItemListener):
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private val days: ArrayList<LocalDate?>
    private val date: LocalDateTime
    private val onItemListener: OnItemListener

    init {
        this.days = days
        this.date = date
        this.onItemListener = onItemListener
    }

    inner class CalendarViewHolder(view: View, onItemListener: OnItemListener, days: ArrayList<LocalDate?>) :
        RecyclerView.ViewHolder(view), View.OnClickListener {

        private val dayOfMonth: TextView = itemView.findViewById(R.id.calendarDayText)
        private val onItemListener: OnItemListener
        private val days: ArrayList<LocalDate?>

        init {
            this.onItemListener = onItemListener
            itemView.setOnClickListener(this)
            this.days = days
        }

        fun bind(dayOfMonth: Int) {
            this.dayOfMonth.text = dayOfMonth.toString()
        }

        override fun onClick(v: View?) {
            days[adapterPosition]?.let { onItemListener.onItemClick(adapterPosition, it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.calendar_day, parent, false)
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.height = parent.height * 1/6 //fix maybe
        return CalendarViewHolder(view, onItemListener, days)
    }

    override fun getItemCount() = days.size

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val day = days[position]
        if (day == LocalDate.of(date.year, date.month, date.dayOfMonth)) {
            holder.itemView.setBackgroundColor(Color.argb(95,84,152,68))
        }
        if (day != null) {
            holder.bind(day.dayOfMonth)
        } else {
            holder.itemView.isClickable = false
        }
    }
}