package com.gmail.yuyang226.flickr.groups;

import com.gmail.yuyang226.flickr.SearchResultList;

public class GroupList extends SearchResultList<Group> {
    private static final long serialVersionUID = 3344960036515265775L;

    public Group [] getGroupsArray() {
        return (Group[]) toArray(new Group[size()]);
    }

    public boolean add(Group obj) {
        // forces type to be group. Otherwise a class cast exception is thrown
        return super.add(obj);
    }

}
