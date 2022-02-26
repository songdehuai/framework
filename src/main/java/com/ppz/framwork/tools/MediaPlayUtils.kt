package com.ppz.framwork.tools

import android.media.MediaPlayer
import com.ppz.framwork.App
import java.io.IOException
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.concurrent.CopyOnWriteArrayList

class MediaPlayUtils private constructor() {

    private var mediaPlayer: MediaPlayer? = null

    private val mediaQueue = CopyOnWriteArrayList<Int>()

    fun addPlay(resId: Int) {
        try {
            mediaQueue.add(resId)
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer()
            }
            if (!mediaPlayer!!.isPlaying) {
                play()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clearPlay() {
        mediaPlayer?.stop()
        mediaQueue.clear()
    }

    fun release() {
        mediaPlayer?.release()
    }

    fun playBgm(resId: Int) {
        playRing(resId, true, null)
    }

    private fun play() {
        try {
            if (mediaQueue.isNotEmpty()) {
                playRing(mediaQueue[0], false) {
                    play()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun playRing(resId: Int, isLoop: Boolean = false, complet: (() -> Unit)? = null) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer!!.stop()
                mediaPlayer?.reset()
            } else {
                mediaPlayer = MediaPlayer()
            }
            val afd = App.app.resources.openRawResourceFd(resId)
            mediaPlayer!!.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            afd.close()
            mediaPlayer!!.isLooping = isLoop
            mediaPlayer!!.prepareAsync()
            mediaPlayer!!.setOnPreparedListener { mp: MediaPlayer? -> mediaPlayer!!.start() }
            mediaPlayer!!.setOnCompletionListener { mediaQueue.removeAt(0);complet?.invoke() }
        } catch (ex: IOException) {
            ex.printStackTrace()
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        } catch (ex: SecurityException) {
            ex.printStackTrace()
        }
    }


    companion object {

        private val playQueue = linkedMapOf<String, MediaPlayUtils>()
        private const val BGM = "BGM_WUBALUBADUBDUB"

        fun queue(key: String): MediaPlayUtils? {
            return if (playQueue.containsKey(key)) {
                playQueue[key]
            } else {
                MediaPlayUtils().also {
                    playQueue[key] = it
                }
            }
        }

        fun bgm(): MediaPlayUtils? {
            return queue(BGM)
        }
    }
}