package com.example.vamorachar

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.InputType
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextNumber = findViewById<EditText>(R.id.editTextNumber)
        val editTextNumberDecimal = findViewById<EditText>(R.id.editTextNumberDecimal)
        val textView2 = findViewById<TextView>(R.id.textView2)
        val textView3 = findViewById<TextView>(R.id.textView3)
        val imageButton2 = findViewById<ImageButton>(R.id.imageButton2)
        val imageButton4 = findViewById<ImageButton>(R.id.imageButton4)

        editTextNumberDecimal.inputType = InputType.TYPE_CLASS_NUMBER

        editTextNumber.addTextChangedListener {
            calculateAndDisplayResult(editTextNumber, editTextNumberDecimal, textView2)
        }

        editTextNumberDecimal.addTextChangedListener {
            calculateAndDisplayResult(editTextNumber, editTextNumberDecimal, textView2)
        }

        tts = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                tts.language = Locale("pt", "BR")
            }
        }

        imageButton2.setOnClickListener {
            val textToSpeak = "${textView3.text} ${textView2.text}"
            tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, "")
        }

        imageButton4.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "${textView3.text} ${textView2.text}")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun calculateAndDisplayResult(editTextNumber: EditText, editTextNumberDecimal: EditText, textView2: TextView) {
        val num1 = editTextNumber.text.toString().toDoubleOrNull() ?: return
        val num2 = editTextNumberDecimal.text.toString().toDoubleOrNull() ?: return
        val result = num1 / num2
        textView2.text = "R$ %.2f".format(result)
    }

    override fun onPause() {
        if (tts.isSpeaking) {
            tts.stop()
        }
        super.onPause()
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }
}