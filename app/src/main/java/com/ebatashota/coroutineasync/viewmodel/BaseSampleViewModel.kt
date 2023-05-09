package com.ebatashota.coroutineasync.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseSampleViewModel : ViewModel() {

    protected val log = MutableSharedFlow<String>(replay = 100)

    val logTextLiveData: LiveData<String> = log.mapLatest {
        Log.d("my_log", it)
        val stringBuilder = StringBuilder()
        log.replayCache.forEach {
            stringBuilder.append(it)
            stringBuilder.append("\n")
        }
        stringBuilder.toString()
    }.asLiveData()

    protected suspend fun mySuspend(name: String, delayTimeMillis: Long): Int {
        return withContext(Dispatchers.Default) {
            log.emit("---------start mySuspend() $name---------")
            var result = 0
            (1..5).forEach {
                result += (1..100).random()
                delay(delayTimeMillis)
                log.emit("mySuspend() $name - $it")
            }
            log.emit("---------finish mySuspend() $name---------")
            return@withContext result
        }
    }

    @Suppress("UNREACHABLE_CODE")
    protected suspend fun error(name: String, crashTime: Int = 5): Int {
        withContext(Dispatchers.Default) {
            log.emit("---------start error() $name---------")
            (crashTime downTo 0).forEach {
                if (it <= 0) {
                    log.emit("throw Exception!")
                    throw Exception("error")
                }
                log.emit("throw Exception まで あと${it}秒")
                delay(1000L)
            }
            log.emit("---------finish error() $name---------")
        }
        return (1..100).random()
    }

    class MyTask(
        val typeName: String,
        private val task: (String) -> Unit
    ) {
        fun run() {
            task.invoke(typeName)
        }
    }
}