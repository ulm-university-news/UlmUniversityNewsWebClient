package ulm.university.news.webclient.test;

import ulm.university.news.webclient.data.Moderator;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author Matthias Mak
 */
@WebServlet("/ulm/university/news/webclient/test")
public class TestServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        // TODO Get moderator ulm.university.news.data via REST Server.
        Moderator moderator1 = new Moderator();
        moderator1.setName("tom123");
        moderator1.setLastName("Thomson");
        moderator1.setFirstName("Tom");

        Moderator moderator2 = new Moderator();
        moderator2.setName("ben321");
        moderator2.setLastName("Benson");
        moderator2.setFirstName("Ben");

        List<Moderator> moderators = new ArrayList<Moderator>();
        moderators.add(moderator1);
        moderators.add(moderator2);

        // Store bean in session.
        request.getSession().setAttribute("moderators", moderators);

        // Forward to result view.
        request.getRequestDispatcher("/results.jsp").forward(request, response);
    }
}
