package service;

import exception.ManagerLoadException;
import exception.ManagerSaveException;
import model.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    String fileName;

    public FileBackedTaskManager(String fileName) {
        this.fileName = fileName;
    }

    // Tasks methods

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void removeTaskById(long id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    // SubTasks methods

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void removeSubTaskById(long id) {
        super.removeSubTaskById(id);
        save();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        save();
    }

    // Epics methods

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeEpicById(long id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return super.getPrioritizedTasks();
    }

    // new toString() id, type, name, description, status, startTime, duration, endTime, epicId
    public void save() throws ManagerSaveException {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write("id,type,name,description,status,startTime,duration,endTime,epicId,\n");
            for (Task task : super.getAllTasks()) {
                fileWriter.write(task.toString() + "\n");
            }
            for (Epic epic : super.getAllEpics()) {
                fileWriter.write(epic.toString() + "\n");
            }
            for (SubTask subTask : super.getAllSubTasks()) {
                fileWriter.write(subTask.toString() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public static TaskManager loadFromFile(String fileName) throws ManagerLoadException {
        TaskManager fileBackedTaskManager = Managers.getDefault(fileName);
        List<Map<String, String>> inMemoryTasks = new ArrayList<>();
        boolean isFirstLine = true;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
            String[] fields = fileReader.readLine().split(",");

            while (fileReader.ready()) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] components = fileReader.readLine().split(",");
                Map<String, String> taskComponents = new HashMap<>();

                for (int i = 0; i < components.length; i++) {
                    taskComponents.put(fields[i], components[i]);
                }

                inMemoryTasks.add(taskComponents);
            }
        } catch (IOException e) {
            throw new ManagerLoadException(e.getMessage());
        }

        for (Map<String, String> task : inMemoryTasks) {
            switch (task.get("type").toLowerCase()) {
                case "task": {
                    Task createdTask = new Task(
                            task.get("name"),
                            task.get("description"),
                            Status.valueOf(task.get("status")),
                            LocalDateTime.parse(task.get("startTime"), Task.DATE_TIME_FORMATTER),
                            Duration.ofMinutes(Long.parseLong(task.get("duration"))));
                    createdTask.setId(Long.parseLong(task.get("id")));
                    fileBackedTaskManager.addTask(createdTask);
                    break;
                }
                case "epic": {
                    Epic createdEpic = new Epic(
                            task.get("name"),
                            task.get("description"),
                            LocalDateTime.parse(task.get("startTime"), Task.DATE_TIME_FORMATTER));
                    createdEpic.setId(Long.parseLong(task.get("id")));
                    fileBackedTaskManager.addEpic(createdEpic);
                    break;
                }
                case "subtask": {
                    break;
                }
            }
        }

        for (Map<String, String> subTask : inMemoryTasks) {
            if (subTask.get("type").equalsIgnoreCase("subtask")) {
                SubTask createdSubTask = new SubTask(
                        subTask.get("name"),
                        subTask.get("description"),
                        Long.parseLong(subTask.get("epicId")),
                        Status.valueOf(subTask.get("status")),
                        LocalDateTime.parse(subTask.get("startTime"), Task.DATE_TIME_FORMATTER),
                        Duration.ofMinutes(Long.parseLong(subTask.get("duration"))));
                createdSubTask.setId(Long.parseLong(subTask.get("id")));
                fileBackedTaskManager.addSubTask(createdSubTask);
            }
        }

        return fileBackedTaskManager;
    }
}
