package ru.job4j.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.models.Item;
import ru.job4j.models.User;
import ru.job4j.store.PsqlTracker;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

public class FilterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession sc = req.getSession();
        User user = (User) sc.getAttribute("user");
        Collection<Item> allOpenItems = PsqlTracker.instOf().findAllOpen(user);
        final Gson gson = new GsonBuilder().create();
        String itemsToJson = gson.toJson(allOpenItems);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("json");
        resp.getWriter().write(itemsToJson);
    }
}
