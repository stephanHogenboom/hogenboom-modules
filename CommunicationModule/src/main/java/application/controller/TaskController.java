package application.controller;

import application.model.Task;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class TaskController {

    private static String template = "Task %s";
    private final AtomicInteger counter = new AtomicInteger();

    @RequestMapping("/task")
    public Task task(@RequestParam(value="name", defaultValue="easy task") String name) {
        return new Task(counter.incrementAndGet(),
                String.format(template, name));
    }

}
