package com.gridnine.testing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Flight> conditionOneList = conditionOne(FlightBuilder.createFlights());
        System.out.println("Condition 1 :");
        System.out.println(conditionOneList);
        List<Flight> conditionTwoList = conditionTwo(conditionOneList);
        System.out.println("condition 2 :");
        System.out.println(conditionTwoList);

        List<Flight> testList = createList();       // Создание списка всех перелётов без фильтров, первое и второе правило
        System.out.println("condition 3 :");        // Третье правило вывел отдельно, так как его можно считать фильтром
        System.out.println(conditionThree(testList));

        System.out.println("Для фильтрации и поиска нужного перелёта используйте команды :\n" +
                Arrays.toString(Filter.values()) + "\n ALL FLIGHT - снять все фильтры;" +
                "\n DEPARTURE dd.mm.yyyy - вывести перелёты с датой вылета;" +
                "\n ARRIVAL dd.mm.yyyy - вывести перелёты с датой прилёта;" +
                "\n TRAVEL_TIME 5 - вывести перелёты продолжительностью не более 5 часов;" +
                "\n COUNT_OF_TRANSFER 1 - вывести перелёты не более чем с 1 пересадкой;" +
                "\n WAITING_TIME 3 - вывести перелёты с ожиданием пересадки не более 3 часов;" +
                "\n EXIT VIEW - для выхода из просмотра.");
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            Filter filter = Filter.ALL;
            String condition = " ";
            do {
                testList = viewingFlights(testList, filter, condition);
                System.out.println(testList);
                String s = bufferedReader.readLine();
                int index = s.indexOf(" ");
                String filterStr = s.substring(0, index);
                filter = Filter.valueOf(filterStr.toUpperCase());
                condition = s.substring(index + 1);
            } while (filter != Filter.EXIT);
        } catch (IOException | ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    private static List<Flight> viewingFlights(List<Flight> testList, Filter filter, String condition) throws ParseException {
        switch (filter){
            case ALL:
                return createList();
            case DEPARTURE:
                DateTimeFormatter formatD = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate dateD = LocalDate.parse(condition, formatD);
                return Filter.filterFlights(testList, Filter.isDeparture(dateD));
            case ARRIVAL:
                DateTimeFormatter formatA = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate dateA = LocalDate.parse(condition, formatA);
                return Filter.filterFlights(testList, Filter.isArrival(dateA));
            case TRAVEL_TIME:
                Long hoursT = Long.parseLong(condition);
                return Filter.filterFlights(testList, Filter.isTravelTimeLessThan(hoursT));
            case COUNT_OF_TRANSFER:
                Integer count = Integer.parseInt(condition);
                return Filter.filterFlights(testList, Filter.isCountOfTransferLessOrEqual(count));
            case WAITING_TIME:
                Long hoursW = Long.parseLong(condition);
                return Filter.filterFlights(testList, Filter.isWaitingTimeForTransferLessThan(hoursW));
        }
        return testList;
    }

    private static List<Flight> createList() {
        return conditionTwo(conditionOne(FlightBuilder.createFlights()));
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