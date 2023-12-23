package com.gridnine.testing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        List<Flight> testList = FlightBuilder.createFlights();

        List<Flight> conditionOneList = conditionOne(testList);
        System.out.println(conditionOneList);

        List<Flight> conditionTwoList = conditionTwo(conditionOneList);
        System.out.println(conditionTwoList);

        List<Flight> conditionThreeList = conditionThree(conditionTwoList);
        System.out.println(conditionThreeList);

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

    private static List<Flight> conditionThree(List<Flight> testList) {
        return testList.stream()
                .filter(flight -> flight.getWaitingTimeForTransfer().toHoursPart() <= 2)
                .collect(Collectors.toList());
    }

}