/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.googlecode.flickrjandroid.photos;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;

import com.googlecode.flickrjandroid.util.StringUtilities;

/**
 * @author Anthony Eden
 */
public class Note {
	public static final long serialVersionUID = 12L;
    private String id;
    private String author;
    private String authorName;
    private Rectangle bounds;
    private String text;

    public Note() {

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

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void setBounds(String x, String y, String w, String h) {
        int xi = Integer.parseInt(x);
        int yi = Integer.parseInt(y);
        int wi = Integer.parseInt(w);
        int hi = Integer.parseInt(h);
        setBounds(new Rectangle(xi, yi, wi, hi));
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

	@Override
	public boolean equals(Object obj) {
        if ((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }
		// object must be Note at this point
        Note test = (Note) obj;
        Class<?> cl = this.getClass();
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
                            //System.out.println("Note class: " + method[i].getName());
                            if (!res.equals(resTest)) return false;
                        } else {
                            //return false;
                        }
                    } else if (retType.equals("int")) {
                        if (!((Integer) res).equals(((Integer)resTest))) return false;
                    } else if (retType.equals("boolean")) {
                        if (!((Boolean) res).equals(((Boolean)resTest))) return false;
                    } else {
                        System.out.println(method[i].getName() + "|" +
                            method[i].getReturnType().toString());
                    }
                } catch (IllegalAccessException ex) {
                    System.out.println("equals " + method[i].getName() + " " + ex);
                } catch (InvocationTargetException ex) {
                    //System.out.println("equals " + method[i].getName() + " " + ex);
                } catch (Exception ex) {
                    System.out.println("equals " + method[i].getName() + " " + ex);
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        if(id != null) hash += id.hashCode();
        if(author != null) hash += author.hashCode();
        if(authorName != null) hash += authorName.hashCode();
        if(text != null) hash += text.hashCode();
        return hash;
    }
}
