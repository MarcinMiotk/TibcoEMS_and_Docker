package com.snmill.tibco;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DockerClientBuilder;
import static com.snmill.tibco.WaitForPort.waitForPort;
import java.io.IOException;
import org.junit.rules.ExternalResource;

/**
 *
 */
public class DockerContainerRule extends ExternalResource {

    final DockerClient client;
    final CreateContainerResponse container;
    final String waitForHost;
    final int waitForPort;
    final long WAIT_TIMEOUT = 10_000L;

    public DockerContainerRule(String imageName, String[] ports, String waitForHost, int waitForPort) {
        try {
            this.waitForHost = waitForHost;
            this.waitForPort = waitForPort;
            client = DockerClientBuilder.getInstance().build();

            removeAllContainers(client, imageName);

            Ports portBindings = new Ports();
            for (String pair : ports) {
                String[] fromTo = pair.split(":");
                portBindings.bind(ExposedPort.tcp(Integer.parseInt(fromTo[0])), Ports.Binding.bindPort(Integer.parseInt(fromTo[1])));
            }
            container = client.createContainerCmd(imageName).withPortBindings(portBindings).exec();
            System.out.println("RULE CONSTRUCTOR");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        System.out.println("BEFORE");

        client.startContainerCmd(container.getId()).exec();
        waitForPort(waitForHost, waitForPort, WAIT_TIMEOUT);
    }

    @Override
    protected void after() {
        super.after();
        System.out.println("AFTER");
        try {
            client.killContainerCmd(container.getId()).exec();
            client.removeContainerCmd(container.getId()).withForce(true).exec();
            client.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void removeAllContainers(DockerClient client, String withImage) throws IOException {
        for (Container container : client.listContainersCmd().withShowAll(true).exec()) {
            if (withImage.equalsIgnoreCase(container.getImage())) {
                client.removeContainerCmd(container.getId()).withForce(true).exec();
            }
        }
    }
}
