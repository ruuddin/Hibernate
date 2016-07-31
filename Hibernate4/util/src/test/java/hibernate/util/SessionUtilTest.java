package hibernate.util;

import org.hibernate.Session;
import org.testng.annotations.Test;

public class SessionUtilTest {

    @Test
    public void testSessionFactory(){
        Session session = SessionUtil.getSession();
        session.close();
    }
}
