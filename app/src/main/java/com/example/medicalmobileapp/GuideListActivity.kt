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
                GuideStep("Садитесь прямо и наклонитесь слегка вперёд."),
                GuideStep("Защёлкните ноздрю и прижмите её большим и указательным пальцем."),
                GuideStep("Дышите через рот, удерживайте давление 10–15 минут."),
                GuideStep("Нанесите холодный компресс на переносицу, если кровотечение сильное."),
                GuideStep("Если кровотечение не прекращается — обратитесь в медпункт.")
            )
        ),
        Guide(
            title = "Ожог — первая помощь",
            steps = listOf(
                GuideStep("Охладите ожог под струёй прохладной воды 10–20 минут."),
                GuideStep("Не используйте лед прямо на кожу."),
                GuideStep("Снимите украшения и одежду, если они не прилипли."),
                GuideStep("Накройте стерильной повязкой и обратитесь к врачу при глубоком ожоге.")
            )
        ),
        Guide(
            title = "Ушиб и растяжение",
            steps = listOf(
                GuideStep("Приложите холод к месту ушиба на 10–15 минут."),
                GuideStep("Приподнимите повреждённую конечность."),
                GuideStep("При сильной боли обратитесь к врачу.")
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
                putParcelableArrayListExtra("steps", ArrayList(guide.steps))
                putExtra("title", guide.title)
            }
            startActivity(intent)
        }
    }
}
