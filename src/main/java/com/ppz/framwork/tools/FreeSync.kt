package com.ppz.framwork.tools

import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author songdehuai
 */
object FreeSync {

    private val DEFAULT_FREE_SYNC_NAME by lazy { UUID.randomUUID().toString() }
    private val syncMaps by lazy { ConcurrentHashMap<Any, Sync>() }

    fun default(): Sync {
        with(DEFAULT_FREE_SYNC_NAME)
        return syncMaps[DEFAULT_FREE_SYNC_NAME]!!
    }

    fun with(key: Any = DEFAULT_FREE_SYNC_NAME): Sync {
        val keyStr = key.toString()
        if (!syncMaps.containsKey(keyStr)) {
            syncMaps[keyStr] = Sync()
        }
        return syncMaps[keyStr]!!
    }

    fun remove(key: String) {
        syncMaps.remove(key)
    }
}

class Sync {

    private val freeSyncCallbackHashMaps by lazy { ConcurrentHashMap<Any, CopyOnWriteArrayList<(Any?) -> Unit>>() }

    fun <T> addCall(key: Any, callBack: (T?) -> Unit) {
        val keyStr = key.toString()
        if (freeSyncCallbackHashMaps.containsKey(keyStr)) {
            freeSyncCallbackHashMaps[keyStr]?.add(callBack as ((Any?) -> Unit)?)
        } else {
            freeSyncCallbackHashMaps[keyStr] =
                CopyOnWriteArrayList<(Any?) -> Unit>().also { it.add(callBack as ((Any?) -> Unit)?) }
        }
    }

    fun call(key: Any, isRemove: Boolean = false) {
        call<Any>(key, null, isRemove)
    }

    fun <T> call(key: Any, value: T? = null, isRemove: Boolean = false) {
        val keyStr = key.toString()
        if (!freeSyncCallbackHashMaps.containsKey(keyStr)) {
            return
        }
        freeSyncCallbackHashMaps.forEach { maps ->
            if (maps.key == keyStr) {
                val callBacks = maps.value
                callBacks.forEach { call ->
                    call.invoke(value)
                }
            }
        }
        if (isRemove) {
            freeSyncCallbackHashMaps.remove(keyStr)
        }
    }

    fun remove(key: String) {
        freeSyncCallbackHashMaps.remove(key)
    }
}