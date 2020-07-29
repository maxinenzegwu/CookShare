package com.example.cookshare;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("User")
    public class User extends ParseObject {

        // no-arg, empty constructor required for Parceler
        public User() {
        }

        //define keys
        public static final String KEY_OBJECTID = "objectId";
        public static final String KEY_PICTURE = "picture";

        //define getters and setters for each key

        public ParseFile getPicture() {
            return getParseFile(KEY_PICTURE);
        }

        public String getObject() {
            return getString(KEY_OBJECTID);
        }


        public void setPicture(ParseFile parsefile) {
            put(KEY_PICTURE, parsefile);
        }






    }



