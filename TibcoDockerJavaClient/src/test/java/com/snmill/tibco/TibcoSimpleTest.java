package com.snmill.tibco;

import com.tibco.tibjms.admin.QueueInfo;
import com.tibco.tibjms.admin.TibjmsAdmin;
import com.tibco.tibjms.admin.TibjmsAdminException;
import org.junit.Rule;
import org.junit.Test;

/**
 *
 */
public class TibcoSimpleTest {

    @Rule
    public TibcoContainerRule rule = new TibcoContainerRule();

    private static final String TIBCO_URL = "tcp://192.168.99.100:8222";

    @Test
    public void checkInfo() throws TibjmsAdminException {
        TibjmsAdmin admin = new TibjmsAdmin(TIBCO_URL, "admin", "");
        QueueInfo info = admin.createQueue(new QueueInfo("mami"));

        System.out.println("INFO: " + admin.getInfo());

        admin.close();
    }

}
