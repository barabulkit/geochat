package xyz.barabulkit.geochat.utils

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.postgis.Point
import xyz.barabulkit.geochat.database.Messages
import xyz.barabulkit.geochat.models.Message

fun insertQuery(msg: Message): Messages.(UpdateBuilder<*>) -> Unit = {
    if(msg.id != null) it[id] = msg.id
    it[content] = msg.content
    it[location] = msg.location
}

fun ResultRow.getMessage() = Message(this[Messages.content], this[Messages.location], this[Messages.id])

fun removeZ(point : Point?): Point {
    val x = point?.getX() ?: 0.0
    val y = point?.getY() ?: 0.0
    return Point(x,y)
}