package org.example;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.MonthDay;
import java.util.List;
import java.util.Collections;


public class Today {
    public static void main(String[] args) {
        // Replace with a valid path to the events.csv file on your own computer!
        String homeDir = System.getProperty("user.home");
        Path filePath = Paths.get(homeDir, ".today", "events.csv");

        //check if path exists
        if (!filePath.toFile().exists()) {
            System.err.println("Tiedostoa ei löydy: " + filePath);
            System.exit(1);
        }

        EventProvider provider = new CSVEventProvider(filePath.toString());

        final MonthDay monthDay = MonthDay.of(2, 10);

        // Get events for given day, any year, any category, newest first
        List<Event> events = provider.getEventsOfDate(monthDay);
        Collections.sort(events);
        Collections.reverse(events);
        
        for (Event event : events) {
            System.out.println(event);
        }
    }
}