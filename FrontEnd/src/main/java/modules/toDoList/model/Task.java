package modules.toDoList.model;

import java.time.LocalDate;
import java.util.Optional;

public class Task {

    private long oid;
    private String name;
    private int effort;
    private LocalDate startDate;
    private LocalDate dateOfCompletion;

    public Task() {
    }

    public Task(long oid, String name, int effort, LocalDate startDate, LocalDate dateOfCompletion) {
        this.oid = oid;
        this.name = name;
        this.effort = effort;
        this.startDate = startDate;
        this.dateOfCompletion = dateOfCompletion;
    }

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEffort() {
        return effort;
    }

    public void setEffort(int effort) {
        this.effort = effort;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDateOfCompletion() {
        return dateOfCompletion;
    }

    public void setDateOfCompletion(LocalDate dateOfCompletion) {
        this.dateOfCompletion = dateOfCompletion;
    }
}
