package test;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;

/**
 * TODO
 *
 * @author Matthias Mak
 */
@WebServlet("/test")
public class TestServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        response.getOutputStream().print("Hello doGet");
    }
}
