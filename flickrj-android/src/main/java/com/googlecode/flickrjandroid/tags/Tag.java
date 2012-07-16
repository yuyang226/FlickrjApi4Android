/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.googlecode.flickrjandroid.tags;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;

import com.googlecode.flickrjandroid.util.StringUtilities;

/**
 * @author Anthony Eden
 */
public class Tag {
	private static final long serialVersionUID = 12L;

    private String id;
    private String author;
    private String authorName;
    private String raw;
    private String value;
    private int count;

    public Tag() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setCount(String count) {
        setCount(Integer.parseInt(count));
    }

	@Override
	public boolean equals(Object obj) {
        if ((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }
		// object must be GeoData at this point
        Tag test = (Tag) obj;
        Class<? extends Tag> cl = this.getClass();
        Method[] method = cl.getMethods();
        for (int i = 0; i < method.length; i++) {
            Matcher m = StringUtilities.getterPattern.matcher(method[i].getName());
            if (m.find() && !method[i].getName().equals("getClass")) {
                try {
                    Object res = method[i].invoke(this, (Object[])null);
                    Object resTest = method[i].invoke(test, (Object[])null);
                    String retType = method[i].getReturnType().toString();
                    if (retType.indexOf("class") == 0) {
                        if (res != null && resTest != null) {
                            if (!res.equals(resTest)) return false;
                        } else {
                            //return false;
                        }
                    } else if (retType.equals("int")) {
                        if (!((Integer) res).equals(((Integer)resTest))) return false;
                    } else {
                        System.out.println(method[i].getName() + "|" +
                            method[i].getReturnType().toString());
                    }
                } catch (IllegalAccessException ex) {
                    System.out.println("Size equals " + method[i].getName() + " " + ex);
                } catch (InvocationTargetException ex) {
                    //System.out.println("equals " + method[i].getName() + " " + ex);
                } catch (Exception ex) {
                    System.out.println("Size equals " + method[i].getName() + " " + ex);
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash += new Integer(count).hashCode();
        if (value != null) hash += value.hashCode();
        if (raw != null) hash += raw.hashCode();
        if (author != null) hash += author.hashCode();
        if (authorName != null) hash += authorName.hashCode();
        if (id != null) hash += id.hashCode();
        return hash;
    }
}
