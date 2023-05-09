package com.ebatashota.coroutineasync.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

@OptIn(ExperimentalCoroutinesApi::class)
class ErrorSampleViewModel : BaseSampleViewModel() {

    val types: List<MyTask> = listOf(
        MyTask(
            typeName = "error type1",
            task = { typeName ->
                log.resetReplayCache()
                viewModelScope.launch {
                    log.emit(
                        """
                            2つのsuspend関数を
                            直列に並べたただけ
                            2つ目の関数で例外が発生するケース
                            
                            try {
                                val a = mySuspend() A
                                val b = error() B
                            }
                    
                    """.trimIndent()
                    )
                    log.emit("↓======== $typeName ========↓")
                    try {
                        val a = mySuspend("A", 1000L)
                        val b = error("B")
                        log.emit("a = $a")
                        log.emit("b = $b")
                        log.emit("↑======== $typeName ========↑")
                    } catch (e: Exception) {
                        log.emit("↑== $typeName catch Exception ==↑")
                    }
                }
            }
        ),
        MyTask(
            typeName = "error type2",
            task = { typeName ->
                log.resetReplayCache()
                viewModelScope.launch {
                    log.emit(
                        """
                            ※クラッシュします
                            
                            2つのsuspend関数をasyncで囲んで
                            2つ目の関数で例外が発生するケース
                            
                            try {
                                val aDeferred = async { mySuspend() A }
                                val bDeferred = async { error() B }
                                val a = aDeferred.await()
                                val b = bDeferred.await()
                            }
                        
                    """.trimIndent()
                    )
                    log.emit("↓======== $typeName ========↓")
                    try {
                        val aDeferred = async { mySuspend("A", 1000L) }
                        val bDeferred = async { error("B") }
                        val a = aDeferred.await()
                        val b = bDeferred.await()
                        log.emit("a = $a")
                        log.emit("b = $b")
                        log.emit("↑======== $typeName ========↑")
                    } catch (e: Exception) {
                        log.emit("↑== $typeName catch Exception ==↑")
                    }
                }
            }
        ),
        MyTask(
            typeName = "error type3",
            task = { typeName ->
                log.resetReplayCache()
                viewModelScope.launch {
                    log.emit(
                        """
                            2つのsuspend関数をasyncで囲んで
                            2つ目の関数で例外が発生するケース
                            
                            try {
                                coroutineScope {
                                    val aDeferred = async { mySuspend() A }
                                    val bDeferred = async { error() B }
                                    val a = aDeferred.await()
                                    val b = bDeferred.await()
                                }
                            }
                        
                    """.trimIndent()
                    )
                    log.emit("↓======== $typeName ========↓")
                    try {
                        coroutineScope {
                            val aDeferred = async { mySuspend("A", 1500L) }
                            val bDeferred = async { error("B") }
                            val a = aDeferred.await()
                            val b = bDeferred.await()
                            log.emit("a = $a")
                            log.emit("b = $b")
                            log.emit("↑======== $typeName ========↑")
                        }
                    } catch (e: Exception) {
                        log.emit("↑== $typeName catch Exception ==↑")
                    }
                }
            }
        ),
        MyTask(
            typeName = "error type4",
            task = { typeName ->
                log.resetReplayCache()
                viewModelScope.launch {
                    log.emit(
                        """
                            2つのsuspend関数をasyncで囲んで
                            2つ目の関数で例外が発生するケース
                            全体をsupervisorScopeで囲う
                            
                            try {
                                supervisorScope {
                                    val aDeferred = async { mySuspend() A }
                                    val bDeferred = async { error() B }
                                    val a = aDeferred.await()
                                    val b = bDeferred.await()
                                }
                            }
                        
                    """.trimIndent()
                    )
                    log.emit("↓======== $typeName ========↓")
                    try {
                        supervisorScope {
                            val aDeferred = async { mySuspend("A", 1000L) }
                            val bDeferred = async { error("B") }
                            val a = aDeferred.await()
                            val b = bDeferred.await()
                            log.emit("a = $a")
                            log.emit("b = $b")
                            log.emit("↑======== $typeName ========↑")
                        }
                    } catch (e: Exception) {
                        log.emit("↑== $typeName catch Exception ==↑")
                    }
                }
            }
        ),
        MyTask(
            typeName = "error type5",
            task = { typeName ->
                log.resetReplayCache()
                viewModelScope.launch {
                    log.emit(
                        """
                            2つのsuspend関数をそれぞれasyncで囲んで
                            errorが発生する async を coroutineScope で囲う
                            
                            launch {
                                val aDeferred = async { mySuspend() A }
                                val bDeferred = try {
                                    coroutineScope {
                                        async { error() B }
                                    }
                                }
                                val a = aDeferred.await() 
                                val b = bDeferred.await()
                            }
                        
                    """.trimIndent()
                    )
                    log.emit("↓======== $typeName ========↓")
                    val aDeferred = async { mySuspend("A", 1500L) }
                    val bDeferred = try {
                        coroutineScope {
                            async { error("B") }
                        }
                    } catch (e: Exception) {
                        log.emit("↑== $typeName catch Exception ==↑")
                        return@launch
                    }
                    val a = aDeferred.await()
                    val b = bDeferred.await()
                    log.emit("a = $a")
                    log.emit("b = $b")
                    log.emit("↑======== $typeName ========↑")
                }
            }
        ),
        MyTask(
            typeName = "error type6",
            task = { typeName ->
                log.resetReplayCache()
                viewModelScope.launch {
                    log.emit(
                        """
                            ※クラッシュします
                            
                            2つのsuspend関数をそれぞれasyncで囲んで
                            errorが発生する wait を coroutineScope で囲う
                            
                            launch {
                                val aDeferred = async { mySuspend() A }
                                val bDeferred = async { error() B }
                                val a = aDeferred.await() 
                                val b = try { 
                                    coroutineScope {
                                        bDeferred.await()
                                    }
                                }
                            }
                        
                    """.trimIndent()
                    )
                    log.emit("↓======== $typeName ========↓")
                    val aDeferred = async { mySuspend("A", 1500L) }
                    val bDeferred = async { error("B") }
                    val a = aDeferred.await()
                    val b = try {
                        coroutineScope {
                            bDeferred.await()
                        }
                    } catch (e: Exception) {
                        log.emit("↑== $typeName catch Exception ==↑")
                        return@launch
                    }
                    log.emit("a = $a")
                    log.emit("b = $b")
                    log.emit("↑======== $typeName ========↑")
                }
            }
        ),
        MyTask(
            typeName = "error type7",
            task = { typeName ->
                log.resetReplayCache()
                viewModelScope.launch {
                    log.emit(
                        """
                            ※クラッシュします
                            
                            2つのsuspend関数をそれぞれasyncで囲んで
                            errorが発生する async を coroutineScope で囲う
                            
                            launch {
                                val aDeferred = async { mySuspend() A }
                                val bDeferred = coroutineScope {
                                    async { error() B }
                                }
                                val a = aDeferred.await() 
                                val b = try { 
                                    bDeferred.await()
                                }
                            }
                        
                    """.trimIndent()
                    )
                    log.emit("↓======== $typeName ========↓")
                    val aDeferred = async { mySuspend("A", 1500L) }
                    val bDeferred = coroutineScope {
                        async { error("B") }
                    }
                    val a = aDeferred.await()
                    val b = try {
                        bDeferred.await()
                    } catch (e: Exception) {
                        log.emit("↑== $typeName catch Exception ==↑")
                        return@launch
                    }
                    log.emit("a = $a")
                    log.emit("b = $b")
                    log.emit("↑======== $typeName ========↑")
                }
            }
        ),
        MyTask(
            typeName = "error type8",
            task = { typeName ->
                log.resetReplayCache()
                viewModelScope.launch {
                    log.emit(
                        """
                            2つのsuspend関数をそれぞれasyncで囲んで
                            errorが発生する async を supervisorScope で囲う
                            
                            launch {
                                val aDeferred = async { mySuspend() A }
                                val bDeferred = supervisorScope {
                                    async { error() B }
                                }
                                val a = aDeferred.await() 
                                val b = try { 
                                    bDeferred.await()
                                }
                            }
                        
                    """.trimIndent()
                    )
                    log.emit("↓======== $typeName ========↓")
                    val aDeferred = async { mySuspend("A", 1500L) }
                    val bDeferred = supervisorScope {
                        async { error("B") }
                    }
                    val a = aDeferred.await()
                    val b = try {
                        bDeferred.await()
                    } catch (e: Exception) {
                        log.emit("↑== $typeName catch Exception ==↑")
                        return@launch
                    }
                    log.emit("a = $a")
                    log.emit("b = $b")
                    log.emit("↑======== $typeName ========↑")
                }
            }
        ),
        MyTask(
            typeName = "error type9",
            task = { typeName ->
                log.resetReplayCache()
                viewModelScope.launch {
                    log.emit(
                        """
                            ※クラッシュします
                            
                            2つのsuspend関数をそれぞれasyncで囲んで
                            errorが発生する async を supervisorScope で囲う
                            
                            launch {
                                val aDeferred = async { mySuspend() A }
                                val bDeferred = supervisorScope {
                                    async { error() B }
                                }
                                val a = aDeferred.await() 
                                val b = bDeferred.await()
                            }
                        
                    """.trimIndent()
                    )
                    log.emit("↓======== $typeName ========↓")
                    val aDeferred = async { mySuspend("A", 1500L) }
                    val bDeferred = supervisorScope {
                        async { error("B") }
                    }
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