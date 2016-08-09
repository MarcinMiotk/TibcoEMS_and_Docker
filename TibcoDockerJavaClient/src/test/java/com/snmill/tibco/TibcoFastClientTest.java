package com.snmill.tibco;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DockerClientBuilder;
import static com.snmill.tibco.WaitForPort.waitForPort;
import com.tibco.tibjms.admin.QueueInfo;
import com.tibco.tibjms.admin.TibjmsAdmin;
import com.tibco.tibjms.admin.TibjmsAdminException;
import java.io.IOException;
import org.junit.Test;

/**
 *
 */
public class TibcoFastClientTest {

    private static final int TIBCO_DOCKER_PORT = 8222;
    private static final String TIBCO_DOCKER_HOST = "192.168.99.100";
    private static final String TIBCO_IMAGE = "mami/tibco";

    private DockerClient client() {
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        return dockerClient;
    }

    private CreateContainerCmd tibcoContainer(DockerClient docker) {
        Ports portBindings = new Ports();
        portBindings.bind(ExposedPort.tcp(7222), Ports.Binding.bindPort(TIBCO_DOCKER_PORT));

        return docker
                .createContainerCmd(TIBCO_IMAGE)
                .withPortBindings(portBindings);
    }

    @Test
    public void removeTibcoContainers() throws IOException {
        removeAllTibcoContainers(TIBCO_IMAGE);
    }

    private void removeAllTibcoContainers(String withImage) throws IOException {
        try (DockerClient dockerClient = client()) {
            for (Container container : dockerClient.listContainersCmd().withShowAll(true).exec()) {
                if (withImage.equalsIgnoreCase(container.getImage())) {
                    dockerClient.removeContainerCmd(container.getId()).withForce(true).exec();
                }
            }
        }
    }

    @Test
    public void canConnectToDocker() throws InterruptedException, IOException, TibjmsAdminException {
        try (DockerClient dockerClient = client()) {
            CreateContainerCmd containerCmd = tibcoContainer(dockerClient);
            CreateContainerResponse container = containerCmd.exec();
            dockerClient.startContainerCmd(container.getId()).exec();

            waitForPort(TIBCO_DOCKER_HOST, TIBCO_DOCKER_PORT, 10_000L);
            System.out.println("connecting ...");

            tibcoAdminCreatesQueue();

            Thread.sleep(3_000L);

            dockerClient.killContainerCmd(container.getId()).exec();
            dockerClient.removeContainerCmd(container.getId()).withForce(true).exec();
        }
    }

    private void tibcoAdminCreatesQueue() throws TibjmsAdminException {
        TibjmsAdmin admin = new TibjmsAdmin("tcp://" + TIBCO_DOCKER_HOST + ":" + TIBCO_DOCKER_PORT, "admin", "");
        QueueInfo info = admin.createQueue(new QueueInfo("mami"));
        admin.close();
    }

}
