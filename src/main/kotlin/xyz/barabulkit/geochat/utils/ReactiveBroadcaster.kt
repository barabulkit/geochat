package xyz.barabulkit.geochat.utils

import org.springframework.http.MediaType
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.io.IOException
import java.util.Collections.synchronizedSet

class ReactiveBroadcaster {
    private var emitters = synchronizedSet(HashSet<SseEmitter>())

    fun subscribe(): SseEmitter {
        val sseEmitter = SseEmitter()
        sseEmitter.onCompletion( { this.emitters.remove(sseEmitter) } )
        this.emitters.add(sseEmitter);
        return sseEmitter;
    }

    fun send(o: Any) {
        synchronized(emitters) {
            emitters.iterator().forEach {
                try {
                    it.send(o, MediaType.APPLICATION_JSON)
                } catch (e: IOException) {}
            }
        }
    }
}