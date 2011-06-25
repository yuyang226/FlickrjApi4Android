/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.aetrion.flickr.uploader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.Parameter;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.Transport;
import com.aetrion.flickr.auth.AuthUtilities;
import com.aetrion.flickr.util.StringUtilities;

/**
 * Upload a photo.<p>
 *
 * Setting {@link com.aetrion.flickr.uploader.UploadMetaData#setAsync(boolean)}
 * you can switch between synchronous and asynchronous uploads.<p>
 *
 * Synchronous uploads return the photoId, whilst asynchronous uploads
 * return a ticketId.<p>
 *
 * TicketId's can be tracked with
 * {@link com.aetrion.flickr.photos.upload.UploadInterface#checkTickets(Set)}
 * for completion.
 *
 * @author Anthony Eden
 * @version $Id: Uploader.java,v 1.12 2009/12/15 20:57:49 x-mago Exp $
 */
public class Uploader {
    private String apiKey;
    private String sharedSecret;
    private Transport transport;

    /**
     * Construct an Uploader.
     *
     * @param apiKey The API key
     */
    public Uploader(String apiKey, String sharedSecret) {
        try {
            this.apiKey = apiKey;
            this.sharedSecret = sharedSecret;
            this.transport = new REST();
            this.transport.setResponseClass(UploaderResponse.class);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Upload a photo from a byte-array.
     *
     * @param data The photo data as a byte array
     * @param metaData The meta data
     * @return photoId or ticketId
     * @throws FlickrException
     * @throws IOException
     * @throws SAXException
     */
    public String upload(byte[] data, UploadMetaData metaData) throws FlickrException, IOException, SAXException {
        List<Parameter> parameters = new ArrayList<Parameter>();

        parameters.add(new Parameter("api_key", apiKey));

        String title = metaData.getTitle();
        if (title != null)
            parameters.add(new Parameter("title", title));

        String description = metaData.getDescription();
        if (description != null)
            parameters.add(new Parameter("description", description));

        Collection<String> tags = metaData.getTags();
        if (tags != null)
            parameters.add(new Parameter("tags", StringUtilities.join(tags, " ")));

        parameters.add(new Parameter("is_public", metaData.isPublicFlag() ? "1" : "0"));
        parameters.add(new Parameter("is_family", metaData.isFamilyFlag() ? "1" : "0"));
        parameters.add(new Parameter("is_friend", metaData.isFriendFlag() ? "1" : "0"));

        parameters.add(new Parameter("photo", data));

        if (metaData.isHidden() != null) {
            parameters.add(new Parameter("hidden", metaData.isHidden().booleanValue() ? "1" : "0"));
        }

        if (metaData.getSafetyLevel() != null) {
            parameters.add(new Parameter("safety_level", metaData.getSafetyLevel()));
        }

        parameters.add(new Parameter("async", metaData.isAsync() ? "1" : "0"));

        if (metaData.getContentType() != null) {
            parameters.add(new Parameter("content_type", metaData.getContentType()));
        }
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getMultipartSignature(sharedSecret, parameters)
            )
        );

        UploaderResponse response = (UploaderResponse) transport.post("/services/upload/", parameters, true);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        String id = "";
        if (metaData.isAsync()) {
            id = response.getTicketId();
        } else {
            id = response.getPhotoId();
        }
        return id;
    }

    /**
     * Upload a photo from an InputStream.
     *
     * @param in
     * @param metaData
     * @return photoId or ticketId
     * @throws IOException
     * @throws FlickrException
     * @throws SAXException
     */
    public String upload(InputStream in, UploadMetaData metaData) throws IOException, FlickrException, SAXException {
        List<Parameter> parameters = new ArrayList<Parameter>();

        parameters.add(new Parameter("api_key", apiKey));

        String title = metaData.getTitle();
        if (title != null)
            parameters.add(new Parameter("title", title));

        String description = metaData.getDescription();
        if (description != null)
            parameters.add(new Parameter("description", description));

        Collection<String> tags = metaData.getTags();
        if (tags != null) {
            parameters.add(new Parameter("tags", StringUtilities.join(tags, " ")));
        }

        parameters.add(new Parameter("is_public", metaData.isPublicFlag() ? "1" : "0"));
        parameters.add(new Parameter("is_family", metaData.isFamilyFlag() ? "1" : "0"));
        parameters.add(new Parameter("is_friend", metaData.isFriendFlag() ? "1" : "0"));
        parameters.add(new Parameter("async", metaData.isAsync() ? "1" : "0"));

        parameters.add(new Parameter("photo", in));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getMultipartSignature(sharedSecret, parameters)
            )
        );

        UploaderResponse response = (UploaderResponse) transport.post("/services/upload/", parameters, true);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        String id = "";
        if (metaData.isAsync()) {
            id = response.getTicketId();
        } else {
            id = response.getPhotoId();
        }
        return id;
    }

    /**
     * Upload a photo from an InputStream.
     *
     * @param in
     * @param metaData
     * @return photoId or ticketId
     * @throws IOException
     * @throws FlickrException
     * @throws SAXException
     */
    public String replace(InputStream in, String flickrId, boolean async) throws IOException, FlickrException, SAXException {
        List<Parameter> parameters = new ArrayList<Parameter>();

        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("async", async ? "1" : "0"));
        parameters.add(new Parameter("photo_id", flickrId));

        parameters.add(new Parameter("photo", in));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getMultipartSignature(sharedSecret, parameters)
            )
        );

        UploaderResponse response = (UploaderResponse) transport.post("/services/replace/", parameters, true);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        String id = "";
        if (async) {
            id = response.getTicketId();
        } else {
            id = response.getPhotoId();
        }
        return id;
    }

    /**
     * Upload a photo from an InputStream.
     *
     * @param in
     * @param metaData
     * @return photoId or ticketId
     * @throws IOException
     * @throws FlickrException
     * @throws SAXException
     */
    public String replace(byte[] data, String flickrId, boolean async) throws IOException, FlickrException, SAXException {
        List<Parameter> parameters = new ArrayList<Parameter>();

        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("async", async ? "1" : "0"));
        parameters.add(new Parameter("photo_id", flickrId));

        parameters.add(new Parameter("photo", data));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getMultipartSignature(sharedSecret, parameters)
            )
        );

        UploaderResponse response = (UploaderResponse) transport.post("/services/replace/", parameters, true);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        String id = "";
        if (async) {
            id = response.getTicketId();
        } else {
            id = response.getPhotoId();
        }
        return id;
    }
}
