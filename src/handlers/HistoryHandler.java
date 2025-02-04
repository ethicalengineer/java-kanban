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
        String method = h.getRequestMethod();

        if (pathParts.length == 2 && pathParts[1].equals("history")) {
            if (method.equals("GET")) {
                readHistoryHandler(h);
            } else {
                super.sendNotFound(h);
            }
        } else {
            super.sendNotFound(h);
        }
    }

    private void readHistoryHandler(HttpExchange h) throws IOException {
        super.sendText(h, 200, gson.toJson(historyManager.getHistory()));
    }
}
