package sjsu.cmpe275.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import sjsu.cmpe275.entity.Employee;

import java.io.IOException;

public class ShallowEmployeeSerializer extends JsonSerializer<Employee> {

    @Override
    public void serialize(Employee employee, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", employee.getId());
        jsonGenerator.writeStringField("employerId", String.valueOf(employee.getEmployerId()));
        jsonGenerator.writeStringField("name", employee.getName());
        jsonGenerator.writeStringField("email", employee.getEmail());
        jsonGenerator.writeStringField("title", employee.getTitle());
        jsonGenerator.writeEndObject();
    }
}
