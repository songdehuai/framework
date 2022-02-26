package com.ppz.framwork.tools

import android.speech.tts.TextToSpeech
import com.ppz.framwork.App
import com.ppz.framwork.ext.ifFalse
import com.ppz.framwork.ext.ifTrue
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object TTSUtils {


    private val ttsExecutorService: ExecutorService = Executors.newFixedThreadPool(5)

    private var textToSpeech: TextToSpeech? = null


    fun init() {
        textToSpeech = TextToSpeech(App.app) {
            if (it == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.CHINA
            }
        }
    }


    fun stop() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }

    fun destroy() {
        textToSpeech?.isSpeaking?.ifTrue {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
    }

    fun speak(text: String) {
        textToSpeech?.isSpeaking?.ifFalse {
            ttsExecutorService.execute {
                textToSpeech?.setPitch(1.5f)
                //设定语速 ，默认1.0正常语速
                textToSpeech?.setSpeechRate(1.5f)
                //朗读，注意这里三个参数的added in API level 4   四个参数的added in API level 21
                textToSpeech?.speak(text, 1, null, "1")
            }
        }
    }
}