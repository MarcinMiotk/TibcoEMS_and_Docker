package com.snmill.tibco;

/**
 *
 */
public class TibcoContainerRule extends DockerContainerRule {

    public TibcoContainerRule() {
        super("mami/tibco", new String[]{"7222:8222"}, "192.168.99.100", 8222);
    }

}
