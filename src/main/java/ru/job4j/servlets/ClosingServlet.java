package ru.job4j.servlets;

import ru.job4j.models.User;
import ru.job4j.store.PsqlTracker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ClosingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        int idToClose = Integer.parseInt(req.getParameter("closedTask"));
        HttpSession sc = req.getSession();
        User user = (User) sc.getAttribute("user");
        PsqlTracker.instOf().closeTask(user, idToClose);
        req.getRequestDispatcher("/tasks.jsp").forward(req, resp);
    }
}
