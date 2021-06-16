package xyz.barabulkit.geochat.models

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.postgis.geojson.deserializers.GeometryDeserializer
import org.postgis.Point

data class Message (
    var content: String,
    @JsonDeserialize(using = GeometryDeserializer::class) var location: Point? = null,
    var id: Int? = null
)