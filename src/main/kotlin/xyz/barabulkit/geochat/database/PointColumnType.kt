package xyz.barabulkit.geochat.database

import org.jetbrains.exposed.sql.ColumnType
import org.postgis.PGgeometry
import org.postgis.Point

class PointColumnType(val srid: Int = 4326): ColumnType() {
    override fun sqlType() = "GEOMETRY(Point, $srid)"
    override fun valueFromDB(value: Any) = if (value is PGgeometry) value.geometry else value
    override fun notNullValueToDB(value: Any): Any {
        if (value is Point) {
            if (value.srid == Point.UNKNOWN_SRID) value.srid = srid
            return PGgeometry(value)
        }
        return value
    }
}