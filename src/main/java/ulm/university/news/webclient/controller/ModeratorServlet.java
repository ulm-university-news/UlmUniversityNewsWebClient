package ulm.university.news.webclient.controller;

import ulm.university.news.webclient.api.ModeratorAPI;
import ulm.university.news.webclient.data.Moderator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author Matthias Mak
 */
@WebServlet("/moderator")
public class ModeratorServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get moderators via REST Server.
        List<Moderator> moderators = new ArrayList<Moderator>();

        try {
            moderators = new ModeratorAPI().getModerators();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Store moderator data in session.
        request.getSession().setAttribute("moderators", moderators);

        // Forward to result view.
        request.getRequestDispatcher("/moderators.jsp").forward(request, response);
    }
}
