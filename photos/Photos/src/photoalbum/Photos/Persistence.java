package photoalbum.Photos;

import java.io.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import photoalbum.model.Admin;
import photoalbum.model.User;


public class Persistence implements Serializable{
    private static final long serialVersionUID = 1L;
	public static final String storeDir = "info";
	public static final String storeFile = "info.dat";
    public Admin admin;
    public boolean loggedIn;
    public User currentUser;
    public boolean adminMode;

    public Persistence() {
        admin = new Admin();
		this.currentUser = null;
		this.loggedIn = false;
	}

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

    public int getUserIndex() {
		int index = 0;
		for(User user : admin.getUsers()) {
			if(user.getName().equals(Main.driver.getCurrentUser().getName())) {
				return index;
			}
			index++;
		}
		return -1;
	}

    public User getUser(String username){
        int index = this.admin.userExists(username);
        if(index == -1){
            return null;
        }
        else{
            return admin.getUsers().get(index);
        }
    }

    public void setUsers(ArrayList<User> users) {
		admin.setUsers(users);
	}

    public User getCurrentUser() {
		return currentUser;
	}


	public void setCurrentUser(User curr) {
		this.currentUser = curr;
	}

    public static void save(Persistence pdApp) throws IOException {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
			oos.writeObject(pdApp);
			oos.close();
	}
	

	public static Persistence load() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
		Persistence userList = (Persistence) ois.readObject();
		ois.close();
		return userList;
		
	}

}