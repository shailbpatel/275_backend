package sjsu.cmpe275.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import sjsu.cmpe275.entity.Employer;

import java.io.IOException;

public class ShallowEmployerSerializer extends JsonSerializer<Employer> {
    @Override
    public void serialize(Employer employer, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObjectFieldStart("employer");
        jsonGenerator.writeStringField("id", employer.getId());
        jsonGenerator.writeStringField("name", employer.getName());
        jsonGenerator.writeStringField("description", employer.getDescription());
        jsonGenerator.writeEndObject();
    }
}
