package ru.job4j.servlets;

import ru.job4j.models.User;
import ru.job4j.store.PsqlTracker;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        User user = PsqlTracker.instOf().getUser(req.getParameter("name"));
        if (user != null) {
            req.setAttribute("registration", user);
        } else {
            User userToSave = new User();
            userToSave.setName(req.getParameter("name"));
            userToSave.setPassword(req.getParameter("password"));
            PsqlTracker.instOf().saveUser(userToSave);
        }
        resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }
}
