package com.wez.christmas.gitfs;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import one.util.streamex.EntryStream;

import java.util.Map;

@AllArgsConstructor(onConstructor_ = @Inject)
class PlanPublisher {

    private final RSACipher rsaCipher;

    public Map<String, String> publish(Map<Person, Person> plan) {
        return EntryStream.of(plan)
                .mapToValue(this::buildMessage)
                .mapKeys(Person::getName)
                .toMap();
    }

    public String buildMessage(Person from, Person to) {
        String rawText = String.format("%s please prepare a gift for %s", from.getName(), to.getName());
        String publicKey = from.getPublicKey();
        return rsaCipher.encrypt(rawText, publicKey);
    }
}
