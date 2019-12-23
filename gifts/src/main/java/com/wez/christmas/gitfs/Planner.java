package com.wez.christmas.gitfs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@AllArgsConstructor(onConstructor_ = @Inject)
public class Planner {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(SerializationFeature.INDENT_OUTPUT, true);

    private final ExchangePlanGenerator exchangePlanGenerator;
    private final PlanPublisher planPublisher;

    @SneakyThrows
    public String plan(String filePath) {
        @Cleanup
        FileInputStream fileInputStream = new FileInputStream(getFile(filePath));
        List<Person> people = Arrays.asList(objectMapper.readValue(fileInputStream, Person[].class));

        Map<Person, Person> plan = exchangePlanGenerator.generatePlan(people);
        Map<String, String> messages = planPublisher.publish(plan);

        StringWriter stringWriter = new StringWriter();
        objectMapper.writeValue(stringWriter, messages);
        return stringWriter.toString();
    }

    private File getFile(String filePath) {
        return ObjectUtils.firstNonNull(getLocalFile(filePath), getResourceFile(filePath));
    }

    private File getLocalFile(String filePath) {
        Path path = Paths.get(filePath);
        File file = path.toFile();
        return file.exists() ? file : null;
    }

    private File getResourceFile(String filePath) {
        URL resource = Validate.notNull(getClass().getClassLoader().getResource(filePath));
        return new File(resource.getFile());
    }
}
