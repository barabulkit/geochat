package xyz.barabulkit.geochat.repositories

import xyz.barabulkit.geochat.models.Message

interface MessageRepository : CrudRepository<Message, Int> {
}