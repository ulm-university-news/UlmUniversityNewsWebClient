package ulm.university.news.webclient.util;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This filter instance is used to deactivate caching in browsers for requests that
 * are sent to our weblient application by setting header fields.
 *
 * @author Matthias Mak
 * @author Philipp Speidel
 */
@WebFilter("/webclient/*")
public class NoCacheFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0); // Proxies.

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {

    }
}
