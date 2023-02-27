package space.yoko.service.desk.controller.exception

import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.LocalDateTime
import kotlin.Throws
import java.io.IOException
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.format.DateTimeFormatter

class ApiErrorLocalDateTimeSerializer : StdSerializer<LocalDateTime>(LocalDateTime::class.java) {
    @Throws(IOException::class)
    override fun serialize(value: LocalDateTime, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeString(value.format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")))
    }
}