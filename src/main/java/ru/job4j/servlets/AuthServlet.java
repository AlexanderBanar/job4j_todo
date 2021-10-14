package ru.job4j.servlets;

import ru.job4j.models.User;
import ru.job4j.store.PsqlTracker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        User userToSearchByName = PsqlTracker.instOf().getUser(name);
        if (userToSearchByName != null) {
            if (userToSearchByName.getPassword().equals(password)) {
                HttpSession sc = req.getSession();
                User user = new User();
                user.setName(name);
                user.setPassword(password);
                sc.setAttribute("user", user);
                resp.sendRedirect(req.getContextPath() + "/tasks.jsp");
            } else {
                req.setAttribute("error", "wrong name or password");
                req.getRequestDispatcher("/index.jsp").forward(req, resp);
            }
        } else {
            req.setAttribute("error", "user is not registered");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }
}
