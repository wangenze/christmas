package com.wez.christmas.gitfs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;
import javafx.util.Pair;
import lombok.Cleanup;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PersonTest {

    private RSACipher rsaCipher = new RSACipher();
    private ObjectMapper objectMapper = new ObjectMapper()
            .configure(SerializationFeature.INDENT_OUTPUT, true);
    @Test
    public void testSerialization() throws Exception {
        List<Person> people = Lists.newArrayList();
        people.add(generatePerson("小王"));
        people.add(generatePerson("珂珂"));
        people.add(generatePerson("小黄"));
        people.add(generatePerson("张瑜"));
        people.add(generatePerson("小曹"));
        people.add(generatePerson("小云"));

        StringWriter stringWriter = new StringWriter();
        objectMapper.writeValue(stringWriter, people);
        System.out.println(stringWriter.toString());
    }

    @Test
    public void testDeserialization() throws Exception {
        String resourceFile = getResourceFile("TestPersons.json");
        @Cleanup
        FileInputStream fileInputStream = new FileInputStream(resourceFile);
        List<Person> people = Arrays.asList(objectMapper.readValue(fileInputStream, Person[].class));
        System.out.println(people);
    }

    private Person generatePerson(String name) {
        Pair<String, String> keyPair = rsaCipher.generateKeyPair();
        return Person.builder().name(name)
                .publicKey(keyPair.getKey())
                .privateKey(keyPair.getValue())
                .build();
    }

    private String getResourceFile(String filePath) {
        URL resource = getClass().getClassLoader().getResource(filePath);
        return Optional.ofNullable(resource).map(URL::getFile).orElse(null);
    }
}