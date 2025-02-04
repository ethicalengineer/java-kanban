package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    public PrioritizedHandler(TaskManager manager) {
        super(manager);
    }

    public void handle(HttpExchange h) throws IOException {
        String[] pathParts = h.getRequestURI().getPath().split("/");

        if (pathParts.length == 2 && pathParts[1].equals("prioritized")) {
            if (h.getRequestMethod().equals("GET")) {
                super.sendText(h, 200, gson.toJson(manager.getPrioritizedTasks()));
            } else {
                super.sendNotFound(h);
            }
        } else {
            super.sendNotFound(h);
        }
    }
}
