package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.HistoryManager;
import service.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final HistoryManager historyManager;

    public HistoryHandler(TaskManager manager) {
        super(manager);
        this.historyManager = manager.getHistory();
    }

    public void handle(HttpExchange h) throws IOException {
        String[] pathParts = h.getRequestURI().getPath().split("/");

        if (pathParts.length == 2 && pathParts[1].equals("history")) {
            if (h.getRequestMethod().equals("GET")) {
                super.sendText(h, 200, gson.toJson(historyManager.getHistory()));
            } else {
                super.sendText(h, 404, "Метод не реализован");
            }
        } else {
            super.sendText(h, 404, "Метод не реализован");
        }
    }
}
