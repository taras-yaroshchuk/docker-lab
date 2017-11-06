import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ExecCreation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ImageRunner {

    public static void main(String[] args) throws Exception {


        DefaultDockerClient docker = DefaultDockerClient.fromEnv().build();

        docker.pull("postgres:10-alpine");
        try {
            docker.removeContainer("my-postgres", DockerClient.RemoveContainerParam.forceKill());
        } catch (Exception e) {
        }

// Create container with exposed ports
        final ContainerConfig containerConfig = ContainerConfig.builder()
                .image("postgres:10-alpine")
                .build();


        final ContainerCreation creation = docker.createContainer(containerConfig, "my-postgres");
        final String id = creation.id();

        docker.startContainer(id);

//        Attaching logs
//        LogStream logs = docker.logs(id, DockerClient.LogsParam.stdout(), DockerClient.LogsParam.stderr(), DockerClient.LogsParam.follow());
//        logs.attach(System.out, System.out);

        Scanner sc = new Scanner(System.in);
        while (true) {

            String[] command = sc.nextLine().split(" ");


            ExecCreation execCreation = docker.execCreate(id, command,
                    DockerClient.ExecCreateParam.attachStdout(),
                    DockerClient.ExecCreateParam.attachStderr());
            LogStream output = docker.execStart(execCreation.id());
            String outputLogs = output.readFully();
            System.out.println(outputLogs);


        }//        docker.stopContainer(id, 15);
    }
}
