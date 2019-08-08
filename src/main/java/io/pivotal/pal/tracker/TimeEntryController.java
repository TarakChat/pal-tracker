package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private final TimeEntryRepository timeEntryRepository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry) {
        this.timeEntryRepository = timeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry timeEntryCreated = this.timeEntryRepository.create(timeEntryToCreate);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return new ResponseEntity<>(timeEntryCreated,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") long timeEntryId) {
        TimeEntry timeEntryFound = this.timeEntryRepository.find(timeEntryId);
        if (timeEntryFound==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            actionCounter.increment();
            return new ResponseEntity<>(timeEntryFound,HttpStatus.OK);
        }
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        List<TimeEntry> timeEntryList = this.timeEntryRepository.list();
        actionCounter.increment();
        return new ResponseEntity<>(timeEntryList,HttpStatus.OK);
    }

    @PutMapping("/{timeEntryId}")
    public ResponseEntity update(@PathVariable long timeEntryId, @RequestBody TimeEntry expectedTimeEntry) {
        System.out.println("timeEntryId: " + timeEntryId + ", TimeEntry: " + expectedTimeEntry);
        TimeEntry timeEntryUpdated = this.timeEntryRepository.update(timeEntryId,expectedTimeEntry);
        if (timeEntryUpdated==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            actionCounter.increment();
            return new ResponseEntity<>(timeEntryUpdated,HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") long timeEntryId) {
        this.timeEntryRepository.delete(timeEntryId);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
