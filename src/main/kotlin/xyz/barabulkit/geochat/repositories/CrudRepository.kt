package xyz.barabulkit.geochat.repositories

import org.postgis.PGbox2d
import org.postgis.Point

interface CrudRepository<T, K> {
    fun createTable()
    fun insert(t: T): T
    fun findAll(): Iterable<T>
    fun deleteAll(): Int
    fun findByBoundingBox(box: PGbox2d): Iterable<T>
    fun updateLocation(id: K, location: Point)
}