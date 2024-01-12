package com.artem.android.testcalendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(var tasks: List<Task>): RecyclerView.Adapter<TaskAdapter.TaskHolder>() {

    inner class TaskHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var task: Task
        private val taskNameTextView: TextView = itemView.findViewById(R.id.task_name_text_view)
        private val taskDateTextView: TextView = itemView.findViewById(R.id.task_date_text_view)

        @SuppressLint("SetTextI18n")
        fun bind(task: Task) {
            this.task = task
            taskNameTextView.text = this.task.name
            taskDateTextView.text = "${this.task.startTime} - ${this.task.finishTime}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_task, parent, false)
        return TaskHolder(view)
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }
}