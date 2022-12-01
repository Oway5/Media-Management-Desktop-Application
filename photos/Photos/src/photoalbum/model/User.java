/** 
 * 
 * @author Harshith Samayamantula (hs1018)
 * @author Oways Jaffer (omj9)
 * 
 */

package photoalbum.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
/**
 * class designed to hold all user attributes
 */
public class User implements Serializable{
    /**
     * array list of albums a user owns
     */
    private ArrayList<Album> albums;
    /**
     * username of the user
     */
    private String username;
    /**
     * arraylist of all the albums' names a user owns
     */
    public ArrayList<String> albumNames = new ArrayList<String>();
    /**
     * data used for serialization to save state
     */
    public static final long serialVersionUID = 1L;
    public static final String storeDir = "info";
	public static final String storeFile = "info.dat";
    //Define comparators to sort albums
    static Comparator<Album> alphabetical = new Comparator<Album>() {

        /**
         * comapres albums 
         * @param arg0 album 1
         * @param arg1 album 2
         * @return int comparing the two albums
         */
		@Override
		public int compare(Album arg0, Album arg1) {
			
			return arg0.getAlbumName().compareTo(arg1.getAlbumName());
		}
        
    };
    /**
     * comparator of type Album used to find earliest date
     */
    static Comparator<Album> byEarliestDate = new Comparator<Album>() {
        /**
         * compare function between two albums to find the earliest date
         * @param arg0 album 1
         * @param arg1 album 2 
         * @return int representing the state after comparing the two albums' earliest photos
         */
		@Override
		public int compare(Album arg0, Album arg1) {
			
            Date date1 = arg0.getEarliestPhoto();
            Date date2 = arg1.getEarliestPhoto();
            if(date1 == null && date2 == null) return 0;
            if(date1 == null && date2 != null) return -1;
            if(date2 == null && date1 != null) return 1;
			return arg0.getEarliestPhoto().compareTo(arg1.getEarliestPhoto());
		}
        
    };
    /**
     * arraylist of type photo containing the results of an "or search" by the user
     * @param searchTags optional tags used in the search 
     * @return arraylist of type Photo containing search results
     */
    public ArrayList<Photo> orTagSearch(ArrayList<Tag> searchTags){
        ArrayList<Photo> results = new ArrayList<Photo>();
        HashSet<Photo> noDuplicates = new HashSet<Photo>();
        for(Album a: this.albums){
            for(Photo picture: a.getPhotos()){
                for(Tag searchTag: searchTags){
                    for(Tag photoTag: picture.getTags()){
                        if(photoTag.getTagName().toLowerCase().equals(searchTag.getTagName().toLowerCase()) && photoTag.getTagValue().toLowerCase().equals(searchTag.getTagValue().toLowerCase())){
                            noDuplicates.add(picture);
                        }
                    }
                }
            }
        }
        results.addAll(noDuplicates);
        return results;
    }
    /**
     * returns an arraylist of type photo containing search results from a mandatory tag search
     * @param searchTags mandatory tags
     * @return Arraylist of Photos containing photos that apply to the search
     */
    public ArrayList<Photo> andTagSearch(ArrayList<Tag> searchTags){
        ArrayList<Photo> results = new ArrayList<Photo>();
        HashSet<Photo> noDuplicates = new HashSet<Photo>();
        boolean found = false;
        for(Album a: this.albums){
            for(Photo picture: a.getPhotos()){
                for(Tag searchTag: searchTags){
                    if(!picture.hasTag(searchTag)){
                        found = false;
                        break;
                    }
                    else{
                        found = true;
                    }
                }
                if(found){
                    noDuplicates.add(picture);
                }
            }
        }
        results.addAll(noDuplicates);
        return results;
    }

    /**
     * arraylist of photos containing photos within a specified range
     * @param date1 first date in range
     * @param date2 second date in range
     * @return returns arraylist of photos which are within the date range provided
     */
    public ArrayList<Photo> getPhotosInRange(Date date1, Date date2){
        ArrayList<Photo> results = new ArrayList<Photo>();
        for(Album album: this.albums){
            for(Photo picture: album.getPhotos()){
                Date testDate = picture.getDate();
                if(testDate.compareTo(date1) >= 0 && testDate.compareTo(date2) <= 0){
                    if(results.contains(picture)){
                        continue;
                    }
                    else{
                        results.add(picture);
                    }
                }
            }
        }
        return results;
    }

    /**
     * arraylist containing all abums's names owned by the user
     * @return arraylist of type string containing all album names. albums are owned by the user
     */
    public ArrayList<String> getAlbumNameList(){
        // this.albumNames.clear();
        this.albumNames = new ArrayList<String>();
        for(int i=0;i<albums.size();i++){
            this.albumNames.add(albums.get(i).getAlbumName());
        }
        return albumNames;
    }


    /**
     * getter. gets user's username 
     * @return username as a string
     */
    public String getName(){
        return this.username;
    }
    /**
     * getter. gets arraylist containing albums for user
     * @return arraylist of type album with a user's albums 
     */
    public ArrayList<Album> getAlbums(){
        return this.albums;
    }
    /**
     * constructor
     */
    public User(String username){
        this.username = username;
        this.albums = new ArrayList<Album>();
    }
    /**
     * creates album with the specified albumname
     * @param albumname
     */
    public void createAlbum(String albumName){
        Album newAlbum = new Album(albumName);
        this.albums.add(newAlbum);
    }
    /**
     * adds given album to album arraylist 
     * @param a album given to be added to arraylist
     */
    public void addAlbum(Album a){
        this.albums.add(a);
    }


    /**
     * finds album given album name
     * @param albumName given album name
     * @return album with matching name to the given 
     */
    public Album findAlbum(String albumName){
        for(Album a : this.albums){
            if(a.getAlbumName().equals(albumName)){
                return a;
            }
        }
        return null;
    }
    /**
     * sorts user's albums 
     * @param x given comparator to sort albums
     */
    public void sortAlbums(Comparator<Album> x){
        this.albums.sort(x);
    }
    /**
     * deletes album specified as object album
     * @param album album to be deleted 
     */
    public void deleteAlbum(Album album){
        albums.remove(album);
    }
    /**
     * deletes album specified by a name String 
     * @param albumName
     */
    public void deleteAlbum(String albumName){
        Album foundAlbum = findAlbum(albumName);
        albums.remove(foundAlbum);
    }
    /**
     * deletes album specified by given index
     * @param index
     */
    public void deleteAlbum(int index){
        this.albums.remove(index);
    }
    /**
     * returns album at the index position given
     * @param index
     * @return album at the index given
     */
    public Album getAlbumAt(int index){
        return this.albums.get(index);
    }
    /**
     * renames given album to the name string given
     * @param album
     * @param newName
     */
    public void renameAlbum(Album album, String newName){
        album.setAlbumName(newName);
    }
    /**
     * checks if given user is equal to this user
     * @param u user object given
     * @return true or false if they're equal or not 
     */
    @Override
    public boolean equals(Object u){
        if(u instanceof User){
            User user = (User) u;
            return this.getName().equals(user.getName());
        }
        else{
            return false;
        }
    }


    public static void updateNameList(){

    }
//-----------------------------------------------------------------------------------------------------------------------------------
    /**
	 * Saves user state to .dat file
	 * @param user
	 * @throws IOException
	 */
    public static void save(User user) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(user);
		oos.close();
	}
	/**
	 * Loads from dat file
	 * @return list of users
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static User load() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
		User userList = (User) ois.readObject();
		ois.close();
		return userList;
	}
}