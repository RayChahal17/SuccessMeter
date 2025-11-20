package com.example.successmeter.data.repo

import com.example.successmeter.data.local.db.dao.TaskDao
import com.example.successmeter.data.mapper.toDomain
import com.example.successmeter.data.mapper.toEntity
import com.example.successmeter.domain.model.Task
import com.example.successmeter.domain.repo.TasksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TasksRepositoryRoom @Inject constructor(private val taskDao: TaskDao, ) : TasksRepository{

    override fun observeTasksForGoal(goalId: Long): Flow<List<Task>> {
        return taskDao.observeTasksForGoal(goalId)
            .map{
                entityList -> entityList.map { entity -> entity.toDomain() }
            }
    }

    override suspend fun upsertTask(task: Task): Long {
        return taskDao.upsertTask(task.toEntity())
    }

    override suspend fun archiveTask(taskId: Long) {
        return taskDao.archiveTask(taskId)
    }


}