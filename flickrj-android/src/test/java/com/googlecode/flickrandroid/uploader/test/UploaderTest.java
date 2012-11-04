/**
 * 
 */
package com.googlecode.flickrandroid.uploader.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.googlecode.flickrandroid.test.AbstractFlickrTest;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.uploader.UploadMetaData;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
@Ignore("I certainly do not want to upload to many photos")
public class UploaderTest extends AbstractFlickrTest {

    /**
     * Test method for {@link com.googlecode.flickrjandroid.uploader.Uploader#upload(byte[], com.googlecode.flickrjandroid.uploader.UploadMetaData)}.
     * @throws SAXException 
     * @throws FlickrException 
     * @throws IOException 
     */
    @Test
    public void testUploadByteArrayUploadMetaData() throws IOException, FlickrException, SAXException {
        InputStream input = null;
        try {
            byte[] photoData = convertInputStreamToBytes(input);

            UploadMetaData metadata = new UploadMetaData();
            metadata.setTitle("TestUploadPhoto");
            metadata.setDescription(String.format(Locale.US, "Uploaded at %s by %s", 
                    new Date(System.currentTimeMillis()), System.getProperty("user.name")));
            metadata.setAsync(false);
            String result = f.getUploader().upload("icon.jpg", photoData, metadata);
            System.out.println(result);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.uploader.Uploader#upload(java.io.InputStream, com.googlecode.flickrjandroid.uploader.UploadMetaData)}.
     * @throws SAXException 
     * @throws FlickrException 
     * @throws IOException 
     */
    @Test
    public void testUploadInputStreamUploadMetaData() throws IOException, FlickrException, SAXException {
        InputStream input = null;
        try {
            input = Thread.currentThread().getContextClassLoader().getResourceAsStream("icon.jpg");
            UploadMetaData metadata = new UploadMetaData();
            metadata.setTitle("TestUploadPhoto");
            metadata.setDescription(String.format(Locale.US, "Uploaded at %s by %s", 
                    new Date(System.currentTimeMillis()), System.getProperty("user.name")));
            metadata.setAsync(false);
            String result = f.getUploader().upload("icon.jpg", input, metadata);
            System.out.println(result);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.uploader.Uploader#replace(java.io.InputStream, java.lang.String, boolean)}.
     * @throws JSONException 
     * @throws SAXException 
     * @throws FlickrException 
     * @throws IOException 
     */
    @Ignore("currently not working")
    @Test
    public void testReplaceInputStreamStringBoolean() throws IOException, FlickrException, SAXException, JSONException {
        InputStream input = null;
        try {
            input = Thread.currentThread().getContextClassLoader().getResourceAsStream("icon.jpg");
            String result = f.getUploader().replace("icon.jpg", input, "5575910305", false);
            System.out.println(result);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private byte[] convertInputStreamToBytes(InputStream in) throws IOException {
        InputStream input = null;
        ByteArrayOutputStream buffer = null;
        try {
            input = Thread.currentThread().getContextClassLoader().getResourceAsStream("icon.jpg");
            buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = input.read(data, 0, data.length)) != -1) {
              buffer.write(data, 0, nRead);
            }

            buffer.flush();
            return buffer.toByteArray();
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Test method for {@link com.googlecode.flickrjandroid.uploader.Uploader#replace(byte[], java.lang.String, boolean)}.
     * @throws IOException 
     * @throws SAXException 
     * @throws FlickrException 
     */
    @Ignore("currently not working")
    @Test
    public void testReplaceByteArrayStringBoolean() throws IOException, FlickrException, SAXException {
        InputStream input = null;
        try {
            byte[] photDoata = convertInputStreamToBytes(input);
            String result = f.getUploader().replace("icon.jpg", photDoata, "5575910305", false);
            System.out.println(result);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
