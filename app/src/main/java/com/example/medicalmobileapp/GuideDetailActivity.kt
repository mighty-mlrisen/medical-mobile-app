package com.example.medicalmobileapp


/*
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


 */


import android.content.Context
import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*;

class GuideDetailActivity : AppCompatActivity() {

    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private var lowLightWarningShown = false

    private var lang: String = Locale.getDefault().language

    private val lightSensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val lightLevel = event.values[0]

            // --- Логика Toast с учётом текущего языка ---
            val lowLightThreshold = 100f
            val resetThreshold = 150f
            /*
            if (lightLevel < lowLightThreshold && !lowLightWarningShown) {
                // Используем локализованный Context
                val locale = Locale(lang) // lang — текущий язык активности
                val localizedContext = this@GuideDetailActivity.createConfigurationContext(
                    resources.configuration.apply { setLocale(locale) }
                )
                val message = localizedContext.getString(R.string.low_light_warning)
                android.widget.Toast.makeText(this@GuideDetailActivity, message, android.widget.Toast.LENGTH_SHORT).show()
                lowLightWarningShown = true
            } else if (lightLevel >= resetThreshold) {
                lowLightWarningShown = false
            }

             */
            if (lightLevel < lowLightThreshold && !lowLightWarningShown) {
                // Используем локализованный Context
                val prefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
                val currentLang = prefs.getString("lang", lang) ?: lang

                // Создаём локализованный контекст
                val config = resources.configuration
                val locale = Locale(currentLang)
                val localizedConfig = Configuration(config)
                localizedConfig.setLocale(locale)
                val localizedContext = createConfigurationContext(localizedConfig)

                val message = localizedContext.getString(R.string.low_light_warning)
                Toast.makeText(this@GuideDetailActivity, message, Toast.LENGTH_SHORT).show()
                lowLightWarningShown = true
            } else if (lightLevel >= resetThreshold) {
                lowLightWarningShown = false
            }


            // --- Логика Темы (с проверкой, чтобы избежать лишних вызовов) ---
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_guide_detail)

        if (savedInstanceState != null) {
            lowLightWarningShown = savedInstanceState.getBoolean("KEY_WARNING_SHOWN", false)
        }

        // Кнопка "назад"
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        // Заголовок гайда
        val title = intent.getStringExtra("title") ?: "Гайд"
        findViewById<TextView>(R.id.tvGuideTitle).text = title

        // Список шагов
        @Suppress("DEPRECATION")
        val steps = intent.getParcelableArrayListExtra<GuideStep>("steps") ?: arrayListOf()

        val recyclerView = findViewById<RecyclerView>(R.id.stepsContainer)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = GuideStepAdapter(steps)

        lang = intent.getStringExtra("lang") ?: Locale.getDefault().language


        // Инициализация сенсора
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("KEY_WARNING_SHOWN", lowLightWarningShown)
    }
}
