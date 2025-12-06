package com.example.medicalmobileapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.ComponentActivity
import java.util.Date
import java.util.*
class GuideListActivity : ComponentActivity() {

    private val guides = listOf(
        Guide(
            title = "Кровь из носа — что делать?",
            steps = listOf(
                GuideStep("Садитесь прямо и наклонитесь слегка вперёд.",imageResId = R.drawable.guide_1),
                GuideStep("Защёлкните ноздрю и прижмите её большим и указательным пальцем.",imageResId = R.drawable.guide_1),
                GuideStep("Дышите через рот, удерживайте давление 10–15 минут.",imageResId = R.drawable.guide_1)
            ),
            createdAt = Calendar.getInstance().apply { set(2023, Calendar.OCTOBER, 29) }.time
        ),
        Guide(
            title = "Ожог — первая помощь",
            steps = listOf(
                GuideStep("Охладите ожог под струёй прохладной воды 10–20 минут.",imageResId = R.drawable.guide_1),
                GuideStep("Не используйте лед прямо на кожу.",imageResId = R.drawable.guide_1)
            ),
            createdAt = Calendar.getInstance().apply { set(2023, Calendar.OCTOBER, 28) }.time
        ),
        Guide(
            title = "Ушиб и растяжение",
            steps = listOf(
                GuideStep("Приложите холод к месту ушиба на 10–15 минут.",imageResId = R.drawable.guide_1),
                GuideStep("Приподнимите повреждённую конечность.",imageResId = R.drawable.guide_1),
                GuideStep("При сильной боли обратитесь к врачу.",imageResId = R.drawable.guide_1),
                GuideStep("При необходимости наложите повязку.",imageResId = R.drawable.guide_1)
            ),
            createdAt = Calendar.getInstance().apply { set(2023, Calendar.OCTOBER, 27) }.time
        ),
        Guide(
            title = "Первая помощь при укусе насекомого",
            steps = listOf(
                GuideStep("Очистите место укуса водой с мылом."),
                GuideStep("Прикладывайте холодный компресс.")
            ),
            createdAt = Calendar.getInstance().apply { set(2023, Calendar.OCTOBER, 26) }.time
        ),
        Guide(
            title = "Ожоги химическими веществами",
            steps = listOf(
                GuideStep("Снимите загрязнённую одежду."),
                GuideStep("Промойте поражённый участок большим количеством воды."),
                GuideStep("Обратитесь к врачу."),
                GuideStep("Не трите кожу.")
            ),
            createdAt = Calendar.getInstance().apply { set(2023, Calendar.OCTOBER, 25) }.time
        ),
        Guide(
            title = "Обморок",
            steps = listOf(
                GuideStep("Уложите человека на спину."),
                GuideStep("Поднимите ноги выше уровня сердца.")
            ),
            createdAt = Calendar.getInstance().apply { set(2023, Calendar.OCTOBER, 24) }.time
        ),
        Guide(
            title = "Кашель и удушье",
            steps = listOf(
                GuideStep("Попросите человека наклониться вперёд."),
                GuideStep("Если предмет застрял, сделайте приём Геймлиха."),
                GuideStep("Вызовите скорую при полной непроходимости дыхательных путей.")
            ),
            createdAt = Calendar.getInstance().apply { set(2023, Calendar.OCTOBER, 23) }.time
        ),
        Guide(
            title = "Инсульт",
            steps = listOf(
                GuideStep("Определите признаки инсульта (лицо, речь, рука)."),
                GuideStep("Вызовите скорую помощь.")
            ),
            createdAt = Calendar.getInstance().apply { set(2023, Calendar.OCTOBER, 22) }.time
        ),
        Guide(
            title = "Солнечный удар",
            steps = listOf(
                GuideStep("Перенесите человека в прохладное место."),
                GuideStep("Обеспечьте доступ свежего воздуха."),
                GuideStep("При потере сознания вызовите скорую."),
                GuideStep("Охладите голову влажным полотенцем.")
            ),
            createdAt = Calendar.getInstance().apply { set(2023, Calendar.OCTOBER, 21) }.time
        ),
        Guide(
            title = "Порезы и раны",
            steps = listOf(
                GuideStep("Остановите кровотечение."),
                GuideStep("Очистите рану.")
            ),
            createdAt = Calendar.getInstance().apply { set(2023, Calendar.OCTOBER, 20) }.time
        )
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide_list)

        val lvGuides = findViewById<ListView>(R.id.lvGuides)
        //val titles = guides.map { it.title }

        //val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, titles)
        val adapter = GuideListAdapter(this, guides)
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