package ru.job4j.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.models.Item;
import ru.job4j.models.User;
import ru.job4j.store.PsqlTracker;

public class TodoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession sc = req.getSession();
        User user = (User) sc.getAttribute("user");
        Collection<Item> allItems = PsqlTracker.instOf().findAll(user);
        final Gson gson = new GsonBuilder().create();
        String itemsToJson = gson.toJson(allItems);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("json");
        resp.getWriter().write(itemsToJson);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String description = req.getParameter("description");
        String[] cIds = req.getParameterValues("cIds");
        HttpSession sc = req.getSession();
        User user = (User) sc.getAttribute("user");
        PsqlTracker.instOf().save(user, description, cIds);
        req.getRequestDispatcher("/tasks.jsp").forward(req, resp);
    }
}
