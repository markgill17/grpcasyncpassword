package ie.gmit.ds;


import org.xml.sax.SAXException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Set;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class UserRESTController {

    private final Validator validator;

    public UserRESTController(Validator validator) {
        this.validator = validator;
    }

    @GET
    @Path("/users")
    public Response getUsers(){
        return Response.ok(UserDB.getUsers()).build();
    }

    @GET
    @Path("users/{id}")
    public Response getUserById(@PathParam("id") Integer id){
        User user = UserDB.getUser(id);
        if(user!=null)
            return Response.ok(user).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();

    }

    @POST
    @Path("/users")
    public Response createUser(User user) throws URISyntaxException {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        User u = UserDB.getUser(user.getId());
        System.out.println(user);

        if (u == null) {
            if (violations.size()>0){
                ArrayList<String> validationMessages = new ArrayList<String>();
                for(ConstraintViolation<User> violation : violations){
                    validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
                }
                return Response.status(Response.Status.BAD_REQUEST).entity(validationMessages).build();
            } else {
                UserDB.updateUser(user.getId(), new User(user.getId(),user.getName(),user.getEmail(), user.getPassword()));
                return Response.created(new URI("/users/"+user.getId())).build();
            }
        } else
            return Response.status(Response.Status.CONFLICT).build();



    }

    @PUT
    @Path("users/{id}")
    public Response updateUserById(@PathParam("id") Integer id, User user) throws IOException, SAXException {
        // validation
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        User u = UserDB.getUser(user.getId());
        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<String>();
            for (ConstraintViolation<User> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(validationMessages).build();
        }
        if (u != null) {
            user.setId(id);
            //UserDB.updateUser(id, user);
            UserDB.updateUser(user.getId(), new User(user.getId(),user.getName(),user.getEmail(), user.getPassword()));
            return Response.ok(user).build();
        } else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("users/{id}")
    public Response removeUserById(@PathParam("id") Integer id) {
        User user = UserDB.getUser(id);
        if (user != null) {
            UserDB.removeUser(id);
            return Response.ok().build();
        } else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/login")
    public Response login(Login login) {
        PasswordServiceClient client = new PasswordServiceClient("localhost", 50551);

        Set<ConstraintViolation<Login>> violations = validator.validate(login);
        User u = UserDB.getName(login.getName());

        if (violations.size()>0){
            ArrayList<String> validationMessages = new ArrayList<String>();
            for(ConstraintViolation<Login> violation : violations){
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(validationMessages).build();
        } else {
            if(u != null){
                //making a new instance of client that gets login and decoded version of hashed pw and salt
                boolean valid = client.validate(login.getPassword(), client.decodeHexString(u.getHashedPassword()), client.decodeHexString(u.getSalt()));
                if(valid){
                    return Response.status(Response.Status.ACCEPTED).build();

                }
            }
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }
}
