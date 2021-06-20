package xyz.barabulkit.geochat.controllers

import org.postgis.PGbox2d
import org.postgis.Point
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import xyz.barabulkit.geochat.models.Message
import xyz.barabulkit.geochat.repositories.MessageRepository
import xyz.barabulkit.geochat.utils.ReactiveBroadcaster
import xyz.barabulkit.geochat.utils.removeZ

@RestController
@RequestMapping("/message")
class MessageController(val repository: MessageRepository) {
    val broadcaster = ReactiveBroadcaster()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody message: Message): Message {
        message.location = removeZ(message.location)
        val msg = repository.insert(message)
        broadcaster.send(msg)
        return msg
    }

    @GetMapping
    fun list() = repository.findAll()

    @GetMapping("/bbox/{xMin},{yMin},{xMax},{yMax}")
    fun findByBoundingBox(
        @PathVariable xMin : Double,
        @PathVariable yMin : Double,
        @PathVariable xMax : Double,
        @PathVariable yMax : Double
    ) = repository.findByBoundingBox(PGbox2d(Point(xMin,yMin), Point(xMax, yMax)))

    @GetMapping("/subscribe")
    fun subscribe() = broadcaster.subscribe()
}