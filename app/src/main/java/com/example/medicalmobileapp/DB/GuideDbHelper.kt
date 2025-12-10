package com.example.medicalmobileapp.DB


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*
import com.example.medicalmobileapp.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class GuideDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "guides.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE guides (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title_ru TEXT,
                title_en TEXT,
                created_at INTEGER
            )
            """
        )

        db.execSQL(
            """
            CREATE TABLE steps (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                guide_id INTEGER,
                text_ru TEXT,
                text_en TEXT,
                image_res_id INTEGER,
                FOREIGN KEY(guide_id) REFERENCES guides(id)
            )
            """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS steps")
        db.execSQL("DROP TABLE IF EXISTS guides")
        onCreate(db)
    }

    fun insertGuide(db: SQLiteDatabase, guide: Guide, titleRu: String, titleEn: String, stepsRu: List<String>, stepsEn: List<String>, imageResIds: List<Int?>) {
        val guideValues = ContentValues().apply {
            put("title_ru", titleRu)
            put("title_en", titleEn)
            put("created_at", guide.createdAt.time)
        }
        val guideId = db.insert("guides", null, guideValues)

        stepsRu.forEachIndexed { index, ruStep ->
            val stepValues = ContentValues().apply {
                put("guide_id", guideId)
                put("text_ru", ruStep)
                put("text_en", stepsEn[index])
                put("image_res_id", imageResIds.getOrNull(index))
            }
            db.insert("steps", null, stepValues)
        }
    }



    suspend fun getGuidesAsync(db: SQLiteDatabase, lang: String): List<Guide> {
        return withContext(Dispatchers.IO) {
            getGuides(db, lang)
        }
    }


    fun getGuides(db: SQLiteDatabase, lang: String): List<Guide> {
        val titleColumn = if (lang == "ru") "title_ru" else "title_en"
        val textColumn = if (lang == "ru") "text_ru" else "text_en"

        val guidesList = mutableListOf<Guide>()

        val cursor = db.query("guides", arrayOf("id", titleColumn, "created_at"), null, null, null, null, null)
        while (cursor.moveToNext()) {
            val guideId = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(titleColumn))
            val createdAt = Date(cursor.getLong(cursor.getColumnIndexOrThrow("created_at")))

            val stepsCursor = db.query(
                "steps",
                arrayOf("text_ru", "text_en", "image_res_id"),
                "guide_id=?",
                arrayOf(guideId.toString()),
                null, null, null
            )

            val steps = mutableListOf<GuideStep>()
            while (stepsCursor.moveToNext()) {
                val text = stepsCursor.getString(stepsCursor.getColumnIndexOrThrow(textColumn))
                val imageResId = stepsCursor.getInt(stepsCursor.getColumnIndexOrThrow("image_res_id"))
                steps.add(GuideStep(text, imageResId))
            }
            stepsCursor.close()
            guidesList.add(Guide(title, steps, createdAt))
        }
        cursor.close()
        return guidesList
    }
}
