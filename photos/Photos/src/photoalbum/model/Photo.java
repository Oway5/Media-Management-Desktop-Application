/** 
 * 
 * @author Harshith Samayamantula (hs1018)
 * @author Oways Jaffer (omj9)
 * 
 */
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

/**
 * this class lets the user manipulate photos
 */
public class Photo implements Serializable{
    /**
     * data used for serialization
     */
    public static final long serialVersionUID = 1L;
    public static final String storeDir = "info";
	public static final String storeFile = "info.dat";
    /**
     * date associated with the photo
     */
    private Date date;
    /**
     * the File of the picture itself
     */
    private File picture; //Have to figure this out
    /**
    * arraylist of tags pertaining to a photo
    */
    private ArrayList<Tag> tags;
    /**
     * caption of a photo as a String    
     */
    private String caption;
    /**
     * filepath of the photo as a string 
     */
    private String filePath;
    /**
     * name of photo stored as string
     */
    private String name;
    
     /**
      * returns name of photo
      * @return returns name 
      */
    public String getName(){
        return name;
    }
    /**
     * returns date of photo
     * @return date of photo
     */
    public Date getDate(){
        return this.date;
    }
    /**
     * returns caption of photo
     * @return caption string
     */
    public String getCaption(){
        return this.caption;
    }
    /**
     * returns file path of photo
     * @return filepath as string 
     */
    public String getFilePath(){
        return this.filePath;
    }
    /**
     * returns tags in arraylist 
     * @return arraylist of tags pertaining to this photo
     */
    public ArrayList<Tag> getTags(){
        return this.tags;
    }
    /**
     * returns file, the picture itself
     * @return file the picture
     */
    public File getFile(){
        return this.picture;
    }
    /**
     * returns path of photo as a string
     * @return filepath as string
     */
    public String getpath(){
        return this.picture.getAbsolutePath();
    }
    /**
     * changes caption of photo
     * @param newCaption caption  user wants to change to 
     */
    public void changeCaption(String newCaption){
        this.caption = newCaption;
    }
    /**
     * adds tag to photo with the parameters specified
     * @param tagName 
     * @param tagValue
     */
    public void addTag(String tagName, String tagValue){
        tags.add(new Tag(tagName, tagValue));   
    }
    /**
     * deletes tag given name and value
     * @param Name
     * @param tagValue
     */
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
    /**
     * checks if tag is exists in photo
     * @param tag
     * @return true or false if tag is in photo
     */
    public boolean hasTag(Tag tag){
        for(Tag x: this.tags){
            if(x.getTagName().toLowerCase().equals(tag.getTagName().toLowerCase()) && x.getTagValue().toLowerCase().equals(tag.getTagValue().toLowerCase())){
                return true;
            }
        }
        return false;
    }

    //Constructor
    /**
     * contructor given a File
     * @param pic
     * @throws IOexception
     */
    public Photo(File pic) throws IOException{
        if(pic!=null){
        this.filePath = pic.getAbsolutePath();
        this.name=pic.getName();
        this.picture=pic;
        this.tags=new ArrayList<Tag>();
        this.caption = "";
        
        // File newFile = new File(filePath);
        // new FileReader(filePath)
        // this.location = filePath;
        

        FileTime fileTime = Files.getLastModifiedTime(Paths.get(filePath));
        this.date = new Date( fileTime.toMillis());
        }
    }
    /**
     * checks if pictures are equal
     * @param o object we are comparing
     * @return true or false depending on if its equal
     */
    public boolean equals(Object o){
        if(o instanceof Photo){
            Photo pic = (Photo) o;
            return (this.picture.equals(pic.picture) && this.name.equals(pic.getName()));
        }
        else{
            return false;
        }
    }

    /**
     * formats all detials of a picture into a single string
     */
    public String printDetails(){
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        String details = "";
        details+= ("Name: " + this.name + "\n");
        details+= ("Caption: "+ this.caption + "\n");
        details+= ("Date: "+ formatter.format(this.date) + "\n");
        details+= "Tags:\n";
        for(Tag t: this.tags){
            details+= "Name: " + t.getTagName() + " | Value: " + t.getTagValue() + "\n";
        }
        
        return details;
    }
    /**
	 * Saves state to .dat file
	 * @param pdApp
	 * @throws IOException
	 */
    public static void save(Photo pdApp) throws IOException {
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
	public static User load() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
		User userList = (User) ois.readObject();
		ois.close();
		return userList;
	}
}