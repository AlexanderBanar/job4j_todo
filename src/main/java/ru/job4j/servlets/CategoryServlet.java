package ru.job4j.servlets;

import ru.job4j.models.Category;
import ru.job4j.store.PsqlTracker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class CategoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category> categories = PsqlTracker.instOf().getAllCategories();
        req.setAttribute("allCategories", categories);
    }
}
