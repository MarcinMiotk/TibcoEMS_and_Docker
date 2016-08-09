package com.snmill.tibco;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

/**
 *
 */
public class WaitForPort {

    public static void waitForPort(String dockerHost, int port, long timeoutInMillis) {
        SocketAddress address = new InetSocketAddress(dockerHost, port);
        long totalWait = 0;
        while (true) {
            try {
                SocketChannel.open(address).close();

                return;
            } catch (IOException e) {
                try {
                    Thread.sleep(100);
                    totalWait += 100;
                    if (totalWait > timeoutInMillis) {
                        throw new IllegalStateException("Timeout while waiting for port " + port);
                    }
                } catch (InterruptedException ie) {
                    throw new IllegalStateException(ie);
                }
            }
        }
    }
}
