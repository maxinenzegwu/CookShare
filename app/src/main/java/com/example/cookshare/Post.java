package com.example.cookshare;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

@Parcel(analyze = Post.class)
@ParseClassName("Post")
public class Post extends ParseObject {

    // no-arg, empty constructor required for Parceler
    public Post(){}

    //define keys
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_RECIPE = "recipe";

    //define getters and setters for each key
    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public String getRecipe(){
        return getString(KEY_RECIPE);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public void setImage(ParseFile parsefile){
        put(KEY_IMAGE, parsefile);
    }

    public void setUser(ParseUser parseUser){
        put(KEY_USER, parseUser);
    }

    public void setRecipe(String recipe){
        put(KEY_RECIPE, recipe);
    }

}
