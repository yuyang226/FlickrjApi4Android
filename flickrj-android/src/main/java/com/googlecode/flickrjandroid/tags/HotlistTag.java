package com.googlecode.flickrjandroid.tags;

/**
 *
 * @author mago
 * @version $Id: HotlistTag.java,v 1.2 2009/07/12 22:43:07 x-mago Exp $
 */
public class HotlistTag {
    private static final long serialVersionUID = 12L;

    private String value;
    private int score = 0;

    public HotlistTag() {

    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setScore(String score) {
        setScore(Integer.parseInt(score));
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "HotlistTag [value=" + value + ", score=" + score + "]";
    }
    
}
