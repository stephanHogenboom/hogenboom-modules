package util;

import modules.toDoList.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ExcelService {

    public void toExcell(List<Task> tasks, String outputFile) {
        List<String> lines = tasks.stream().map(this::taskToLine).collect(Collectors.toList());
        try {
            Files.write(Paths.get(outputFile), lines);
        } catch (IOException e) {
            System.out.printf("writing to excell file went wrong: %s \n", e);
        }
    }
    private String taskToLine(Task task){
        return String.format("%s;%s;%s;%s;%s",task.getOid(),task.getName(),task.getEffort(),task.getStartDate(),task.getDateOfCompletion());
    }
}
