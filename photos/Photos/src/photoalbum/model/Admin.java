/** 
 * 
 * @author Harshith Samayamantula (hs1018)
 * @author Oways Jaffer (omj9)
 * 
 */
package photoalbum.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class defines and controls how to add users
 */
public class Admin implements Serializable{
    /**
     * Arraylist of users
     */
    private ArrayList<User> users;
    /**
     * username String for a specific user
     */
    private String username;
    /**
     * creates user (constructor) with user list input
     * @param users 
     */
    public Admin(ArrayList<User> users){
        this.username = "admin";
        this.users = users;
    }
/**
 * constructor
 */
    public Admin(){
        this.username = "admin";
        this.users = new ArrayList<User>();
    }
/** 
 * @param users
 */
    public void setUsers(ArrayList<User> users){
        this.users = users;
    }
    /**
     * adds user to userlist
     * @param userName
     */
    public void addUser(String userName){
        User newUser = new User(userName);
        users.add(newUser);
    }
    /**
     * returns a list of users
     * @return list of users
     */
    public ArrayList<User> getUsers(){
        return users;
    }
    /**
     * returns list of usernames
     * @return arraylist of usernames
     */
    public ArrayList<String> getUsernameList(){
        ArrayList<String> usernames = new ArrayList<String>();
        for(User u: this.users){
            usernames.add(u.getName());
        }
        return usernames;
    }
    /**
     * checks if user exists and returns 1 or -1
     * @param userName
     */
    public int userExists(String userName){
        for(int i = 0; i < users.size(); i++){
            if(users.get(i).getName().equals(userName)){
                return i;
            }
        }
        return -1;
    }
    /**
     * deletes given user
     * @param userName
     */
    public void deleteUser(String userName){
        // if(userName.equals("stock")) return;
        for(int i = 0; i < users.size(); i++){
            if(users.get(i).getName().equals(userName)){
                users.remove(i);
                return;
            }
        }
    }


}