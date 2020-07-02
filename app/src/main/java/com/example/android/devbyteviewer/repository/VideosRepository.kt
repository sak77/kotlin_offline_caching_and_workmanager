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

package com.example.android.devbyteviewer.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.devbyteviewer.database.VideoDatabase
import com.example.android.devbyteviewer.database.asDomainModel
import com.example.android.devbyteviewer.domain.Video
import com.example.android.devbyteviewer.network.Network
import com.example.android.devbyteviewer.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * A Repository is just a regular class that has one (or more) methods that load data without specifying the data source as part of the main API.
 * Because it's just a regular class, there's no need for an annotation to define a repository.
 * The repository hides the complexity of managing the interactions between the database and the networking code.
 */
class VideosRepository (val videoDatabase: VideoDatabase) {


    //This repository will have 2 methods. One to refresh videos and one to get all videos.

    //Refresh videos - suspend is kotlins way of saying a function is coroutine friendly
    suspend fun refreshVideos() {
        //Make n/w call using coroutines- using withContext(Dispatchers.IO) we force the code within the block to be run on the IO thread.
        withContext(Dispatchers.IO) {
            //Make network call  - await causes this function execution to wait for the network result in an ubblocking way..
            val playlist = Network.devbytes.getPlaylist().await()

            //Now insert all playlist objects to the DB.
            //Note the asterisk * is the spread operator. It allows you to pass in an array to a function that expects varargs.
            videoDatabase.videoDao.insertAllVideos(*playlist.asDatabaseModel())
        }
    }

    //get all videos from the DB.
    val videos : LiveData<List<Video>> = Transformations.map(videoDatabase.videoDao.getAllVideos()) {
        it.asDomainModel()
    }

}
