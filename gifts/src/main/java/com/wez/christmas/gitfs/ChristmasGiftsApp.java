package com.wez.christmas.gitfs;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Random;

@AllArgsConstructor
@Getter
public class ChristmasGiftsApp extends AbstractModule {

    private final long randomSeed;

    @Override
    protected void configure() {
        // Change random seed to replay
        bind(Random.class).toProvider(() -> new Random(randomSeed));
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ChristmasGiftsApp(2020));
        Planner planner = injector.getInstance(Planner.class);
        String plan = planner.plan("Persons.json");
        System.out.println(plan);
    }
}
