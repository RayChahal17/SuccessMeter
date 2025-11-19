package com.example.successmeter.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.successmeter.data.local.db.entity.ChiefAimEntity
import com.example.successmeter.domain.model.ChiefAimRank
import kotlinx.coroutines.flow.Flow

@Dao
interface ChiefAimDao {

    @Query("SELECT * FROM chief_aims WHERE isArchived = 0")
    fun observeAllChiefAims(): Flow<List<ChiefAimEntity>>

    @Query("""
        SELECT * FROM chief_aims
        where rank = :rank
        AND isArchived = 0
        LIMIT 1
    """)
    suspend fun getChiefAimByRank(rank: ChiefAimRank): ChiefAimEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertChiefAim(entity: ChiefAimEntity): Long

    @Update
    suspend fun updateChiefAim(entity: ChiefAimEntity)

    @Query("UPDATE chief_aims SET isArchived = 1 WHERE id = :id")
    suspend fun archiveChiefAim(id: Long)


}





