/** 
 * 
 * @author Harshith Samayamantula (hs1018)
 * @author Oways Jaffer (omj9)
 * 
 */
package photoalbum.model;

import java.io.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import photoalbum.Photos.Photos;
import photoalbum.model.Admin;
import photoalbum.model.User;
import photoalbum.model.Album;

/**
 * this class allows for persistence with the application throughout sessions by saving states
 */
public class Persistence implements Serializable{
    /**
     * used for serialization
     */
    private static final long serialVersionUID = 1L;
	public static final String storeDir = "info";
	public static final String storeFile = "info.dat";
    /**
     * admin used to control other users
     */
    public Admin admin;
    /**
     * current state of user, logged in or not
     */
    public boolean loggedIn;
    /**
     * saving the current user logged in
     */
    public User currentUser;
    /**
     * saves wether or not admin is signd in
     */
    public boolean adminMode;
    /**
     * saves the current album being viewed
     */
    public Album currentAlbum;
    /**
     * sets the current album
     * @param album
     */
    public void setCurrentAlbum(Album album){
        this.currentAlbum = album;
    }

    /**
     * returns the current album open
     * @return current album
     */
    public Album getCurrentAlbum(){
        return this.currentAlbum;
    }
    /**
     * constructor 
     */
    public Persistence() {
        admin = new Admin();
		this.currentUser = null;
		this.loggedIn = false;
	}
    /**
     * checks if user exists
     * @return true or false if user exists
     */
    public boolean checkUser(String username){
        int res = this.admin.userExists(username);
        if(res == -1){
            return false;
        }
        else{
            this.setCurrentUser(admin.getUsers().get(res));
            this.loggedIn = true;
            return true;
        }
    }
    /**
     * returns index of user as int
     * @return user index
     */
    public int getUserIndex() {
		int index = 0;
		for(User user : admin.getUsers()) {
			if(user.getName().equals(Photos.driver.getCurrentUser().getName())) {
				return index;
			}
			index++;
		}
		return -1;
	}
    /**
     * returns user given username
     * @param username
     * @return user given username
     */
   
    public User getUser(String username){
        int index = this.admin.userExists(username);
        if(index == -1){
            return null;
        }
        else{
            return admin.getUsers().get(index);
        }
    }
    /**
     * sets users from arraylist of users
     *@param user    
     */
    public void setUsers(ArrayList<User> users) {
		admin.setUsers(users);
	}
    /**
     * gets current user
     */
    public User getCurrentUser() {
		return currentUser;
	}

    /**
     * sets current user    
     */
	public void setCurrentUser(User curr) {
		this.currentUser = curr;
	}
    /**
	 * Saves state to .dat file
	 * @param pdApp
	 * @throws IOException
	 */
    public static void save(Persistence pdApp) throws IOException {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
			oos.writeObject(pdApp);
			oos.close();
	}
	
    /**
	 * Loads from dat file
	 * @return list of users
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Persistence load() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
		Persistence userList = (Persistence) ois.readObject();
		ois.close();
		return userList;
		
	}

}