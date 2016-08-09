package com.snmill.tibco;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 */
public class DockerMachineTest {

    public DockerMachineTest() {
    }

    @Test
    public void canConnectToDockerMachineWithPropertiesFile() {
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        List<Image> images = dockerClient.listImagesCmd().exec();
        for (int i = 0; i < images.size(); i++) {
            System.out.println(images.get(i));
        }
    }

    @Ignore
    @Test
    public void canConnectToDockerMachineProgrammatic() {
        DefaultDockerClientConfig.Builder b = new DefaultDockerClientConfig.Builder();
        b.withDockerTlsVerify(true)
                .withDockerCertPath("C:\\Users\\mmiotk\\.docker\\machine\\machines\\default");
        b.withDockerHost("tcp://192.168.99.100:2376");

        DockerClient dockerClient = DockerClientBuilder.getInstance(b.build()).build();
        List<Image> images = dockerClient.listImagesCmd().exec();

        for (int i = 0; i < images.size(); i++) {
            System.out.println(images.get(i));
        }
    }

}
