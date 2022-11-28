package photoalbum.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import photoalbum.Photos.Persistence;

public class Album implements Serializable{
	
	public int numberPhotos;
	public int earlistDate;
	public int latestDate;
	private String albumName;
	private ArrayList<Photo> photoslist;
	private int photoCount = 0;
	private Photo selectedPhoto;
	private Date earliestPhoto;
	private Date latestPhoto;
	private static final long serialVersionUID = 1L;
	public static final String storeDir = "info";
	public static final String storeFile = "info.dat";
 
	public String getAlbumName() {
		return albumName;
	}

	public int getPhotoCount(){
		return this.photoCount;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	

	// public String getName() {
	// 	return this.albumName;
	// }

	// public void rename(String name) {
	// 	this.albumName = name;
	// }

	public Date getEarliestPhoto(){
		return this.earliestPhoto;
	}

	public Date getLatestPhoto(){
		return this.latestPhoto;
	}
	
	
	public void addPhoto(Photo photo) {
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
	}
	

	public void deletePhoto(int index) {
		photoslist.remove(index);
		this.photoCount--;
		this.earliestPhoto = findEarliestPhoto();
		this.latestPhoto = findLatestPhoto();
	}
	

	public ArrayList<Photo> getPhotos() {
		return photoslist;
	}
	
	
	public Photo getSelectedPhoto() {
		return selectedPhoto;
	}
	
	
	public void setSelectedPhoto(Photo selectedPhoto) {
		this.selectedPhoto = selectedPhoto;
	}

	
	@Override//not sure if i need this
	public String toString() {
		return getAlbumName();
	}
    public Album(String albumName) {
		this.albumName = albumName; 
		this.photoslist = new ArrayList<Photo>();
		this.photoCount = 0;
		this.earliestPhoto = null;
		this.latestPhoto = null;
	}

	public Album(String albumName, ArrayList<Photo> photos){
		this.albumName = albumName;
		this.photoslist = photos;
		this.photoCount = photos.size();
		this.earliestPhoto = findEarliestPhoto();
		this.latestPhoto = findLatestPhoto();
	}

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

	public static void save(Album pdApp) throws IOException {
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