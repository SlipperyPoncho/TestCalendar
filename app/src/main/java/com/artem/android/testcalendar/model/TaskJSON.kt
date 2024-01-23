package com.artem.android.testcalendar.model

import com.google.gson.annotations.SerializedName

// Task from json is displayed on 21st January
data class TaskJSON(
    var id: String = "",
    var name: String = "",
    @SerializedName("date_start") var dateStart: String = System.currentTimeMillis().toString(),
    @SerializedName("date_finish") var dateFinish: String = (System.currentTimeMillis() + 3600000).toString(),
    var description: String = "")