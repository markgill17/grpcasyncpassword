package ie.gmit.ds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserDB {

    public static HashMap<Integer, User> users = new HashMap<>();
    static{
        users.put(1, new User(1, "Charlie", "charlie@gmail.com", "charlie"));
        users.put(2, new User(2, "Dennis", "dennis@gmail.com", "dennis"));
        users.put(3, new User(3, "Mac", "mac@gmail.com", "mac"));
    }

    public static List<User> getUsers(){
        return new ArrayList<User>(users.values());
    }

    public static User getUser(Integer id){
        return users.get(id);
    }

    public static void updateUser(Integer id, User user){
        users.put(id, user);
    }

    public static void removeUser(Integer id){
        users.remove(id);
    }

    public static User getName(String username){
        for(User u : users.values()){
            if(u.getName().equals(username)){
                return u;
            }
        }
        return null;
    }

}
