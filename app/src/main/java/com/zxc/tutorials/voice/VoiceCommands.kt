package com.zxc.tutorials.voice

import android.Manifest
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
import com.zxc.tutorials.permission.PermissionCallback
import com.zxc.tutorials.permission.PermissionHelper


class VoiceCommands : AppCompatActivity() {

    private val TAG = "VoiceCommands"

    private lateinit var mBinding: ActivityVoiceCommandsBinding

    private var speech: SpeechRecognizer? = null
    private var recognizerIntent: Intent? = null
    private var isListening = false

    private val iRCAudio = 1
    private var mPermissionUtil: PermissionHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPermissionUtil = PermissionHelper(this, mPermissionCallback)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_voice_commands)

        setUp()

        mBinding.textView.movementMethod = ScrollingMovementMethod()

        mBinding.btnStart.setOnClickListener {
            if (isListening) {
                stop()
            } else {
                askPermission()
            }
        }
    }

    private fun setUp() {
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
    }

    private fun askPermission() {
        mPermissionUtil!!.askPermission(iRCAudio, Manifest.permission.RECORD_AUDIO)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        mPermissionUtil!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private val mPermissionCallback: PermissionCallback = object : PermissionCallback {
        override fun onGranted(requestCode: Int) {
            if (requestCode == iRCAudio) {
                start()
            }
        }

        override fun onDenied(requestCode: Int, isNeverAskAgain: Boolean) {
            if (requestCode == iRCAudio) {
                if (isNeverAskAgain) {
                    mPermissionUtil!!.neverAskDialog()
                } else {
                    mPermissionUtil!!.askAgainDialog("App needs record audio permission to work properly.")
                }
            }
        }
    }

    private fun start() {
        isListening = true
        mBinding.btnStart.text = "Stop"

        speech?.startListening(recognizerIntent)

        beep(true)
    }

    private fun stop() {
        isListening = false
        mBinding.btnStart.text = "Start"
        mBinding.textView.text = ""

        speech!!.stopListening()
        speech!!.destroy()

        beep(false)

        finish()
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
                    start()
                }
            } else {
                start()
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
                    start()
                }
            } else {
                start()
            }
        }

        override fun onEvent(eventType: Int, params: Bundle?) {
            Log.e(TAG, "onEvent: $eventType")
        }
    }

}