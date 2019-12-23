package com.wez.christmas.gitfs;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import one.util.streamex.EntryStream;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.Map;
import java.util.Random;

@AllArgsConstructor(onConstructor_ = @Inject)
class ExchangePlanGenerator {

    private final Random random;

    public Map<Person, Person> generatePlan(List<Person> people) {
        List<Person> distinctPeople = StreamEx.of(people).distinct().toList();
        List<Map<Person, Person>> validPlans = generateAllValidPlans(distinctPeople);
        int randomSelected = random.nextInt(validPlans.size());
        return validPlans.get(randomSelected);
    }

    private List<Map<Person, Person>> generateAllValidPlans(List<Person> people) {
        return StreamEx.ofPermutations(people.size())
                .map(permutation -> StreamEx.of(people)
                        .zipWith(IntStreamEx.of(permutation))
                        .mapValues(people::get)
                        .toMap())
                .filter(this::isValidPlan)
                .toList();
    }

    private boolean isValidPlan(Map<Person, Person> plan) {
        return EntryStream.of(plan).noneMatch(Person::equals);
    }
}
