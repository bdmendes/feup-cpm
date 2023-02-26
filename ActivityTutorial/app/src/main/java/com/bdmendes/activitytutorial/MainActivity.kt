package com.bdmendes.activitytutorial

import android.content.ClipData.newIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup

class MainActivity : AppCompatActivity() {
    private val radioGroup: RadioGroup by lazy { findViewById<RadioGroup>(R.id.radioGroup) }
    private val selectButton: Button by lazy { findViewById<Button>(R.id.selectButton) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectButton.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            val selectedButton = findViewById<Button>(selectedId)
            val personText = selectedButton.text

            val drawableFace = when(selectedId) {
                R.id.radioButtonBDMendes -> R.drawable.face1
                R.id.radioButtonSirze -> R.drawable.face2
                R.id.radioButtonNando -> R.drawable.face3
                else -> R.drawable.face4
            }

            val intent = Intent(this, PersonView::class.java)
            intent.putExtra("person", personText)
            intent.putExtra("face", drawableFace)
            startActivity(intent)
        }
    }
}