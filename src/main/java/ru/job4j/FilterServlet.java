package ru.job4j;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class FilterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Collection<Item> allOpenItems = PsqlTracker.instOf().findAllOpen();
        final Gson gson = new GsonBuilder().create();
        String itemsToJson = gson.toJson(allOpenItems);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("json");
        resp.getWriter().write(itemsToJson);
    }
}
