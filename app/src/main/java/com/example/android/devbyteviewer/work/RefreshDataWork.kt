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

package com.example.android.devbyteviewer.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android.devbyteviewer.database.getDatabase
import com.example.android.devbyteviewer.repository.VideosRepository
import retrofit2.HttpException


class RefreshDataWork (val appContext: Context, val params: WorkerParameters)
    : CoroutineWorker(appContext, params) {

    companion object {
        val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        //Fetch videos
        val database = getDatabase(appContext)
        val videosRepository = VideosRepository(database)

        return try {
            videosRepository.refreshVideos()
            Result.success()

        }catch (exception : HttpException) {
            Result.failure()
        }

    }

}
