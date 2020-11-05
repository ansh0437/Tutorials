package com.zxc.tutorials.voice

import android.Manifest
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.zxc.tutorials.R
import com.zxc.tutorials.databinding.ActivitySphnixVoiceBinding
import edu.cmu.pocketsphinx.*
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference

class SphnixVoiceActivity : AppCompatActivity(), RecognitionListener {

    private lateinit var mBinding: ActivitySphnixVoiceBinding

    private val iRCAudio = 1

    /* Keyword we are looking for to activate menu */
    private val keyphrase = "enstructor"

    private val kwsSearch = "wakeup"
    private val commandSearch = "command"

    private var recognizer: SpeechRecognizer? = null
    private var captions = hashMapOf<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sphnix_voice)

        captions[kwsSearch] = R.string.wake_caption
        captions[commandSearch] = R.string.command_caption

        mBinding.tvCaption.text = "Preparing the recognizer"

        // Check if user has given permission to record audio
        val permissionCheck =
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.RECORD_AUDIO)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                iRCAudio
            )
            return
        }

        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task
        SetupTask(this).execute()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == iRCAudio) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SetupTask(this).execute()
            } else {
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (recognizer != null) {
            recognizer?.cancel()
            recognizer?.shutdown()
        }
    }

    private class SetupTask(activity: SphnixVoiceActivity?) :
        AsyncTask<Void?, Void?, Exception?>() {

        var activityReference: WeakReference<SphnixVoiceActivity> = WeakReference(activity!!)

        override fun doInBackground(vararg params: Void?): Exception? {
            try {
                val assets = Assets(activityReference.get())
                val assetDir: File = assets.syncAssets()
                activityReference.get()?.setupRecognizer(assetDir)
            } catch (e: IOException) {
                return e
            }
            return null
        }

        override fun onPostExecute(result: Exception?) {
            if (result != null) {
                activityReference.get()?.mBinding?.tvCaption?.text =
                    "Failed to init recognizer $result"
            } else {
                activityReference.get()?.switchSearch(activityReference.get()?.kwsSearch!!)
            }
        }
    }

    private fun setupRecognizer(assetsDir: File) {
        recognizer = SpeechRecognizerSetup.defaultSetup()
            .setAcousticModel(File(assetsDir, "en-us-ptm"))
            .setDictionary(File(assetsDir, "cmudict-en-us.dict"))
            .setRawLogDir(assetsDir)
            .recognizer

        recognizer?.addListener(this@SphnixVoiceActivity)

        recognizer?.addKeyphraseSearch(kwsSearch, keyphrase)

        val commandGrammar = File(assetsDir, "command.gram")
        recognizer?.addGrammarSearch(commandSearch, commandGrammar)
    }

    private fun switchSearch(searchName: String) {
        recognizer?.stop()
        if (searchName == kwsSearch) {
            recognizer?.startListening(searchName)
        } else {
            recognizer?.startListening(searchName, 10000)
        }
        mBinding.tvCaption.text = resources.getString(captions[searchName]!!)
    }

    override fun onPartialResult(hypothesis: Hypothesis?) {
        if (hypothesis == null) return
        val text = hypothesis.hypstr
        if (text == keyphrase) {
            switchSearch(commandSearch)
        } else {
            mBinding.tvResult.text = text
        }
    }

    override fun onResult(hypothesis: Hypothesis?) {
        mBinding.tvResult.text = ""
        if (hypothesis != null) {
            val text = hypothesis.hypstr
            makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBeginningOfSpeech() {}

    override fun onEndOfSpeech() {
        if (!recognizer?.searchName.equals(kwsSearch)) switchSearch(kwsSearch)
    }

    override fun onError(error: Exception) {
        mBinding.tvCaption.text = error.message
    }

    override fun onTimeout() {
        switchSearch(kwsSearch)
    }
}