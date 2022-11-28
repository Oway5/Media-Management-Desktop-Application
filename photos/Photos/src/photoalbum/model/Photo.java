package photoalbum.model;

import java.awt.image.BufferedImage;
import java.util.Date;
import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.*;
import java.text.*;

public class Photo implements Serializable{

    public static final long serialVersionUID = 1L;
    public static final String storeDir = "info";
	public static final String storeFile = "info.dat";
    private Date date;
    public File picture; //Have to figure this out
    private ArrayList<Tag> tags;
    private String caption;
    private String filePath;
    private String name;
    
    //They used calendar to set the date for the photo but i was trying to set the date to last modified time of the file (line 61 + 62)
    
    
    public Date getDate(){
        return this.date;
    }

    public String getCation(){
        return this.caption;
    }

    public String getFilePath(){
        return this.filePath;
    }

    public ArrayList<Tag> getTags(){
        return this.tags;
    }

    public File getFile(){
        return this.picture;
    }
    public String getpath(){
        return this.picture.getAbsolutePath();
    }

    public void changeCaption(String newCaption){
        this.caption = newCaption;
    }
    public void addTag(String tagName, String tagValue){
        tags.add(new Tag(tagName, tagValue));   
    }
    public void delTag(String Name, String tagValue){
        for(int i =0; i<tags.size();i++){
            Tag x = tags.get(i);
            if(x.getTagName().toLowerCase().equals(Name.toLowerCase())){
                if(x.getTagValue().toLowerCase().equals(tagValue.toLowerCase())){
                    tags.remove(i);
                }
            }
        }
    }

    public boolean hasTag(Tag tag){
        for(Tag x: this.tags){
            if(x.getTagName().toLowerCase().equals(tag.getTagName().toLowerCase()) && x.getTagValue().toLowerCase().equals(tag.getTagValue().toLowerCase())){
                return true;
            }
        }
        return false;
    }

    //Constructor
    public Photo(File pic) throws IOException{
        if(pic!=null){
        this.filePath = pic.getAbsolutePath();
        this.name=pic.getName();
        this.picture=pic;
        this.tags=new ArrayList<Tag>();

        
        // File newFile = new File(filePath);
        // new FileReader(filePath)
        // this.location = filePath;
        

        FileTime fileTime = Files.getLastModifiedTime(Paths.get(filePath));
        this.date = new Date( fileTime.toMillis());
        }
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