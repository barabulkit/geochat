package xyz.barabulkit.geochat.database

import org.jetbrains.exposed.sql.*
import org.postgis.Point

object Messages : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val content = text("content")
    val location = point("location").nullable();
}

fun Table.point(name: String, srid: Int = 4326): Column<Point>
        = registerColumn(name, PointColumnType(srid))