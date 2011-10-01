package com.gmail.yuyang226.flickr.photos.upload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.gmail.yuyang226.flickr.FlickrException;
import com.gmail.yuyang226.flickr.Parameter;
import com.gmail.yuyang226.flickr.Response;
import com.gmail.yuyang226.flickr.Transport;
import com.gmail.yuyang226.flickr.oauth.OAuthInterface;
import com.gmail.yuyang226.flickr.oauth.OAuthUtils;
import com.gmail.yuyang226.flickr.org.json.JSONArray;
import com.gmail.yuyang226.flickr.org.json.JSONException;
import com.gmail.yuyang226.flickr.org.json.JSONObject;


/**
 * Checks the status of asynchronous photo upload tickets.
 *
 * @author till (Till Krech) extranoise:flickr
 * @version $Id: UploadInterface.java,v 1.3 2008/01/28 23:01:45 x-mago Exp $
 */
public class UploadInterface {
    public static final String METHOD_CHECK_TICKETS  = "flickr.photos.upload.checkTickets";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public UploadInterface(
        String apiKey,
        String sharedSecret,
        Transport transport
     ) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transport;
     }

    /**
     * Checks the status of one or more asynchronous photo upload tickets.
     * This method does not require authentication.
     *
     * @param tickets a set of ticket ids (Strings) or {@link Ticket} objects containing ids
     * @return a list of {@link Ticket} objects.
     * @throws IOException
     * @throws FlickrException
     * @throws JSONException 
     */
    public List<Ticket> checkTickets(Set<Object> tickets) throws IOException, FlickrException, JSONException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_CHECK_TICKETS));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));

        StringBuffer sb = new StringBuffer();
        Iterator<Object> it = tickets.iterator();
        while (it.hasNext()) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            Object obj = it.next();
            if (obj instanceof Ticket) {
                sb.append(((Ticket) obj).getTicketId());
            } else {
                sb.append(obj);
            }
        }
        parameters.add(new Parameter("tickets", sb.toString()));
        OAuthUtils.addOAuthToken(parameters);

        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        // <uploader>
        //  <ticket id="128" complete="1" photoid="2995" />
        //  <ticket id="129" complete="0" />
        //  <ticket id="130" complete="2" />
        //  <ticket id="131" invalid="1" />
        // </uploader>

        List<Ticket> list = new ArrayList<Ticket>();
        JSONObject uploaderElement = response.getData().getJSONObject("uploader");
        JSONArray ticketNodes = uploaderElement.optJSONArray("ticket");
        for (int i = 0; ticketNodes != null && i < ticketNodes.length(); i++) {
            JSONObject ticketElement = ticketNodes.getJSONObject(i);
            String id = ticketElement.getString("id");
            String complete = ticketElement.getString("complete");
            boolean invalid = "1".equals(ticketElement.getString("invalid"));
            String photoId = ticketElement.getString("photoid");
            Ticket info = new Ticket();
            info.setTicketId(id);
            info.setInvalid(invalid);
            info.setStatus(Integer.parseInt(complete));
            info.setPhotoId(photoId);
            list.add(info);
        }
        return list;
    }

}
