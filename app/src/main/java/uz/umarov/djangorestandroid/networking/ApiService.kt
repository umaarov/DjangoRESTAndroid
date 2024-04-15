package uz.umarov.djangorestandroid.networking

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import uz.umarov.djangorestandroid.models.Task

interface ApiService {
    @GET("api/tasks/")
    fun getTasks(): Call<MutableList<Task>>

    @POST("api/tasks/")
    fun createTask(@Body task: Task): Call<Task>
}
