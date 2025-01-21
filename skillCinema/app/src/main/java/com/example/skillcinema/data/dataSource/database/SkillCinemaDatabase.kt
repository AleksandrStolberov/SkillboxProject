package com.example.skillcinema.data.dataSource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.skillcinema.data.dataSource.database.model.CachedMovie
import com.example.skillcinema.data.dataSource.database.model.CollectionAndMovie
import com.example.skillcinema.data.dataSource.database.model.CollectionMovie
import com.example.skillcinema.data.dataSource.database.model.MyCollection
import com.example.skillcinema.data.dataSource.database.model.ShownMovie

@Database(
    entities = [
        ShownMovie::class,
        MyCollection::class,
        CollectionMovie::class,
        CollectionAndMovie::class,
        CachedMovie::class],
    version = SkillCinemaDatabase.DB_VERSION
)
abstract class SkillCinemaDatabase : RoomDatabase() {

    abstract fun movieDao(): ShownMovieDao
    abstract fun collectionAndMovieDao(): CollectionAndMovieDao
    abstract fun cachedMovieDao(): CachedMovieDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "skillcinema_database"
    }
}