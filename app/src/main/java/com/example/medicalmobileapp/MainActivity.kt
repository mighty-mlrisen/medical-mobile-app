package com.example.medicalmobileapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.medicalmobileapp.ui.theme.MedicalMobileAppTheme
import android.content.Intent
import android.widget.Button
import android.view.Menu
import android.view.MenuItem
import java.util.Locale
import androidx.appcompat.app.AppCompatActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //supportActionBar?.setDisplayShowTitleEnabled(false)


        val btnOpen = findViewById<Button>(R.id.btnOpenGuides)
        btnOpen.setOnClickListener {
            startActivity(Intent(this, GuideListActivity::class.java))
        }
    }

/*
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

    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("lang", lang)
        startActivity(intent)
        finish() // закрываем текущую активити
    }

 */

    /*
    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)


        val config = resources.configuration
        config.setLocale(locale)

        resources.updateConfiguration(config, resources.displayMetrics)

        recreate()

    }

     */

}
