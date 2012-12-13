/**
 * 
 */
package com.googlecode.flickrjandroid.stats;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public final class StatsUtils {

    /**
     * 
     */
    private StatsUtils() {
        super();
    }
    
    public static Stats createStats(JSONObject responseData) throws JSONException {
        JSONObject statsObject = responseData.getJSONObject("stats");
        Stats stats = new Stats();
        stats.setComments(statsObject.optInt("comments"));
        stats.setViews(statsObject.optInt("views"));
        stats.setFavorites(statsObject.optInt("favorites"));
        return stats;
    }
    
    public static ReferrerList createReferrerList(JSONObject data) throws JSONException {
        ReferrerList referrers = new ReferrerList();
        referrers.setPerPage(data.getInt("perpage"));
        referrers.setPages(data.getInt("pages"));
        referrers.setPage(data.getInt("page"));
        referrers.setTotal(data.getInt("total"));
        referrers.setName(data.optString("name"));
        JSONArray referrerArray = data.optJSONArray("referrer");
        for (int i = 0; referrerArray != null && i < referrerArray.length(); i++) {
            referrers.add(createReferrer(referrerArray.getJSONObject(i)));
        }
        return referrers;
    }
    
    public static Referrer createReferrer(JSONObject data) throws JSONException {
        Referrer referrer = new Referrer();
        String url = data.optString("url", null);
        if (url != null) {
            try {
                referrer.setUrl(new URL(url));
            } catch (MalformedURLException e) {
            }
        }
        referrer.setViews(data.getInt("views"));
        referrer.setSearchterm(data.optString("searchterm"));
        return referrer;
    }
    
    public static DomainList createDomainList(JSONObject data) throws JSONException {
        DomainList domains = new DomainList();
        domains.setPerPage(data.getInt("perpage"));
        domains.setPages(data.getInt("pages"));
        domains.setPage(data.getInt("page"));
        domains.setTotal(data.getInt("total"));
        JSONArray domainArray = data.optJSONArray("domain");
        for (int i = 0; domainArray != null && i < domainArray.length(); i++) {
            domains.add(createDomain(domainArray.getJSONObject(i)));
        }
        return domains;
    }
    
    public static Domain createDomain(JSONObject data) throws JSONException {
        Domain domain = new Domain();
        domain.setName(data.getString("name"));
        domain.setViews(data.getInt("views"));
        return domain;
    }

}
