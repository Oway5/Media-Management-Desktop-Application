/** 
 * 
 * @author Harshith Samayamantula (hs1018)
 * @author Oways Jaffer (omj9)
 * 
 */
package photoalbum.model;

import java.io.Serializable;
import java.util.*;

/**
 * This class allows tags on photos
 */
public class Tag implements Serializable{
    /**
     * name of tag associated with photo as a String
     */
    private String tagName;
    /**
     * value of tag associated with photo as a String
     */
    private String tagValue;
    /**
     * constructor 
     */
    public Tag(String tagName, String tagValue){
        this.tagName = tagName;
        this.tagValue = tagValue;
    }
    /**
     * gets tag name
     * @return tag name as a string
     */
    public String getTagName(){
        return this.tagName;
    }
    /**
     * gets the value of the tag and returns it
     * @return tag value as a string
     */
    public String getTagValue(){
        return this.tagValue;
    }
    /**
     * compares tag values
     * @return true or false if they're equal
     */
    public boolean equals(Object t){
        if(t instanceof Tag){
            Tag tag = (Tag) t;
            return this.tagName.equals(tag.getTagName()) && this.tagValue.equals(tag.getTagValue());
        }
        else{
            return false;
        }
    }

}