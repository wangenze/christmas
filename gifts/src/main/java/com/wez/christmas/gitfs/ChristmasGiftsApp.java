package com.wez.christmas.gitfs;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.Random;

public class ChristmasGiftsApp extends AbstractModule {

    @Override
    protected void configure() {
        // Change random seed to replay
        bind(Random.class).toProvider(() -> new Random());
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ChristmasGiftsApp());
        Planner planner = injector.getInstance(Planner.class);
        String plan = planner.plan("Persons.json");
        System.out.println(plan);
    }
}
