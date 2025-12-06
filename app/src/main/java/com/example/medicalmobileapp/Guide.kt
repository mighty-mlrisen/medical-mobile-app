package com.example.medicalmobileapp

import java.util.Date
data class Guide(
    val title: String,
    val steps: List<GuideStep>,
    val createdAt: Date
)