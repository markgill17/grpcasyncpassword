package ie.gmit.ds;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase{

    private static final Logger logger =
            Logger.getLogger(PasswordServiceImpl.class.getName());


    @Override
    public void hash(HashRequest request, StreamObserver<HashResponse> responseObserver) {
        //Vars
        logger.info(request.toString());
        char[] password = request.getPassword().toCharArray();
        byte[] salt = Passwords.getNextSalt();
        // Stores hashed password
        byte[] hashedPassword = Passwords.hash(password, salt);

        //Creating a response
        HashResponse response = HashResponse.newBuilder().setUserId(request.getUserId())
                .setHashedPassword(ByteString.copyFrom(hashedPassword))
                .setSalt(ByteString.copyFrom(salt))
                .build();
        //Send the response back to client and completing the request
        logger.info(response.toString());
        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    @Override
    public void validate(ValidateRequest request, StreamObserver<BoolValue> responseObserver) {
        // Creating a response
        logger.info(request.toString());
        BoolValue response = BoolValue.of(Passwords.isExpectedPassword(
                request.getPassword().toCharArray(),
                request.getSalt().toByteArray(),
                request.getHashedPassword().toByteArray()));
        //Send the response back to client and completing the request
        logger.info(response.toString());
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
