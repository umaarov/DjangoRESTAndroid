package uz.umarov.djangorestandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.umarov.djangorestandroid.databinding.TaskItemBinding
import uz.umarov.djangorestandroid.models.Task


class TasksAdapter(private var tasksList: MutableList<Task>) :
    RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    class ViewHolder(val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.num.text = "${task.id})"
            binding.title.text = task.title
            binding.desc.text = task.description
            binding.completedCheckBox.isChecked = task.completed
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TaskItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasksList[position])
    }

    override fun getItemCount(): Int = tasksList.size

    fun updateTasks(newTasks: MutableList<Task>) {
        tasksList = newTasks
        notifyDataSetChanged()  // Notify the adapter of the data change
    }

    fun addTask(task: Task) {
        tasksList.add(task)
        notifyItemInserted(tasksList.size - 1)
    }

}
