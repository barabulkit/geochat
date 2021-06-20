package xyz.barabulkit.geochat.database

import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.ExpressionWithColumnType
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.QueryBuilder
import org.postgis.PGbox2d

class WithinOp(private val expr1: Expression<*>, private val box: PGbox2d): Op<Boolean>() {
    override fun toSQL(queryBuilder: QueryBuilder): String =
        "${expr1.toSQL(queryBuilder)} && ST_MakeEnvelope(${box.llb.x}, ${box.llb.y}, ${box.urt.x}, ${box.urt.y}, 4326)"


}
infix fun ExpressionWithColumnType<*>.within(box: PGbox2d): Op<Boolean> = WithinOp(this,box)