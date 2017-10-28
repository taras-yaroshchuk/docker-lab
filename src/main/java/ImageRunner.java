import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;

public class ImageRunner {

    public static void main(String[] args) throws Exception {


        DefaultDockerClient docker = DefaultDockerClient.fromEnv().build();

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

        LogStream logs = docker.logs(id, DockerClient.LogsParam.stdout(), DockerClient.LogsParam.stderr(), DockerClient.LogsParam.follow());


        logs.attach(System.out, System.out);
//        docker.stopContainer(id, 15);
    }
}
