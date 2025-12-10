package com.example.medicalmobileapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.example.medicalmobileapp.DB.GuideDbHelper
import java.util.*
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import android.content.res.Configuration




class GuideListActivity : AppCompatActivity() {

    private lateinit var lvGuides: ListView
    private lateinit var dbHelper: GuideDbHelper
    private var lang: String = "ru" // по умолчанию


    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private var lowLightWarningShown = false


    override fun onCreate(savedInstanceState: Bundle?) {

        val prefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val lang = prefs.getString("lang", Locale.getDefault().language) ?: "ru"

        // Применяем локаль
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide_list)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        lvGuides = findViewById(R.id.lvGuides)
        dbHelper = GuideDbHelper(this)

        // Получаем язык из Intent
        //lang = intent.getStringExtra("lang") ?: Locale.getDefault().language

        val db = dbHelper.writableDatabase


        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        if (savedInstanceState != null) {
            lowLightWarningShown = savedInstanceState.getBoolean("KEY_WARNING_SHOWN", false)
        }

        /*
        // Очищаем таблицу при каждом запуске (для теста)
        db.delete("guides", null, null)

        // Вставляем примерные данные
        val exampleGuides = ExampleData.getExampleGuides()
        exampleGuides.forEach { guideData ->
            dbHelper.insertGuide(
                db,
                guideData.guide,
                guideData.titleRu,
                guideData.titleEn,
                guideData.stepsRu,
                guideData.stepsEn,
                guideData.imageResIds
            )
        }

         */

        val cursor = db.query("guides", arrayOf("id"), null, null, null, null, null)
        if (!cursor.moveToFirst()) { // если база пустая
            val exampleGuides = ExampleData.getExampleGuides()
            exampleGuides.forEach { guideData ->
                dbHelper.insertGuide(
                    db,
                    guideData.guide,
                    guideData.titleRu,
                    guideData.titleEn,
                    guideData.stepsRu,
                    guideData.stepsEn,
                    guideData.imageResIds
                )
            }
        }
        cursor.close()

