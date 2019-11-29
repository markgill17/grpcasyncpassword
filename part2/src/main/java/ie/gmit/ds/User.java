package ie.gmit.ds;

import jdk.nashorn.internal.runtime.doubleconv.DtoaBuffer;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
public class User {

    @NotNull
    private int id;
    private String name;
    @Pattern(regexp=".+@.+\\.[a-z]+")
    private String email;
    private String password;
    private String hashedPassword;
    private String salt;

    public User(Integer id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        String[] response= new PasswordServiceClient("localhost", 50551).hash(id, password);

        // putting the response into an arraylist to avoid extra conversion
        this.hashedPassword = response[0];
        this.salt = response[1];
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public User(){}





    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
