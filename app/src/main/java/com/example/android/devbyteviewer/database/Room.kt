/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.devbyteviewer.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface VideoDao {
    @Query("select * from databaseVideo")
    fun getAllVideos() : LiveData<List<DatabaseVideo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllVideos(vararg videos: DatabaseVideo)
}

//Create Room DB
@Database(entities = [DatabaseVideo::class], version = 1)
abstract class VideoDatabase : RoomDatabase() {
    //Define Dao
    abstract val videoDao : VideoDao
}

//Access DB instance as singleton

//First we define a private DB instance
private lateinit var INSTANCE : VideoDatabase

//Then we define a getter
fun getDatabase(context : Context) : VideoDatabase {

    //Call this within synchronized so that it is thread safe..
    synchronized(VideoDatabase::class.java) {
        //Check if instance is not null
        if (!::INSTANCE.isInitialized) {
            //Initialize DB
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    VideoDatabase::class.java,"videos").build()
        }
    }
    return INSTANCE
}