        loadGuides()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("KEY_WARNING_SHOWN", lowLightWarningShown)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.lang_ru -> setLocale("ru")
            R.id.lang_en -> setLocale("en")
        }
        return true
    }

    /*
    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        recreate()
        // Перезапускаем текущую активити, чтобы обновить язык
        //val intent = Intent(this, GuideListActivity::class.java)
        //intent.putExtra("lang", lang)
        //startActivity(intent)
        //finish()
    }

     */

    private fun setLocale(lang: String) {
        // Сохраняем язык в SharedPreferences
        val prefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        prefs.edit().putString("lang", lang).apply()

        // Применяем локаль
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Перезапускаем Activity, чтобы применить язык
        recreate()
    }



    private fun loadGuides() {
        val langFromIntent = intent.getStringExtra("lang")
        val langToUse = langFromIntent ?: Locale.getDefault().language

        val db = dbHelper.readableDatabase

        lifecycleScope.launch {
            val guides = dbHelper.getGuidesAsync(db, langToUse)

            val adapter = GuideListAdapter(this@GuideListActivity, guides)
            lvGuides.adapter = adapter

            lvGuides.setOnItemClickListener { _, _, position, _ ->
                val guide = guides[position]
                val intent = Intent(this@GuideListActivity, GuideDetailActivity::class.java).apply {
                    putParcelableArrayListExtra("steps", ArrayList(guide.steps))
                    putExtra("title", guide.title)
                    putExtra("lang", lang)
                }
                startActivity(intent)
            }
        }
    }



    private val lightSensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val lightLevel = event.values[0]

            /*
            if (lightLevel < 100f && !lowLightWarningShown) {
                val locale = Locale(lang) // lang - текущий язык Activity
                val localizedContext = this@GuideListActivity.createConfigurationContext(
                    resources.configuration.apply { setLocale(locale) }
                )
                val message = localizedContext.getString(R.string.low_light_warning)
                Toast.makeText(this@GuideListActivity, message, Toast.LENGTH_SHORT).show()
                lowLightWarningShown = true
            } else if (lightLevel >= 150f) {
                lowLightWarningShown = false
            }

             */

            if (lightLevel < 100f && !lowLightWarningShown) {
                // Получаем язык из SharedPreferences или переменной lang
                val prefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
                val currentLang = prefs.getString("lang", lang) ?: lang

                // Создаём локализованный контекст
                val config = resources.configuration
                val locale = Locale(currentLang)
                val localizedConfig = Configuration(config)
                localizedConfig.setLocale(locale)
                val localizedContext = createConfigurationContext(localizedConfig)

                val message = localizedContext.getString(R.string.low_light_warning)
                Toast.makeText(this@GuideListActivity, message, Toast.LENGTH_SHORT).show()
                lowLightWarningShown = true
            }else if (lightLevel >= 150f) {
                lowLightWarningShown = false
            }


            // Логика Темы (с проверкой, чтобы избежать лишних вызовов)
            val currentMode = AppCompatDelegate.getDefaultNightMode()

            if (lightLevel < 200f) {
                if (currentMode != AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            } else {
                if (currentMode != AppCompatDelegate.MODE_NIGHT_NO) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }



    override fun onResume() {
        super.onResume()
        lightSensor?.also { sensor ->
            sensorManager.registerListener(lightSensorListener, sensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(lightSensorListener)
    }






    /*
        private fun loadGuides() {
            val langFromIntent = intent.getStringExtra("lang")
            val langToUse = langFromIntent ?: Locale.getDefault().language

            val db = dbHelper.readableDatabase
            val guides = dbHelper.getGuides(db, langToUse)
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

     */



    // Примерные данные для базы
    object ExampleData {
        data class GuideData(
            val guide: Guide,
            val titleRu: String,
            val titleEn: String,
            val stepsRu: List<String>,
            val stepsEn: List<String>,
            val imageResIds: List<Int?>
        )

        fun getExampleGuides(): List<GuideData> {
            val guides = mutableListOf<GuideData>()

            // 1. Кровь из носа
            val g1 = Guide(
                title = "Кровь из носа — что делать?",
                steps = emptyList(),
                createdAt = Calendar.getInstance().apply { set(2023, Calendar.OCTOBER, 29) }.time
            )
            guides.add(
                GuideData(
                    guide = g1,
                    titleRu = "Кровь из носа — что делать?",
                    titleEn = "Nosebleed — what to do?",
                    stepsRu = listOf(
                        "Садитесь прямо и наклонитесь слегка вперёд.",
                        "Защёлкните ноздрю и прижмите её большим и указательным пальцем.",
                        "Дышите через рот, удерживайте давление 10–15 минут."
                    ),
                    stepsEn = listOf(
                        "Sit up straight and lean slightly forward.",
                        "Pinch the nostril with your thumb and index finger.",
                        "Breathe through your mouth and maintain pressure for 10–15 minutes."
                    ),
                    imageResIds = listOf(R.drawable.guide_1, R.drawable.guide_1, R.drawable.guide_1)
                )
            )

            // 2. Ожог
            val g2 = Guide(
                title = "Ожог — первая помощь",
                steps = emptyList(),
                createdAt = Calendar.getInstance().apply { set(2023, Calendar.OCTOBER, 28) }.time
            )
            guides.add(
                GuideData(
                    guide = g2,
                    titleRu = "Ожог — первая помощь",
                    titleEn = "Burn — first aid",
                    stepsRu = listOf(
                        "Охладите ожог под струёй прохладной воды 10–20 минут.",
                        "Не используйте лед прямо на кожу.",
                        "При сильной боли обратитесь к врачу."
                    ),
                    stepsEn = listOf(
                        "Cool the burn under running water for 10–20 minutes.",
                        "Do not apply ice directly to the skin.",
                        "If severe pain occurs, see a doctor."
                    ),
                    imageResIds = listOf(R.drawable.guide_1, R.drawable.guide_1, R.drawable.guide_1)
                )
            )

            // 3. Ушиб
            val g3 = Guide(
                title = "Ушиб и растяжение",
                steps = emptyList(),
                createdAt = Calendar.getInstance().apply { set(2023, Calendar.OCTOBER, 27) }.time
            )
            guides.add(
                GuideData(
                    guide = g3,
                    titleRu = "Ушиб и растяжение",
                    titleEn = "Bruise and sprain",
                    stepsRu = listOf(
                        "Приложите холод к месту ушиба на 10–15 минут.",
                        "Приподнимите повреждённую конечность.",
                        "При сильной боли обратитесь к врачу.",
                        "При необходимости наложите повязку."
                    ),
                    stepsEn = listOf(
                        "Apply cold to the bruised area for 10–15 minutes.",
                        "Elevate the injured limb.",
                        "If severe pain occurs, see a doctor.",
                        "Apply a bandage if necessary."
                    ),
                    imageResIds = List(4) { R.drawable.guide_1 }
                )
            )

            // 4. Укус насекомого
            val g4 = Guide(
                title = "Первая помощь при укусе насекомого",
                steps = emptyList(),
                createdAt = Calendar.getInstance().apply { set(2023, Calendar.OCTOBER, 26) }.time
            )
            guides.add(
                GuideData(
                    guide = g4,
                    titleRu = "Первая помощь при укусе насекомого",
                    titleEn = "First aid for insect bites",
                    stepsRu = listOf(
                        "Очистите место укуса водой с мылом.",
                        "Прикладывайте холодный компресс.",
                        "Наблюдайте за аллергической реакцией."
                    ),
                    stepsEn = listOf(
                        "Clean the bite area with soap and water.",
                        "Apply a cold compress.",
                        "Watch for allergic reactions."
                    ),
                    imageResIds = List(3) { R.drawable.guide_1 }
                )
            )

            // 5. Ожоги химическими веществами
            val g5 = Guide(
                title = "Ожоги химическими веществами",
                steps = emptyList(),
                createdAt = Calendar.getInstance().apply { set(2023, Calendar.OCTOBER, 25) }.time
            )
            guides.add(
                GuideData(
                    guide = g5,
                    titleRu = "Ожоги химическими веществами",
                    titleEn = "Chemical burns",
                    stepsRu = listOf(
                        "Снимите загрязнённую одежду.",
                        "Промойте поражённый участок большим количеством воды.",
                        "Обратитесь к врачу.",
                        "Не трите кожу.",
                        "Наблюдайте за признаками инфекции."
                    ),
                    stepsEn = listOf(
                        "Remove contaminated clothing.",
                        "Rinse the affected area with plenty of water.",
                        "See a doctor.",
                        "Do not rub the skin.",
                        "Watch for signs of infection."
                    ),
                    imageResIds = List(5) { R.drawable.guide_1 }
                )
            )

            // 6. Обморок
            val g6 = Guide(
                title = "Обморок",
                steps = emptyList(),
                createdAt = Calendar.getInstance().apply { set(2023, Calendar.OCTOBER, 24) }.time
            )
            guides.add(
                GuideData(
                    guide = g6,
                    titleRu = "Обморок",
                    titleEn = "Fainting",
                    stepsRu = listOf(
                        "Уложите человека на спину.",
                        "Поднимите ноги выше уровня сердца.",
                        "Обеспечьте приток свежего воздуха."
                    ),
                    stepsEn = listOf(
                        "Lay the person on their back.",
                        "Raise the legs above heart level.",
                        "Ensure fresh air."
                    ),
                    imageResIds = List(3) { R.drawable.guide_1 }
                )
            )

            // 7. Кашель и удушье
            val g7 = Guide(
                title = "Кашель и удушье",
                steps = emptyList(),
                createdAt = Calendar.getInstance().apply { set(2023, Calendar.OCTOBER, 23) }.time
            )
            guides.add(
                GuideData(
                    guide = g7,
                    titleRu = "Кашель и удушье",
                    titleEn = "Cough and choking",
                    stepsRu = listOf(
                        "Попросите человека наклониться вперёд.",
                        "Если предмет застрял, сделайте приём Геймлиха.",
                        "Вызовите скорую помощь.",
                        "Следите за дыханием."
                    ),
                    stepsEn = listOf(
                        "Ask the person to lean forward.",
                        "If an object is stuck, perform the Heimlich maneuver.",
                        "Call an ambulance.",
                        "Monitor breathing."
                    ),
                    imageResIds = List(4) { R.drawable.guide_1 }
                )
            )

            // 8. Инсульт
            val g8 = Guide(
                title = "Инсульт",
                steps = emptyList(),
                createdAt = Calendar.getInstance().apply { set(2023, Calendar.OCTOBER, 22) }.time
            )
            guides.add(
                GuideData(
                    guide = g8,
                    titleRu = "Инсульт",
                    titleEn = "Stroke",
                    stepsRu = listOf(
                        "Определите признаки инсульта (лицо, речь, рука).",
                        "Вызовите скорую помощь.",
                        "Следите за дыханием и пульсом."
                    ),
                    stepsEn = listOf(
                        "Identify stroke signs (face, speech, arm).",
                        "Call an ambulance.",
                        "Monitor breathing and pulse."
                    ),
                    imageResIds = List(3) { R.drawable.guide_1 }
                )
            )

            // 9. Солнечный удар
            val g9 = Guide(
                title = "Солнечный удар",
                steps = emptyList(),
                createdAt = Calendar.getInstance().apply { set(2023, Calendar.OCTOBER, 21) }.time
            )
            guides.add(
                GuideData(
                    guide = g9,
                    titleRu = "Солнечный удар",
                    titleEn = "Heat stroke",
                    stepsRu = listOf(
                        "Перенесите человека в прохладное место.",
                        "Обеспечьте доступ свежего воздуха.",
                        "При потере сознания вызовите скорую.",
                        "Охладите голову влажным полотенцем."
                    ),
                    stepsEn = listOf(
                        "Move the person to a cool place.",
                        "Ensure fresh air.",
                        "Call an ambulance if unconscious.",
                        "Cool the head with a wet towel."
                    ),
                    imageResIds = List(4) { R.drawable.guide_1 }
                )
            )

            // 10. Порезы и раны
            val g10 = Guide(
                title = "Порезы и раны",
                steps = emptyList(),
                createdAt = Calendar.getInstance().apply { set(2023, Calendar.OCTOBER, 20) }.time
            )
            guides.add(
                GuideData(
                    guide = g10,
                    titleRu = "Порезы и раны",
                    titleEn = "Cuts and wounds",
                    stepsRu = listOf(
                        "Остановите кровотечение.",
                        "Очистите рану.",
                        "Наложите повязку.",
                        "Обратитесь к врачу при необходимости."
                    ),
                    stepsEn = listOf(
                        "Stop the bleeding.",
                        "Clean the wound.",
                        "Apply a bandage.",
                        "See a doctor if necessary."
                    ),
                    imageResIds = List(4) { R.drawable.guide_1 }
                )
            )

            return guides
        }
    }

}
