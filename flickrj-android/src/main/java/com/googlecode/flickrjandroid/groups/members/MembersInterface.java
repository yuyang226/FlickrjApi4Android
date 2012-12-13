package com.googlecode.flickrjandroid.groups.members;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.Parameter;
import com.googlecode.flickrjandroid.Response;
import com.googlecode.flickrjandroid.Transport;
import com.googlecode.flickrjandroid.oauth.OAuthInterface;
import com.googlecode.flickrjandroid.oauth.OAuthUtils;
import com.googlecode.flickrjandroid.util.StringUtilities;

/**
 * Members Interface.
 *
 * @author mago
 * @version $Id: MembersInterface.java,v 1.1 2009/06/21 19:55:15 x-mago Exp $
 */
public class MembersInterface {
    public static final String METHOD_GET_LIST = "flickr.groups.members.getList";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public MembersInterface(
        String apiKey,
        String sharedSecret,
        Transport transportAPI
    ) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Get a list of the members of a group.
     * The call must be signed on behalf of a Flickr member,
     * and the ability to see the group membership will be
     * determined by the Flickr member's group privileges.
     *
     * @param groupId Return a list of members for this group. The group must be viewable by the Flickr member on whose behalf the API call is made.
     * @param memberTypes A set of Membertypes as available as constants in {@link Member}.
     * @param perPage Number of records per page.
     * @param page Result-section.
     * @return A members-list
     * @throws FlickrException
     * @throws IOException
     * @throws JSONException 
     * @see <a href="http://www.flickr.com/services/api/flickr.groups.members.getList.html">API Documentation</a>
     */
    public MembersList getList(String groupId, Set<String> memberTypes, int perPage, int page)
      throws FlickrException, IOException, JSONException {
        MembersList members = new MembersList();
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_LIST));
        parameters.add(new Parameter(OAuthInterface.PARAM_OAUTH_CONSUMER_KEY, apiKey));
        parameters.add(new Parameter("group_id", groupId));

        if (perPage > 0) {
            parameters.add(new Parameter("per_page", "" + perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", "" + page));
        }
        if (memberTypes != null) {
            parameters.add(
                new Parameter(
                    "membertypes",
                    StringUtilities.join(memberTypes, ",")
                )
            );
        }
        OAuthUtils.addOAuthToken(parameters);
        
        Response response = transportAPI.postJSON(sharedSecret, parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        JSONObject mElement = response.getData().getJSONObject("members");
        members.setPage(mElement.getInt("page"));
        members.setPages(mElement.getInt("pages"));
        members.setPerPage(mElement.getInt("perpage"));
        members.setTotal(mElement.getInt("total"));

        JSONArray mNodes = mElement.optJSONArray("member");
        for (int i = 0; mNodes != null && i < mNodes.length(); i++) {
            JSONObject element = mNodes.getJSONObject(i);
            members.add(parseMember(element));
        }
        return members;
    }

    private Member parseMember(JSONObject mElement) throws JSONException {
        Member member = new Member();
        member.setId(mElement.getString("nsid"));
        member.setUserName(mElement.getString("username"));
        member.setIconServer(mElement.getString("iconserver"));
        member.setIconFarm(mElement.getString("iconfarm"));
        member.setMemberType(mElement.getString("membertype"));
        return member;
    }
}
