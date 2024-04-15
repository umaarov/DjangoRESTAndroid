package uz.umarov.djangorestandroid

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.umarov.djangorestandroid.adapters.TasksAdapter
import uz.umarov.djangorestandroid.databinding.ActivityMainBinding
import uz.umarov.djangorestandroid.models.Task
import uz.umarov.djangorestandroid.networking.ApiService
import uz.umarov.djangorestandroid.networking.RetrofitClient

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TasksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)

        adapter = TasksAdapter(mutableListOf())

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        loadTasksFromApi()

        binding.submit.setOnClickListener {
            val newTask = Task(
                title = binding.title.text.toString(),
                description = binding.desc.text.toString(),
                completed = binding.checkbox.isChecked
            )
            createTask(newTask)
        }
    }

    private fun loadTasksFromApi() {
        showProgressBar(true)
        val retrofit = RetrofitClient.getClient("http://10.30.1.98:8000/")
        val apiService = retrofit.create(ApiService::class.java)

        apiService.getTasks().enqueue(object : Callback<MutableList<Task>> {
            override fun onResponse(
                call: Call<MutableList<Task>>,
                response: Response<MutableList<Task>>
            ) {
                if (response.isSuccessful) {
                    adapter.updateTasks(response.body()!!)
                } else {
                    Log.e("MainActivity", "Error in fetching tasks: ${response.errorBody()}")
                }
                showProgressBar(false)
            }

            override fun onFailure(call: Call<MutableList<Task>>, t: Throwable) {
                Log.e("MainActivity", "Failed to fetch tasks: ${t.message}")
                showProgressBar(false)
            }
        })
    }

    private fun createTask(task: Task) {
        val retrofit = RetrofitClient.getClient("http://10.30.1.98:8000/")
        val apiService = retrofit.create(ApiService::class.java)

        apiService.createTask(task).enqueue(object : Callback<Task> {
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if (response.isSuccessful) {
                    val createdTask = response.body()!!
                    Log.d("MainActivity", "Task created: ${createdTask.title}")
                    // Update adapter with the newly created task
                    adapter.addTask(createdTask)
                } else {
                    Log.e(
                        "MainActivity", "Failed to create task: ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<Task>, t: Throwable) {
                Log.e("MainActivity", "Error creating task: ${t.message}")
            }
        })
    }

    private fun showProgressBar(show: Boolean) {
        binding.progress.visibility = if (show) View.VISIBLE else View.GONE
    }

}
