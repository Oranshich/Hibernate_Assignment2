package Hibernate_classes;

import org.hibernate.*;
import org.hibernate.cfg.*;

import java.net.URL;


/**
 * @author tsikinov
 *  
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            // Create the SessionFactory
//            URL pathToXML = Assignment.class.getClassLoader().getResource("hibernate.cfg.xml");

            sessionFactory = new Configuration().configure().buildSessionFactory();
           
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
            
        } // end of the try - catch block
        
    } // end of static block

    public static final ThreadLocal<Session> session = new ThreadLocal<Session>();

    public static Session currentSession() throws HibernateException {
        
        Session s = (Session) session.get();
        
        // Open a new Session, if this Thread has none yet
        if (s == null) {
            s = sessionFactory.openSession();
            session.set(s);
        }
        return s;
    }

    public static void closeSession() throws HibernateException {
        Session s = (Session) session.get();
        session.set(null);
        if (s != null)
            s.close();
    } // end of the method

} // end of the class 