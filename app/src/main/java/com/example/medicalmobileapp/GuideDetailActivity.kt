package com.example.medicalmobileapp

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GuideDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide_detail)

        // Кнопка "назад"
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        // Заголовок гайда
        val title = intent.getStringExtra("title") ?: "Гайд"
        findViewById<TextView>(R.id.tvGuideTitle).text = title

        // Список шагов передан через Intent
        //val steps = intent.extras?.getParcelableArrayList<GuideStep>("steps") ?: arrayListOf()
        //val steps = intent.extras?.getParcelableArrayList<GuideStep>("steps") ?: arrayListOf()
        @Suppress("DEPRECATION")
        val steps = intent.getParcelableArrayListExtra<GuideStep>("steps") ?: arrayListOf()



        // RecyclerView для шагов
        val recyclerView = findViewById<RecyclerView>(R.id.stepsContainer)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = GuideStepAdapter(steps)
    }
}
