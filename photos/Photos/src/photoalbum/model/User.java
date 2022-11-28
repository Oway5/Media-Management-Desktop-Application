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

public class User implements Serializable{

    private ArrayList<Album> albums;
    private String username;
    public ArrayList<String> albumNames;
    public static final long serialVersionUID = 1L;
    public static final String storeDir = "info";
	public static final String storeFile = "info.dat";
    //Define comparators to sort albums
    static Comparator<Album> alphabetical = new Comparator<Album>() {

		@Override
		public int compare(Album arg0, Album arg1) {
			
			return arg0.getAlbumName().compareTo(arg1.getAlbumName());
		}
        
    };

    static Comparator<Album> byEarliestDate = new Comparator<Album>() {

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
    public ArrayList<String> getAlbumNameList(){
        for(int i=0;i<albums.size();i++){
            this.albumNames.add(albums.get(i).getAlbumName());
        }
        return albumNames;
    }


    
    public String getName(){
        return this.username;
    }

    public ArrayList<Album> getAlbums(){
        return this.albums;
    }
    
    public User(String username){
        this.username = username;
        this.albums = new ArrayList<Album>();
    }
    
    public void createAlbum(String albumName){
        Album newAlbum = new Album(albumName);
        this.albums.add(newAlbum);
    }



    public Album findAlbum(String albumName){
        for(Album a : this.albums){
            if(a.getAlbumName().equals(albumName)){
                return a;
            }
        }
        return null;
    }

    public void sortAlbums(Comparator<Album> x){
        this.albums.sort(x);
    }

    public void deleteAlbum(Album album){
        albums.remove(album);
    }

    public void deleteAlbum(String albumName){
        Album foundAlbum = findAlbum(albumName);
        albums.remove(foundAlbum);
    }

    public void renameAlbum(Album album, String newName){
        album.setAlbumName(newName);
    }

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


    public static void save(Photo pdApp) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(pdApp);
		oos.close();
	}
	
	public static User load() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
		User userList = (User) ois.readObject();
		ois.close();
		return userList;
	}
}