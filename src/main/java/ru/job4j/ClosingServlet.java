package ru.job4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ClosingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        int idToClose = Integer.parseInt(req.getParameter("closedTask"));
        Item itemToClose = PsqlTracker.instOf().findById(idToClose);
        if (itemToClose != null) {
            itemToClose.setDone(true);
            PsqlTracker.instOf().closeTask(itemToClose);
        }
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
