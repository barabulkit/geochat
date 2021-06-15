package xyz.barabulkit.geochat

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.postgis.Point
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup
import org.springframework.web.context.WebApplicationContext
import xyz.barabulkit.geochat.models.Message
import xyz.barabulkit.geochat.repositories.MessageRepository

@RunWith(SpringRunner::class)
@SpringBootTest
class MessageControllerTests {

    @Autowired lateinit var context: WebApplicationContext
    @Autowired lateinit var messageRepository: MessageRepository
    @Autowired lateinit var mapper: ObjectMapper
    lateinit var mockMvc: MockMvc

    @Before
    fun setup() {
        messageRepository.deleteAll()
        mockMvc = webAppContextSetup(this.context).build()
    }

    @Test
    fun `Create new message`() {
        val message = Message("""We have some dummy message""".trimMargin(), Point(0.0, 0.0))
        mockMvc.perform(post("/message")
            .content(mapper.writeValueAsString(message))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated)
    }

    @Test
    fun `Get all messages`() {
        messageRepository.insert(Message("1"))
        messageRepository.insert(Message("2", Point(0.0, 0.0)))

        mockMvc.perform(get("/message").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)

    }

    @Test fun `Subscribe to message`() {
        mockMvc.perform(get("/message/subscribe"))
            .andExpect(status().isOk())
    }
}
