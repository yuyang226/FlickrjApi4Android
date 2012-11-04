/**
 * 
 */
package com.googlecode.flickrjandroid.uploader;

import com.googlecode.flickrjandroid.Parameter;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class ImageParameter extends Parameter {
    private String imageName;
    private String imageType = "jpeg";

    /**
     * @param name
     * @param value
     */
    public ImageParameter(String imageName, Object value) {
        super("photo", value);
        this.imageName = imageName;
    }

    /**
     * @return the imageName
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * @param imageName the imageName to set
     */
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    /**
     * @return the imageType
     */
    public String getImageType() {
        return imageType;
    }

    /**
     * @param imageType the imageType to set
     */
    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

}
