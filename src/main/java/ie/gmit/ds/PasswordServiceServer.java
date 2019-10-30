package ie.gmit.ds;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.logging.Logger;

public class PasswordServiceServer {

    private Server grpcServer;
    // retrieving name of class
    private static final Logger logger = Logger.getLogger(PasswordServiceServer.class.getName());
    private static final int PORT = 50551;

    public static void main(String[] args) throws IOException, InterruptedException {
        final PasswordServiceServer passwordServer = new PasswordServiceServer();
        passwordServer.start();
        passwordServer.blockUntilShutdown();
    }

    //this method starts the server
    private void start() throws IOException {
        grpcServer = ServerBuilder.forPort(PORT)
                .addService(new PasswordServiceImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + PORT);

    }

    //shuts down server
    private void stop() {
        if (grpcServer != null) {
            grpcServer.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (grpcServer != null) {
            grpcServer.awaitTermination();
        }
    }

}
