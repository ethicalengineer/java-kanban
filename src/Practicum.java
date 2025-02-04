import exception.ManagerLoadException;
import service.FileBackedTaskManager;
import service.HttpTaskServer;
import service.Managers;
import service.TaskManager;

import java.io.IOException;

public class Practicum {
    private static final String fileName = "backend.txt";

    public static void main(String[] args) throws IOException {
        TaskManager manager;
        try {
            manager = FileBackedTaskManager.loadFromFile(fileName);
        } catch (ManagerLoadException e) {
            manager = Managers.getDefault(fileName);
        }
        HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.start();
    }
}
