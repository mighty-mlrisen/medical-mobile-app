package com.example.medicalmobileapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.ComponentActivity

class GuideListActivity : ComponentActivity() {

    private val guides = listOf(
        Guide(
            title = "Кровь из носа — что делать?",
            steps = listOf(
                "1. Садитесь прямо и наклонитесь слегка вперёд.",
                "2. Защёлкните ноздрю и прижмите её большим и указательным пальцем.",
                "3. Дышите через рот, удерживайте давление 10–15 минут.",
                "4. Нанесите холодный компресс на переносицу, если кровотечение сильное.",
                "5. Если кровотечение не прекращается — обратитесь в медпункт.",
                "4. Нанесите холодный компресс на переносицу, если кровотечение сильное.",
                "4. Нанесите холодный компресс на переносицу, если кровотечение сильное.",
                "4. Нанесите холодный компресс на переносицу, если кровотечение сильное.",
                "4. Нанесите холодный компресс на переносицу, если кровотечение сильное.",
                "4. Нанесите холодный компресс на переносицу, если кровотечение сильное.",
                "4. Нанесите холодный компресс на переносицу, если кровотечение сильное.",
                "4. Нанесите холодный компресс на переносицу, если кровотечение сильное."
            )
        ),
        Guide(
            title = "Ожог — первая помощь",
            steps = listOf(
                "1. Охладите ожог под струёй прохладной воды 10–20 минут.",
                "2. Не используйте лед прямо на кожу.",
                "3. Снимите украшения и одежду, если они не прилипли.",
                "4. Накройте стерильной повязкой и обратитесь к врачу при глубоком ожоге."
            )
        ),
        Guide(
            title = "Ушиб и растяжение",
            steps = listOf(
                "1. Приложите холод к месту ушиба на 10–15 минут.",
                "2. Приподнимите повреждённую конечность.",
                "3. При сильной боли обратитесь к врачу."
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide_list)

        val lvGuides = findViewById<ListView>(R.id.lvGuides)
        val titles = guides.map { it.title }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, titles)
        lvGuides.adapter = adapter

        lvGuides.setOnItemClickListener { _, _, position, _ ->
            val guide = guides[position]
            val intent = Intent(this, GuideDetailActivity::class.java).apply {
                putExtra("title", guide.title)
                putExtra("steps", guide.steps.toTypedArray())
            }
            startActivity(intent)
        }
    }
}
