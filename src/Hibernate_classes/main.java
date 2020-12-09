package Hibernate_classes;

import java.util.List;
import java.util.Objects;

public class main {
    public static void main(String[] args){
//        /** isExistUsernam Tests**/
//        startTest("isExistUsername");
//        assertTrue(Assignment.isExistUsername("oransh"));
//        assertFalse(Assignment.isExistUsername("gal54321"));
//
        /** isExistUsername and insertUser Tests**/
        startTest("insertUser");
        if (!Assignment.isExistUsername("cheng")) {
            String userid = Assignment.insertUser("cheng", "Aa56", "Chen", "Galed",
                    "2", "05", "1994");
            Users u1 = Assignment.getUser(userid);

            assertTrue(u1 != null);
        }

                /** getUsers Tests**/
        startTest("getUsers");
        List<Users> all = Assignment.getUsers();
        System.out.println(all);

        /** isExistUsernam Tests**/
        startTest("isExistUsername");
        assertTrue(Assignment.isExistUsername("oransh"));
        assertFalse(Assignment.isExistUsername("gal54321"));

        /** isExistUsernam and insertUser Tests**/
        startTest("insertUser");
        if (!Assignment.isExistUsername("gal54321")) {
            String userid = Assignment.insertUser("gal54321", "Aa123456", "gal", "r",
                    "01", "11", "1993");
            Users u1 = Assignment.getUser(userid);

            assertTrue(u1 != null);
        }
//
        /** getNumberOfRegisteredUsers Tests **/
        startTest("getNumberOfResgitredUsers");
        System.out.println("Number of users registered in the past 4 days: " + Assignment.getNumberOfRegistredUsers(4));
        System.out.println("Number of users registered in the past 2 days: " + Assignment.getNumberOfRegistredUsers(1));

        /** getHistory Tests **/
        startTest("getHistory");
        assertEquals(2, Assignment.getHistory("32").size());
        assertEquals(2, Assignment.getHistory("1").size());
        assertEquals(0, Assignment.getHistory("1853").size());
//
        /** getTopNItems Tests **/
        startTest("getTopNItems");
        int num_of_items = 3;
        List<Mediaitems> ret = Objects.requireNonNull(Assignment.getTopNItems(num_of_items));
        assertEquals(num_of_items, ret.size());
        System.out.println(ret);

        /** validateUser Tests **/
        startTest("validateUser");
        String usernameTestValid = "oransh";
        String passwordTestValid = "Aa123456";
        assertEquals("0", Assignment.validateUser(usernameTestValid, passwordTestValid));

        passwordTestValid = "asdf";
        assertEquals("Not Found", Assignment.validateUser(usernameTestValid, passwordTestValid));


        usernameTestValid = "gal41231";
        assertEquals("Not Found", Assignment.validateUser(usernameTestValid, passwordTestValid));


        /** validateAdministrator Tests **/
        startTest("validateAdministrator");
        String usernameAdminTestValid = "oransh";
        String passwordAdminTestValid = "123456";
        assertEquals("1", Assignment.validateAdministrator(usernameAdminTestValid, passwordAdminTestValid));

        passwordAdminTestValid = "asdf";
        assertEquals("Not Found", Assignment.validateAdministrator(usernameAdminTestValid, passwordAdminTestValid));


        usernameAdminTestValid = "gal41231";
        assertEquals("Not Found", Assignment.validateAdministrator(usernameAdminTestValid, passwordAdminTestValid));

        /** insertToHistory Tests **/
        startTest("insertToHistory");
        Assignment.insertToHistory("1", "3"); // should print "The insertion to history table was successful <server time>"
        Assignment.insertToHistory("2", "5"); // should not print
        Assignment.insertToHistory("1", "6"); // should print "The insertion to history table was successful <server time>"
    }

    private static void startTest(String testName) {
        System.out.println("########################################");
        System.out.println("######     " + testName + "      #######");
        System.out.println("########################################");
    }

    private static void assertNull(Object u) {
        System.out.println("assert nullll result!!!!!!!!!!!!!!!!!");
        System.out.println(u == null);
        System.out.println("#####################################################################3");
    }

    private static void assertFalse(boolean result) {
        System.out.println("assert false result!!!!!!!!!!!!!!");
        System.out.println(!result);
        System.out.println("#####################################################################3");
    }

    private static void assertTrue(boolean result) {
        System.out.println("assert true result!!!!!!!!!!!!!!!!!");
        System.out.println(result);
        System.out.println("#####################################################################3");

    }

    private static void assertEquals(int expected, int result) {
        System.out.println("assert equals numbers result!!!!!!!!!!!!!!!!!################################33");
        System.out.println(expected == result);
        System.out.println("#####################################################################3");
    }

    private static void assertEquals(String expected, String result) {
        System.out.println("assert equals strings result!!!!!!!!!!!!!!!!!#################################");
        System.out.println(expected.equals(result));
        System.out.println("#####################################################################3");
    }
}
