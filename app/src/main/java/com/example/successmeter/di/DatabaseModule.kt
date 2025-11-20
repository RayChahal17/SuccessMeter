package com.example.successmeter.di

import android.content.Context
import androidx.room.Room
import com.example.successmeter.data.local.db.AppDatabase
import com.example.successmeter.data.local.db.dao.ChiefAimDao
import com.example.successmeter.data.local.db.dao.GoalDao
import com.example.successmeter.data.local.db.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    //@Module: this class provides things for Hilt to deliver.
    //@InstallIn(SingletonComponent::class): everything provided here lives
    //as a singleton for the entire app process (created once, reused everywhere).
    //object: no need to instantiate this class yourself.
    @Provides @Singleton
    fun provideDb(@ApplicationContext ctx: Context): AppDatabase=
        Room.databaseBuilder(ctx, AppDatabase::class.java, "successmeter.db")
            .fallbackToDestructiveMigration()
            .build()
    //@Provides: “Hilt, when someone asks for AppDatabase, use this function to create it.”
    //@Singleton: create one database instance for the whole app (this is correct for Room).
    //@ApplicationContext: gives you the app Context (safe to use for DB).
    //Room.databaseBuilder(...): builds the actual Room DB.
    //"successmeter.db": the filename on disk.
    //.fallbackToDestructiveMigration(): if schema versions don’t match and you haven’t written a migration,
    //Room will wipe and recreate the DB (good for early development; later you’ll add proper migrations).
    @Provides
    fun providesQuoteDao(db: AppDatabase) = db.quoteDao()
    //“When someone asks for QuoteDao, take the already-built AppDatabase and return db.quoteDao().”
    //We don’t mark this @Singleton because Room already gives you a single DAO tied to the singleton DB;
    //scoping it isn’t necessary.
    // ***** The mental model =>
    //Room Database = your “warehouse” that stores data.
    //DAO (e.g., QuoteDao) = the “counter” at the warehouse to read/write data.
    //Hilt = a “delivery robot” that brings the warehouse and the counter to whatever class needs them,
    //without you manually building them everywhere.
    //After that, you simply ask for QuoteDao (or the repository)
    // with @Inject constructor(...) and Hilt brings it to you. *****

    @Provides
    fun provideChiefAimDao(
        db: AppDatabase,
    ): ChiefAimDao = db.chiefAimDao()

    @Provides
    fun provideGoalDao(
        db: AppDatabase,
    ): GoalDao = db.goalDao()

    @Provides
    fun provideTaskDao(
        db: AppDatabase,
    ): TaskDao = db.taskDao()

}