package com.zxc.tutorials.voice

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.zxc.tutorials.R
import com.zxc.tutorials.databinding.ActivityVoiceCommandsBinding


class VoiceCommands : AppCompatActivity() {

    private val TAG = "VoiceCommands"

    private lateinit var mBinding: ActivityVoiceCommandsBinding

    private var speech: SpeechRecognizer? = null
    private var recognizerIntent: Intent? = null
    private var isListening = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_voice_commands)

        mBinding.textView.movementMethod = ScrollingMovementMethod()

        mBinding.btnStart.setOnClickListener {
            if (isListening) {
                stop()
            } else {
                isListening = true
                mBinding.btnStart.text = "Stop"
                start()
            }
        }
    }

    private fun start() {
        speech = SpeechRecognizer.createSpeechRecognizer(this)
        speech?.setRecognitionListener(mRecognitionListener)
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent?.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
            "en"
        )
        recognizerIntent?.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        recognizerIntent?.putExtra(
            RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,
            60 * 1000
        )
        recognizerIntent?.putExtra(
            RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
            2 * 60 * 1000
        )
        recognizerIntent?.putExtra(
            RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
            2 * 60 * 1000
        )
        recognizerIntent?.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)

        speech?.startListening(recognizerIntent)
    }

    private fun stop() {
        isListening = false
        mBinding.btnStart.text = "Start"
        mBinding.textView.text = ""

        speech!!.stopListening()
        speech!!.destroy()

        beep(false)
    }

    private fun beep(mute: Boolean) {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, mute)
        audioManager.setStreamMute(AudioManager.STREAM_ALARM, mute)
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, mute)
        audioManager.setStreamMute(AudioManager.STREAM_RING, mute)
        audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, mute)
    }

    private val mRecognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            Log.e(TAG, "onReadyForSpeech: ")
        }

        override fun onBeginningOfSpeech() {
            Log.e(TAG, "onBeginningOfSpeech: ")
        }

        override fun onRmsChanged(rmsdB: Float) {
            Log.e(TAG, "onRmsChanged: $rmsdB")
        }

        override fun onBufferReceived(buffer: ByteArray?) {
            Log.e(TAG, "onBufferReceived: ")
        }

        override fun onEndOfSpeech() {
            Log.e(TAG, "onEndOfSpeech: ")
        }

        override fun onError(error: Int) {
            Log.e(TAG, "onError: $error")
            beep(true)
            speech!!.startListening(recognizerIntent)
        }

        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (matches?.isNotEmpty() == true) {
                val text = matches[0]
                if (text == "stop") {
                    stop()
                } else {
                    mBinding.textView.text = text
                    beep(true)
                    speech!!.startListening(recognizerIntent)
                }
            } else {
                beep(true)
                speech!!.startListening(recognizerIntent)
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {
            val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (matches?.isNotEmpty() == true) {
                val text = matches[0]
                if (text == "stop") {
                    stop()
                } else {
                    mBinding.textView.text = text
                    beep(true)
                    speech!!.startListening(recognizerIntent)
                }
            } else {
                beep(true)
                speech!!.startListening(recognizerIntent)
            }
        }

        override fun onEvent(eventType: Int, params: Bundle?) {
            Log.e(TAG, "onEvent: $eventType")
        }
    }

}