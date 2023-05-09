package com.ebatashota.coroutineasync.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class AsyncSampleViewModel : BaseSampleViewModel() {

    val types: List<MyTask> = listOf(
        MyTask(
            typeName = "type1",
            task = { typeName ->
                log.resetReplayCache()
                viewModelScope.launch {
                    log.emit(
                        """
                        2つのsuspend関数を
                        直列に並べたただけ
                        
                        val a = mySuspend() A
                        val b = mySuspend() B
                        
                    """.trimIndent()
                    )
                    log.emit("↓======== $typeName ========↓")
                    val a = mySuspend("A", 1000L)
                    val b = mySuspend("B", 300L)
                    log.emit("a = $a")
                    log.emit("b = $b")
                    log.emit("↑======== $typeName ========↑")
                }
            }
        ),
        MyTask(
            typeName = "type2",
            task = { typeName ->
                log.resetReplayCache()
                viewModelScope.launch {
                    log.emit(
                        """
                        2つのsuspend関数をそれぞれasyncで囲んで
                        それぞれawaitで取得する
                        
                        val aDeferred = async { mySuspend() A }
                        val bDeferred = async { mySuspend() B }
                        val a = aDeferred.await()
                        val b = bDeferred.await()
                        
                    """.trimIndent()
                    )
                    log.emit("↓======== $typeName ========↓")
                    val aDeferred = async { mySuspend("A", 1000L) }
                    val bDeferred = async { mySuspend("B", 300L) }
                    val a = aDeferred.await()
                    val b = bDeferred.await()
                    log.emit("a = $a")
                    log.emit("b = $b")
                    log.emit("↑======== $typeName ========↑")
                }
            }
        ),
        MyTask(
            typeName = "type3",
            task = { typeName ->
                log.resetReplayCache()
                viewModelScope.launch {
                    log.emit(
                        """
                        2つのsuspend関数をasyncで囲んで
                        awaitした2つの間で、別のsuspendを呼び出す
                        
                        val aDeferred = async { mySuspend() A }
                        val bDeferred = async { mySuspend() B }
                        val a = aDeferred.await()
                        val c = mySuspend() C
                        val b = bDeferred.await()
                        
                    """.trimIndent()
                    )
                    log.emit("↓======== $typeName ========↓")
                    val aDeferred = async { mySuspend("A", 300L) }
                    val bDeferred = async { mySuspend("B", 1000L) }
                    val a = aDeferred.await()
                    val c = mySuspend("C", 700L)
                    val b = bDeferred.await()
                    log.emit("a = $a")
                    log.emit("b = $b")
                    log.emit("c = $c")
                    log.emit("↑======== $typeName ========↑")
                }
            }
        ),
        MyTask(
            typeName = "type4",
            task = { typeName ->
                log.resetReplayCache()
                viewModelScope.launch {
                    log.emit(
                        """
                        2つのsuspend関数をasyncで囲んで
                        その間に別のsuspend関数を呼び出す
                        
                        val aDeferred = async { mySuspend() A }
                        val b = mySuspend() B
                        val cDeferred = async { mySuspend() C }
                        val a = aDeferred.await()
                        val c = cDeferred.await()
                    
                    """.trimIndent()
                    )
                    log.emit("↓======== $typeName ========↓")
                    val aDeferred = async { mySuspend("A", 1000L) }
                    val b = mySuspend("B", 700L)
                    val cDeferred = async { mySuspend("C", 300L) }
                    val a = aDeferred.await()
                    val c = cDeferred.await()
                    log.emit("a = $a")
                    log.emit("b = $b")
                    log.emit("c = $c")
                    log.emit("↑======== $typeName ========↑")
                }
            }
        ),
        MyTask(
            typeName = "type5",
            task = { typeName ->
                log.resetReplayCache()
                viewModelScope.launch {
                    log.emit(
                        """
                        2つのsuspend関数をそれぞれasyncで囲んで
                        片方のawait()を書かない
                        
                        val aDeferred = async { mySuspend() A }
                        val bDeferred = async { mySuspend() B }
                        val a = aDeferred.await()
                        
                    """.trimIndent()
                    )
                    log.emit("↓======== $typeName ========↓")
                    val aDeferred = async { mySuspend("A", 1000L) }
                    val bDeferred = async { mySuspend("B", 1500L) }
                    val a = aDeferred.await()
                    log.emit("a = $a")
                    log.emit("b = bDeferred.await() を書かない")
                    log.emit("↑======== $typeName ========↑")
                }
            }
        ),
        MyTask(
            typeName = "type6",
            task = { typeName ->
                log.resetReplayCache()
                val aDeferred = viewModelScope.async { mySuspend("A", 1000L) }
                viewModelScope.launch {
                    log.emit(
                        """
                        2つのsuspend関数をそれぞれasyncで囲んで
                        片方はlaunch外で開始する
                        
                        val aDeferred = async { mySuspend() A }
                        launch {
                            val bDeferred = async { mySuspend() B }
                            val a = aDeferred.await()
                            val b = bDeferred.await()
                        }
                        
                    """.trimIndent()
                    )
                    log.emit("↓======== $typeName ========↓")

                    val bDeferred = async { mySuspend("B", 1500L) }
                    val a = aDeferred.await()
                    val b = bDeferred.await()
                    log.emit("a = $a")
                    log.emit("b = $b")
                    log.emit("↑======== $typeName ========↑")
                }
            }
        ),
    )
}