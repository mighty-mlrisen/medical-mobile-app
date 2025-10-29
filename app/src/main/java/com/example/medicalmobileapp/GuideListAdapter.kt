package com.example.medicalmobileapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class GuideListAdapter(
    private val context: Context,
    private val guides: List<Guide>
) : BaseAdapter() {

    override fun getCount() = guides.size
    override fun getItem(position: Int) = guides[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_guide, parent, false)

        val tvTitle = view.findViewById<TextView>(R.id.tvGuideTitle)
        val btnInfo = view.findViewById<Button>(R.id.btnInfo)

        val guide = guides[position]
        tvTitle.text = guide.title

        // --- Кнопка для Toast ---
        btnInfo.setOnClickListener {
            Toast.makeText(context, "Количество шагов: ${guide.steps.size}", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
