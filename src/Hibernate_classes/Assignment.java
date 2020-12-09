package Hibernate_classes;

import com.sun.org.apache.xerces.internal.util.XMLEntityDescriptionImpl;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class Assignment {
    private static final SessionFactory ourSessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();

            URL pathToXML = Assignment.class.getClassLoader().getResource("hibernate.cfg.xml");

            ourSessionFactory = configuration.configure(pathToXML.toString()).buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }
    /**
     *  Check if the username exist in the users table
     * @param username
     * @return true if the user exist in the user table
     */
    public static boolean isExistUsername (String username){
        try {
            Session session =HibernateUtil.currentSession();
            String queryString = "select username from Users where username='" + username + "'";
            List<Users> result = session.createQuery(queryString).list();
            if (result.size() == 1)
                return true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            HibernateUtil.closeSession();
        }
        return false;
    }


    /**
     * Insert a new user to the Users table
     * @param username
     * @param password
     * @param first_name
     * @param last_name
     * @param day_of_birth
     * @param month_of_birth
     * @param year_of_birth
     * @return the userid of the new user
     */
    public static String insertUser(String username, String password,
                                    String first_name, String last_name, String day_of_birth, String
                                            month_of_birth, String year_of_birth){
        String DATE_FORMAT = "dd-MM-yyyy";

        Users user = new Users();
        // Check if the username exist
        if(isExistUsername(username))
            return null;
        // Check the date is valid
        Session session = HibernateUtil.currentSession();
        String dateToCheck = day_of_birth + "-" + month_of_birth + "-" + year_of_birth;
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        df.setLenient(false);
        try {
            df.parse(dateToCheck);


            if(day_of_birth.length() == 1){
                day_of_birth = "0" + day_of_birth;
            }
            String date_format_for_db = "dd/mm/yyyy";
            df = new SimpleDateFormat(date_format_for_db);
            Date date = df.parse(day_of_birth + "/" + month_of_birth + "/" + year_of_birth);
            Timestamp dateTimeStamp = new Timestamp(date.getTime());
            user.setUsername(username);
            user.setFirstName(first_name);
            user.setLastName(last_name);
            user.setPassword(password);
            user.setRegistrationDate(new Timestamp(System.currentTimeMillis()));
            user.setDateOfBirth(dateTimeStamp);
            session.saveOrUpdate(user);
            Transaction transaction = session.beginTransaction();
            transaction.commit();

        } catch (ParseException e) {
            return null;
        }
        finally {
            HibernateUtil.closeSession();
        }

        return Long.toString(user.getUserid());
    }

    /**
     * Returns the top n media items in descending orders
     * @param top_n
     * @return list of media items
     */
    public static List<Mediaitems> getTopNItems (int top_n){
        List<Mediaitems> mediaItemsList = null;
        try {
            Session session = HibernateUtil.currentSession();
            String queryString = "from Mediaitems order by mid desc";
            Query queryResult = session.createQuery(queryString).setMaxResults(top_n);
            mediaItemsList = queryResult.list();
        }
        catch (Exception e){
            return null;
        }
        finally {
            HibernateUtil.closeSession();
        }

        return mediaItemsList;
    }

    /**
     * The function compares received values with existing in the data base.
     * @param username
     * @param password
     * @return USERID if the values are equal to the values in the table
     * otherwise return “Not Found”
     */
    public static String validateUser (String username, String password){
        try {
            Session session = HibernateUtil.currentSession();
            String queryString = "select userid from Users where username='" + username + "'" +" and password='" + password + "'";
            List<Long> result = session.createQuery(queryString).list();
            if (result.size() == 1){
                return Long.toString(result.get(0));
            }
        }
        catch (Exception e){
            return "Not Found";
        }
        finally {
            HibernateUtil.closeSession();
        }
        return "Not Found";
    }

    /**
     * The function compares received values with existing in the data base.
     * @param username
     * @param password
     * @return ADMINID if the values are equal to the values in the
     * table otherwise “Not Found”.
     */
    public static String validateAdministrator (String username, String password){
        try {
            Session session = HibernateUtil.currentSession();
            String queryString = "select adminid from Administrators where username='" + username + "'" +" and password='" + password + "'";
            List<Long> result = session.createQuery(queryString).list();
            if (result.size() == 1){
                return Long.toString(result.get(0));
            }
        }
        catch (Exception e){
            return "Not Found";
        }
        finally {
            HibernateUtil.closeSession();
        }
        return "Not Found";
    }

    /**
     * The function inserts the row to the History table with current server time and
     * print “The insertion to history table was successful <server time>“
     * @param userid
     * @param mid
     */
    public static void insertToHistory (String userid, String mid){
        try{
            if(userid != null && mid != null) {

                Session session = HibernateUtil.currentSession();
                String queryMid = "from Mediaitems m where m.mid=" + mid;
                List<Mediaitems> resultMid = session.createQuery(queryMid).list();

                String queryUser = "from Users where userid='" + userid + "'";
                List<Users> resultUser = session.createQuery(queryUser).list();
                if (resultUser.size() == 1 && resultMid.size() == 1) {
                    Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                    History history = new History();
                    history.setMid(Long.parseLong(mid));
                    history.setUserid(Long.parseLong(userid));
                    history.setViewtime(currentTime);
                    session.saveOrUpdate(history);
                    Transaction transaction = session.beginTransaction();
                    transaction.commit();
                    System.out.println("The insertion to history table was successful " + currentTime.toLocalDateTime().toString());
                }
            }

        } catch (Exception e) {
        }
        finally {
            HibernateUtil.closeSession();
        }
    }

    /**
     * The function retrieves from the tables History and MediaItems users's items
     * @param userid
     * @return list of pairs <title,viewtime> sorted by VIEWTIME in
     * ascending order
     */
    public static Map<String, Date> getHistory (String userid){
        Map<String, Date> result = new HashMap<>();
        try {
            Session session = HibernateUtil.currentSession();

            if (userid != null) {
                String queryString = "select m.title, h.viewtime from Mediaitems m Join History h on m.mid=h.mid" +
                        " where h.userid=" + userid + " order by h.viewtime asc";
                List<Object> objectsList = session.createQuery(queryString).list();
                for (Object object: objectsList) {
                    Object[] objectArray= (Object[])object;
                    String title = (String)objectArray[0];
                    Timestamp viewTime = (Timestamp)objectArray[1];
                    Date date = new Date(viewTime.getTime());
                    result.put(title, date);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            HibernateUtil.closeSession();
        }

        return result;
    }


    /**
     * The function insert the row to the LoginLog table with current server time
     * @param userid
     */
    public static void insertToLog (String userid) {
        try {
            Session session = HibernateUtil.currentSession();
            if (userid != null) {
                String queryString = "select username frm Users where userid = '" + userid + "'";
                List<Users> result = session.createQuery(queryString).list();
                if (result.size() == 1) {
                    Loginlog loginlog = new Loginlog();
                    Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                    loginlog.setLogintime(currentTime);
                    loginlog.setUserid(Long.parseLong(userid));
                    session.saveOrUpdate(loginlog);
                    Transaction transaction = session.beginTransaction();
                    transaction.commit();
                    System.out.println("The insertion to log table was successful " + currentTime.toLocalDateTime().toString());
                }

            }
        } catch (Exception e) {

        } finally {
            HibernateUtil.closeSession();
        }
    }


    /**
     * The function retrieves from the table Users number of registered users in
     * the past n days
     */
    public static int getNumberOfRegistredUsers(int n){
        List<Long> users = null;
        try {
            Session session = HibernateUtil.currentSession();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, n);
            LocalDateTime today =  LocalDateTime.now();
            LocalDateTime nDaysAgo = today.minusDays(n);
            String queryString = "select count(u.userid) from Users u where u.registrationDate > :date";
            Query q = session.createQuery(queryString);
            q.setTimestamp("date", Timestamp.valueOf(nDaysAgo));
            users = q.list();
            Long number = new Long(0);
            if(users.size()== 1){
                number = users.get(0);
                return number.intValue();
            }

        }
        catch (Exception e){
            return 0;
        }
        finally {
            HibernateUtil.closeSession();
        }

        return 0;
    }

    /**
     * The function retrieves from the table Users all users
     * @return list of users
     */
    public static List<Users> getUsers (){
        try {
            Session session = HibernateUtil.currentSession();
            String queryString = "select u from Users u";
            List<Users> users = session.createQuery(queryString).list();
            return users;
        }catch (Exception e){
            return null;
        }
        finally {
            HibernateUtil.closeSession();
        }
    }

    /**
     * The function retrieves from the table Users user's information
     * @param userid
     * @return object user
     */
    public static Users getUser (String userid){
        try {
            if (userid != null) {
                Session session = HibernateUtil.currentSession();
                String queryString = "select u from Users u where u.userid = '" + userid + "'";
                List<Users> users = session.createQuery(queryString).list();
                if(users.size() == 1)
                    return users.get(0);
            }
        }catch (Exception e){
            return null;
        }
        finally {
            HibernateUtil.closeSession();
        }

        return null;
    }
}
