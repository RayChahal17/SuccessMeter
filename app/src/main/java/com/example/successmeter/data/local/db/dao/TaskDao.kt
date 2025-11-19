package com.example.successmeter.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.successmeter.data.local.db.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    // 1) Observe tasks for a given goal (leaf goal)
    @Query(
        """
    SELECT * FROM tasks
    WHERE goalId = :goalId
      AND isArchived = 0
    """
    )
    fun observeTasksForGoal(goalId: Long): Flow<List<TaskEntity>>

    // 2) Upsert a task
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTask(entity: TaskEntity): Long

    // 3) Update a task
    @Update
    suspend fun updateTask(entity: TaskEntity)

    // 4) Soft-delete (archive) a task
    @Query("UPDATE tasks SET isArchived = 1 WHERE id = :taskId")
    suspend fun archiveTask(taskId: Long)


}