package service;

import adapters.DurationTypeAdapter;
import adapters.StartTimeTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import exception.ManagerLoadException;
import handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final String fileName = "backend.txt";
    private HttpServer httpServer;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager manager) {
        this.taskManager = manager;
    }

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

    public void start() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/subtasks", new SubTaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpServer.stop(0);
    }

    public Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new StartTimeTypeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .create();
    }
}
