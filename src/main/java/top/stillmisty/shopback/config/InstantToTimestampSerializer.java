package top.stillmisty.shopback.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Instant;

// 将 Instant 序列化为时间戳
public class InstantToTimestampSerializer extends JsonSerializer<Instant> {
    @Override
    public void serialize(
            Instant instant, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider
    ) throws IOException {
        if (instant != null) {
            jsonGenerator.writeNumber(instant.toEpochMilli());
        } else {
            jsonGenerator.writeNull();
        }
    }
}