package xyz.barabulkit.geochat.models

import org.postgis.Point

data class Message (
    var content: String,
    var location: Point? = null,
    var id: Int? = null
)