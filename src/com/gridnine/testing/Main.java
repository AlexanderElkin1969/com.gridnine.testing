package com.gridnine.testing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        List<Flight> testList = FlightBuilder.createFlights();

        List<Flight> conditionOneList = conditionOne(testList);
        System.out.println(conditionOneList);

        List<Flight> conditionTwoList = conditionTwo(testList);
        System.out.println(conditionTwoList);


    }

    private static List<Flight> conditionOne(List<Flight> testList) {
        return testList.stream()
                .filter(flight -> flight.getSegments()
                        .stream().findFirst().get().getDepartureDate().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    private static List<Flight> conditionTwo(List<Flight> testList) {
        return testList.stream()
                .filter(flight -> flight.getSegments()
                        .stream().allMatch(segment -> segment.getDepartureDate().isBefore(segment.getArrivalDate())))
                .collect(Collectors.toList());
    }

}