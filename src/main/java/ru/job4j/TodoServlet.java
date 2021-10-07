package ru.job4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TodoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Collection<Item> allItems = PsqlTracker.instOf().findAll();
        final Gson gson = new GsonBuilder().create();
        String itemsToJson = gson.toJson(allItems);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("json");
        resp.getWriter().write(itemsToJson);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String description = req.getParameter("description");
        PsqlTracker.instOf().save(description);
        req.getRequestDispatcher("/index.html").forward(req, resp);
    }
}
