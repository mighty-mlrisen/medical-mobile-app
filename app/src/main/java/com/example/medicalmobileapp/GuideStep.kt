package com.example.medicalmobileapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GuideStep(
    val text: String,
    val imageResId: Int? = null // null, если картинки нет
) : Parcelable