package photoalbum.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Admin implements Serializable{
    
    private ArrayList<User> users;
    private String username;

    public Admin(ArrayList<User> users){
        this.username = "admin";
        this.users = users;
    }

    public Admin(){
        this.username = "admin";
        this.users = new ArrayList<User>();
    }

    public void setUsers(ArrayList<User> users){
        this.users = users;
    }
    
    public void addUser(String userName){
        User newUser = new User(userName);
        users.add(newUser);
    }

    public ArrayList<User> getUsers(){
        return users;
    }

    public int userExists(String userName){
        for(int i = 0; i < users.size(); i++){
            if(users.get(i).getName().equals(userName)){
                return i;
            }
        }
        return -1;
    }

    public void deleteUser(String userName){
        if(userName.equals("stock")) return;
        for(int i = 0; i < users.size(); i++){
            if(users.get(i).getName().equals(userName)){
                users.remove(i);
                return;
            }
        }
    }


}