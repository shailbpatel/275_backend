package sjsu.cmpe275.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

public class FullEmployeeSerializer extends JsonSerializer<Employee> {

    @Override
    public void serialize(Employee employee, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
            writeShallowEmployee(jsonGenerator, employee);

            jsonGenerator.writeObjectFieldStart("address");
                jsonGenerator.writeStringField("street", employee.getAddress().getStreet());
                jsonGenerator.writeStringField("city", employee.getAddress().getCity());
                jsonGenerator.writeStringField("state", employee.getAddress().getState());
                jsonGenerator.writeStringField("zip", employee.getAddress().getZip());
            jsonGenerator.writeEndObject();

            if (employee.getEmployer() != null) {
                ShallowEmployerSerializer shallowEmployerSerializer = new ShallowEmployerSerializer();
                shallowEmployerSerializer.serialize(employee.getEmployer(), jsonGenerator, serializerProvider);
            } else {
                jsonGenerator.writeNullField("employer");
            }

            writeShallowEmployee(jsonGenerator, "manager", employee.getManager());

            jsonGenerator.writeArrayFieldStart("reports");
            List<Employee> reports = employee.getReports();
            ShallowEmployeeListSerializer shallowEmployeeListSerializer = new ShallowEmployeeListSerializer();
            shallowEmployeeListSerializer.serialize(reports, jsonGenerator, serializerProvider);
            jsonGenerator.writeEndArray();

            jsonGenerator.writeArrayFieldStart("collaborators");
            List<Employee> collaborators = employee.getCollaborators();
            shallowEmployeeListSerializer.serialize(collaborators, jsonGenerator, serializerProvider);
            jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
    }

    private void writeShallowEmployee(JsonGenerator jsonGenerator, Employee employee) throws IOException {
        if(employee == null) {
            return;
        }
        jsonGenerator.writeNumberField("id", employee.getId());
        jsonGenerator.writeStringField("employerId", employee.getEmployerId());
        jsonGenerator.writeStringField("name", employee.getName());
        jsonGenerator.writeStringField("email", employee.getEmail());
        jsonGenerator.writeStringField("title", employee.getTitle());
    }

    private void writeShallowEmployee(JsonGenerator jsonGenerator, String fieldName, Employee employee) throws IOException {
        jsonGenerator.writeObjectFieldStart(fieldName);
        writeShallowEmployee(jsonGenerator, employee);
        jsonGenerator.writeEndObject();
    }
}
