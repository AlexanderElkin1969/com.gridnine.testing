package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public enum Filter {
    ALL, DEPARTURE, ARRIVAL, TRAVEL_TIME, COUNT_OF_TRANSFER, WAITING_TIME, EXIT;

    static Predicate<Flight> isDeparture(LocalDate date) {
        return flight -> flight.getDeparture().isEqual(date);
    }

    static Predicate<Flight> isArrival(LocalDate date) {
        return flight -> flight.getArrival().isEqual(date);
    }

    static Predicate<Flight> isTravelTimeLessThan(Long hours) {
        return flight -> flight.getTravelTime().minus(Duration.ofHours(hours)).isNegative();
    }

    static Predicate<Flight> isCountOfTransferLessOrEqual(Integer count) {
        return flight -> flight.getCountOfTransfer() <= count;
    }

    static Predicate<Flight> isWaitingTimeForTransferLessThan(Long hours) {
        return flight -> flight.getWaitingTimeForTransfer().minus(Duration.ofHours(hours)).isNegative();
    }

    static List<Flight> filterFlights (List<Flight> flightList,
                                         Predicate<Flight> predicate)
    {
        return flightList.stream()
                .filter( predicate )
                .collect(Collectors.toList());
    }

}
