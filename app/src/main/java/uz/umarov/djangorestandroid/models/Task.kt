package uz.umarov.djangorestandroid.models

data class Task(
    val id: Int? = null,
    val title: String,
    val description: String,
    val completed: Boolean
)
