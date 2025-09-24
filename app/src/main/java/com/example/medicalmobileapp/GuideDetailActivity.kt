package com.example.medicalmobileapp

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import android.widget.ImageButton

class GuideDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide_detail)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        val title = intent.getStringExtra("title") ?: "Гайд"
        val stepsArray = intent.getStringArrayExtra("steps") ?: arrayOf()

        val tvGuideTitle = findViewById<TextView>(R.id.tvGuideTitle)
        tvGuideTitle.text = title

        val stepsContainer = findViewById<LinearLayout>(R.id.stepsContainer)

        // добавляем шаги динамически: номер + placeholder image + текст
/*
        for ((index, step) in stepsArray.withIndex()) {
            val stepLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = 8
                    bottomMargin = 8
                }
                gravity = Gravity.CENTER_VERTICAL
            }

            // Placeholder image (потом заменим на реальные)
            val image = ImageView(this).apply {
                setImageResource(android.R.drawable.ic_menu_report_image)
                layoutParams = LinearLayout.LayoutParams(80, 80).apply {
                    marginEnd = 12
                }
                scaleType = ImageView.ScaleType.CENTER_INSIDE
            }

            // Текст шага (с номером)
            val tv = TextView(this).apply {
                text = step
                textSize = 16f
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
                )
            }

            stepLayout.addView(image)
            stepLayout.addView(tv)
            stepsContainer.addView(stepLayout)
        }
*/
        for ((index, step) in stepsArray.withIndex()) {
            val stepLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = 16
                    bottomMargin = 16
                }
            }

            // Текст шага
            val tv = TextView(this).apply {
                //text = "${index + 1}. $step"
                text = step
                textSize = 16f
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            // Заглушка вместо фото (цветной квадрат)
            val placeholder = ImageView(this).apply {
                setBackgroundColor(android.graphics.Color.LTGRAY) // серый цвет для примера
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    200 // высота "как фото", можно регулировать
                ).apply {
                    topMargin = 8
                }
                scaleType = ImageView.ScaleType.CENTER_CROP
            }

            stepLayout.addView(tv)
            stepLayout.addView(placeholder)
            stepsContainer.addView(stepLayout)
        }
    }
}

