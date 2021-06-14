package xyz.barabulkit.geochat

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import org.jetbrains.exposed.spring.SpringTransactionManager
import org.postgis.geojson.PostGISModule
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@SpringBootApplication
@EnableTransactionManagement
class GeochatApplication {

    @Bean
    fun objectMapper(): ObjectMapper =
        Jackson2ObjectMapperBuilder().modulesToInstall(PostGISModule())
        .serializationInclusion(JsonInclude.Include.NON_NULL).build()

    @Bean
    fun transactionManager(@Qualifier("dataSource") dataSource: DataSource) = SpringTransactionManager(dataSource)

    @Bean
    fun init(mr: MessageRepository) = CommandLineRunner {
        mr.createTable()
        mr.deleteAll()
    }
}

fun main(args: Array<String>) {
    runApplication<GeochatApplication>(*args)
}
