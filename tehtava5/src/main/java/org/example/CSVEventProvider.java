package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;

public class CSVEventProvider implements EventProvider {
    private List<Event> events;

    public CSVEventProvider(String fileName) {
        this.events = new ArrayList<>();
        Path path = Paths.get(fileName);

        try (FileReader reader = new FileReader(path.toFile());
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord csvRecord : csvParser) {
                Event event = makeEvent(csvRecord);
                this.events.add(event);
            }

            System.out.printf("Read %d events from CSV file%n", this.events.size());
        } catch (IOException ioe) {
            System.err.println("File '" + fileName + "' not found or could not be read: " + ioe.getMessage());
        }
    }

    @Override
    public List<Event> getEvents() {
        return this.events;
    }

    @Override
    public List<Event> getEventsOfCategory(Category category) {
        List<Event> result = new ArrayList<>();
        for (Event event : this.events) {
            if (event.getCategory().equals(category)) {
                result.add(event);
            }
        }
        return result;
    }

    @Override
    public List<Event> getEventsOfDate(MonthDay monthDay) {
        List<Event> result = new ArrayList<>();

        for (Event event : this.events) {
            final Month eventMonth = event.getDate().getMonth();
            final int eventDay = event.getDate().getDayOfMonth();
            if (monthDay.getMonth() == eventMonth && monthDay.getDayOfMonth() == eventDay) {
                result.add(event);
            }
        }

        return result;
    }

    private Event makeEvent(CSVRecord csvRecord) {
        LocalDate date = LocalDate.parse(csvRecord.get("Date"));
        String description = csvRecord.get("Event");
        String categoryString = csvRecord.get("Category");

        String[] categoryParts = categoryString.split("/");
        String primary = categoryParts[0];
        String secondary = (categoryParts.length == 2) ? categoryParts[1] : null;

        Category category = new Category(primary, secondary);
        return new Event(date, description, category);
    }
}