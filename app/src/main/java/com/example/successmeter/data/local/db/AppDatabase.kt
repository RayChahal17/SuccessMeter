package com.example.successmeter.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.successmeter.data.local.db.dao.ChiefAimDao
import com.example.successmeter.data.local.db.dao.GoalDao
import com.example.successmeter.data.local.db.dao.QuoteDao
import com.example.successmeter.data.local.db.dao.TaskDao
import com.example.successmeter.data.local.db.entity.QuoteEntity

@Database(entities = [QuoteEntity::class], version = 2, exportSchema = false)

abstract class AppDatabase : RoomDatabase(){
    abstract fun quoteDao(): QuoteDao

    abstract fun chiefAimDao(): ChiefAimDao
    abstract fun goalDao(): GoalDao
    abstract fun taskDao(): TaskDao


    // " abstract fun quoteDao(): QuoteDao" -->
    // You’re seeing two different things that just happen to look similar:
    //quoteDao (left side) → the function name
    //: QuoteDao (right side) → the function’s return type (an interface)
    //So the full line reads as:
    //“Declare an abstract function named quoteDao that returns a QuoteDao.”
    //Why it’s written this way
    //AppDatabase is an abstract Room database. You list your @Dao interfaces (like QuoteDao) as abstract getter functions.
    //Room generates a concrete implementation of AppDatabase at compile time.
    // In that generated class, it overrides quoteDao() and returns a real QuoteDao implementation under the hood.
}


