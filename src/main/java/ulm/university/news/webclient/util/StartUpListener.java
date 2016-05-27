package ulm.university.news.webclient.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulm.university.news.webclient.controller.dispatcher.RequestDispatcher;
import ulm.university.news.webclient.controller.factory.ActionFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Listens on events of the ServletContext. Is used to initialize
 * components, e.g. the ActionFactory on startup.
 *
 * Created by Philipp on 26.05.2016.
 */
@WebListener
public class StartUpListener implements ServletContextListener {

    /** An instance of the Logger class which performs logging for the StartUpListener class. */
    private static final Logger logger = LoggerFactory.getLogger(StartUpListener.class);

    /**
     * Called on startup of web application.
     *
     * @param servletContextEvent
     */
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("Starting up.");

        // Initialize ActionFactory.
        ActionFactory.initialize();

        // Initialize Dispatcher.
        RequestDispatcher.initialize();
    }

    /**
     * Called on shutdown of web application.
     *
     * @param servletContextEvent
     */
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        logger.info("Shutting down.");
    }
}
