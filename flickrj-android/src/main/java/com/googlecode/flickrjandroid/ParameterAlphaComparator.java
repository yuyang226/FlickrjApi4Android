/*
 * Copyright (c) 2005 Aetrion LLC.
 */

package com.googlecode.flickrjandroid;

import java.util.Comparator;

/**
 * Compare Parameter sorting on the key in alphabetical order.
 *
 * @author Anthony Eden
 */
public class ParameterAlphaComparator implements Comparator<Parameter> {

    /**
     * Compare the two objects
     *
     * @param p1 The first parameter
     * @param p2 The second parameter
     * @return The comparison results
     */
    public int compare(Parameter p1, Parameter p2) {
        return p1.getName().compareTo(p2.getName());
    }
}
