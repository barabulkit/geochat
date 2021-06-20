package xyz.barabulkit.geochat.repositories

import org.jetbrains.exposed.sql.*
import org.postgis.PGbox2d
import org.postgis.Point
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import xyz.barabulkit.geochat.database.Messages
import xyz.barabulkit.geochat.database.within
import xyz.barabulkit.geochat.models.Message
import xyz.barabulkit.geochat.utils.getMessage
import xyz.barabulkit.geochat.utils.insertQuery

@Repository
@Transactional
class MessageRepositoryImpl : MessageRepository{
    override fun createTable() = SchemaUtils.create(Messages)

    override fun insert(t: Message): Message {
        t.id = Messages.insert(insertQuery(t))[Messages.id]
        return t
    }

    override fun findAll(): Iterable<Message> =
        Messages.selectAll().map { it.getMessage() }

    override fun deleteAll(): Int = Messages.deleteAll()

    override fun findByBoundingBox(box: PGbox2d): Iterable<Message> = Messages.select {
        Messages.location within box
    }.map { it.getMessage() }

    override fun updateLocation(id: Int, location: Point) {
        location.srid = 4326
        Messages.update({Messages.id eq id}) { it[Messages.location] = location }
    }
}