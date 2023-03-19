package com.bdmendes.activitytutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class PersonView : AppCompatActivity() {
    private val personNameText by lazy { findViewById<TextView>(R.id.personName) }
    private val personFace by lazy { findViewById<ImageView>(R.id.personFace) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_view)

        val personName = intent.getStringExtra("person")
        personNameText.text = personName

        // get drawable from intent
        val drawableFace = intent.getIntExtra("face", 0)
        personFace.setBackgroundResource(drawableFace)
    }
}