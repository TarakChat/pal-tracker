package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    private long count = 0L;
    private final Map<Long,TimeEntry> timeEntryMap;

    public InMemoryTimeEntryRepository() {
        timeEntryMap = new HashMap<>();
    }


    public TimeEntry find(long id) {
        return timeEntryMap.get(id);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(timeEntryMap.values());
    }

    @Override
    public TimeEntry update(long timeEntryId, TimeEntry timeEntry) {
        if (timeEntryMap.containsKey(timeEntryId)) {
            TimeEntry updatedTimeEntry = new TimeEntry(timeEntryId,timeEntry.getProjectId(),timeEntry.getUserId(),timeEntry.getDate(),timeEntry.getHours());
            timeEntryMap.put(timeEntryId, updatedTimeEntry);
            return updatedTimeEntry;
        }
        else { return null; }
    }

    @Override
    public void delete(long timeEntryId) {
        timeEntryMap.remove(timeEntryId);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        ++count;
        TimeEntry clonedTimeEntry = new TimeEntry(count,timeEntry.getProjectId(),timeEntry.getUserId(),timeEntry.getDate(),timeEntry.getHours());
        this.timeEntryMap.put(count,clonedTimeEntry);
        return clonedTimeEntry;
    }
}
