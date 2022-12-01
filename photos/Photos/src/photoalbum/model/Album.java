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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

/**
 * Class that user utilizes to manipulate albums
 */
public class Album implements Serializable{
	/**
	 * number of photos in album
	 */
	public int numberPhotos;
	/**
	 * earliest date of photo in album	
	 */
	public int earlistDate;
	/**
	 * latest date of photo in album
	 */
	public int latestDate;
	/**
	 * name of Album
	 */
	private String albumName;
	/**
	 * arraylist of photos in the album
	 */
	private ArrayList<Photo> photoslist;
	/**
	 * number of photos in album
	 */
	private int photoCount = 0;
	/**
	 * current photo selected by user
	 */
	private Photo selectedPhoto;
	/**
	 * earliest photo in the album
	 */
	private Date earliestPhoto;
	/**
	 * latest photo in the album
	 */
	private Date latestPhoto;
	/**
	 * int used to display photo index
	 */
	private int displayPhotoIndex;
	/**
	 * used for serialization 
	 */
	private static final long serialVersionUID = 1L;
	public static final String storeDir = "info";
	public static final String storeFile = "info.dat";
	/**
	 * gets the displayed photo index
	 * @return displayPhotoIndex
	 */
	public int getDisplayPhotoIndex(){
		return displayPhotoIndex;
	}
	/**
	 * sets photo index
	 * @param newIdex
	 */
	public void setDisplayPhotoIndex(int newIndex){
		this.displayPhotoIndex = newIndex;
	}
	/**
	 * returns Album name
	 * @return albumName returns album name
	 */
	public String getAlbumName() {
		return albumName;
	}
	/**
	 * returns number of photos
	 * @return photoCount number of phtotos
	 */
	public int getPhotoCount(){
		return this.photoCount;
	}
	/**
	 * sets album name 
	 * @param albumname
	 */
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	

	// public String getName() {
	// 	return this.albumName;
	// }blank

	// public void rename(String name) {
	// 	this.albumName = name;
	// }
	/**
	 * returns earliest photo in album
	 * @return Date returns earliest photo
	 */
	public Date getEarliestPhoto(){
		return this.earliestPhoto;
	}
	/**
	 *	returns date of latest photo in album
	 * @return Date date of latest photo
	 */

	public Date getLatestPhoto(){
		
		return this.latestPhoto;
		
	}
	
	/**
	 * returns if the photo was already in album
	 * @param photo 
	 * @return if the photo has been added to album
	 */
	public boolean addPhoto(Photo photo) {
		//Add check to see if photo has already been added
		if(photoslist.contains(photo)) return false;
		photoslist.add(photo);
		this.photoCount++;
		if(this.earliestPhoto == null && this.latestPhoto == null){
			this.earliestPhoto = photo.getDate();
			this.latestPhoto = photo.getDate();
		}
		if(photo.getDate().compareTo(earliestPhoto) < 0){
			this.earliestPhoto = photo.getDate();
		}
		if(photo.getDate().compareTo(latestPhoto) > 0){
			this.latestPhoto = photo.getDate();
		}
		return true;
	}
	
	/**
	 * deletes photo based off index
	 * @param index
	 */
	public void deletePhoto(int index) {
		photoslist.remove(index);
		this.photoCount--;
		this.earliestPhoto = findEarliestPhoto();
		this.latestPhoto = findLatestPhoto();
	}
	
	/**
	 * returns arraylist of photos from album
	 * @return ArrayList of photos
	 */
	public ArrayList<Photo> getPhotos() {
		return photoslist;
	}
	
	/**
	 * returns photo selected by user
	 * @return photo
	 */
	public Photo getSelectedPhoto() {
		return selectedPhoto;
	}
	/**
	 * sets the selected photo to the index given
	 * @param index
	 */
	public void setSelectedPhoto(int index){
		this.selectedPhoto = photoslist.get(index);
	}
	
	/**
	 * sets selected photo based off Photo given
	 * @param selectedPhoto
	 */
	public void setSelectedPhoto(Photo selectedPhoto) {
		this.selectedPhoto = selectedPhoto;
	}

	/**
	 * returns album name
	 * @return albumname
	 */
	@Override//not sure if i need this
	public String toString() {
		return getAlbumName();
	}
	/**
	 * constructor
	 */
    public Album(String albumName) {
		this.albumName = albumName; 
		this.photoslist = new ArrayList<Photo>();
		this.photoCount = 0;
		this.earliestPhoto = null;
		this.latestPhoto = null;
		this.displayPhotoIndex = -1;
	}
/**
 * constructor 
 */
	public Album(String albumName, ArrayList<Photo> photos){
		this.albumName = albumName;
		this.photoslist = photos;
		this.photoCount = photos.size();
		this.earliestPhoto = findEarliestPhoto();
		this.latestPhoto = findLatestPhoto();
		this.displayPhotoIndex = (this.photoCount > 0)? 0: -1;
	}
	/**
	 *  returns the earliest photo in the album
	 * @return earliest photo date
	 */

	public Date findEarliestPhoto(){
		if(this.photoslist.size() == 0) return null;
		Date earliest = this.photoslist.get(0).getDate();
		for(Photo p: this.photoslist){
			if(p.getDate().compareTo(earliest) < 0){
				earliest = p.getDate();
			}
		}
		return earliest;
	}
	/**
	 * returns the date of the latest photo in album
	 * @return latest photo date
	 */
	public Date findLatestPhoto(){
		if(this.photoslist.size() == 0) return null;
		Date latest = this.photoslist.get(0).getDate();
		for(Photo p: this.photoslist){
			if(p.getDate().compareTo(latest) > 0){
				latest = p.getDate();
			}
		}
		return latest;
	}
	/**
	 * returns String containing details for the album
	 * @return string of album details
	 */
	public String printAlbumDetails(){
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String details = "";
		details+= ("Name: " + this.albumName + "\n");
        details+= ("# of Photos: "+ this.photoCount + "\n");
		if(earliestPhoto != null && latestPhoto != null)
        	details+= ("Dates: "+ formatter.format(this.earliestPhoto) + " --> "+  formatter.format(this.latestPhoto) + "\n");
		return details;
	}
	/**
	 * Saves state to .dat file
	 * @param pdApp
	 * @throws IOException
	 */
	public static void save(Album pdApp) throws IOException {
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