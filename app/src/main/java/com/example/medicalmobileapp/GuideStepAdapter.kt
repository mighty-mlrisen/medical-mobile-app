package com.example.medicalmobileapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GuideStepAdapter(private val steps: List<GuideStep>) :
    RecyclerView.Adapter<GuideStepAdapter.StepViewHolder>() {

    class StepViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvStep: TextView = view.findViewById(R.id.tvStep)
        val ivStep: ImageView = view.findViewById(R.id.ivStep)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_guide_step, parent, false)
        return StepViewHolder(view)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        val step = steps[position]
        holder.tvStep.text = "${position + 1}. ${step.text}"
        if (step.imageResId != null) {
            holder.ivStep.setImageResource(step.imageResId)
        } else {
            holder.ivStep.setBackgroundColor(android.graphics.Color.LTGRAY)
        }
    }

    override fun getItemCount() = steps.size
}
