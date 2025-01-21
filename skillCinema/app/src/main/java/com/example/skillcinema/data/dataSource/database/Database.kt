package com.example.skillcinema.data.dataSource.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.skillcinema.data.dataSource.database.model.MyCollection
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

object Database {

    lateinit var instance: SkillCinemaDatabase
        private set

    fun init(context: Context) {
        instance = Room.databaseBuilder(
            context,
            SkillCinemaDatabase::class.java,
            SkillCinemaDatabase.DB_NAME
        )
            .addCallback(callback)
            .build()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private val callback = object: RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val desiredMovie = MyCollection(0, "Хочу посмотреть", 0)
            val likedMovie = MyCollection(0, "Любимые", 0)

            GlobalScope.launch {
                instance.collectionAndMovieDao().insertMyCollection(desiredMovie)
                instance.collectionAndMovieDao().insertMyCollection(likedMovie)
            }
        }
    }
}