/**
 * 
 */
package com.googlecode.flickrjandroid.people;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class Bandwidth {
    private long max;
    private long used;
    private long maxBytes;
    private long usedBytes;
    private long remainingBytes;
    private long maxKb;
    private long usedKb;
    private long remainingKb;
    private boolean isUnlimited;
    
    
    /**
     * 
     */
    public Bandwidth() {
        super();
    }


    /**
     * @return the max
     */
    public long getMax() {
        return max;
    }


    /**
     * @param max the max to set
     */
    public void setMax(long max) {
        this.max = max;
    }


    /**
     * @return the used
     */
    public long getUsed() {
        return used;
    }


    /**
     * @param used the used to set
     */
    public void setUsed(long used) {
        this.used = used;
    }


    /**
     * @return the maxBytes
     */
    public long getMaxBytes() {
        return maxBytes;
    }


    /**
     * @param maxBytes the maxBytes to set
     */
    public void setMaxBytes(long maxBytes) {
        this.maxBytes = maxBytes;
    }


    /**
     * @return the usedBytes
     */
    public long getUsedBytes() {
        return usedBytes;
    }


    /**
     * @param usedBytes the usedBytes to set
     */
    public void setUsedBytes(long usedBytes) {
        this.usedBytes = usedBytes;
    }


    /**
     * @return the remainingBytes
     */
    public long getRemainingBytes() {
        return remainingBytes;
    }


    /**
     * @param remainingBytes the remainingBytes to set
     */
    public void setRemainingBytes(long remainingBytes) {
        this.remainingBytes = remainingBytes;
    }


    /**
     * @return the maxKb
     */
    public long getMaxKb() {
        return maxKb;
    }


    /**
     * @param maxKb the maxKb to set
     */
    public void setMaxKb(long maxKb) {
        this.maxKb = maxKb;
    }


    /**
     * @return the usedKb
     */
    public long getUsedKb() {
        return usedKb;
    }


    /**
     * @param usedKb the usedKb to set
     */
    public void setUsedKb(long usedKb) {
        this.usedKb = usedKb;
    }


    /**
     * @return the remainingKb
     */
    public long getRemainingKb() {
        return remainingKb;
    }


    /**
     * @param remainingKb the remainingKb to set
     */
    public void setRemainingKb(long remainingKb) {
        this.remainingKb = remainingKb;
    }


    /**
     * @return the isUnlimited
     */
    public boolean isUnlimited() {
        return isUnlimited;
    }


    /**
     * @param isUnlimited the isUnlimited to set
     */
    public void setUnlimited(boolean isUnlimited) {
        this.isUnlimited = isUnlimited;
    }
    
    

}
