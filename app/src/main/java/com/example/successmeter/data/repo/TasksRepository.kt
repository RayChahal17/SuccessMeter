package com.example.successmeter.data.repo

import com.example.successmeter.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TasksRepository {

    fun observeTasksForGoal(goalId: Long): Flow<List<Task>>
    suspend fun upsertTask(task: Task)
    suspend fun archiveTask(taskId: Long)
}
