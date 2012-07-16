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
public class Editability {
	public static final long serialVersionUID = 12L;

    private boolean comment;
    private boolean addmeta;

    public Editability() {

    }

    public boolean isComment() {
        return comment;
    }

    public void setComment(boolean comment) {
        this.comment = comment;
    }

    public boolean isAddmeta() {
        return addmeta;
    }

    public void setAddmeta(boolean addmeta) {
        this.addmeta = addmeta;
    }

	@Override
	public boolean equals(Object obj) {
        if ((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }
		// object must be Editability at this point
        Editability test = (Editability) obj;
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
        hash += new Boolean(comment).hashCode();
        hash += new Boolean(addmeta).hashCode();
        return hash;
    }

}
