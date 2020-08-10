Original App Design Project - README 
===

# Cook Share

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
This is a mobile app that allows beginners to learn how to cook and experienced cooks to explore new recipes or share their own. Users can track their progress by sharing photos on what recipes they created or recreated on their own.

### App Evaluation

- **Category:**
Social / Food & Drink
- **Mobile:**
Mobile devices only
- **Story:**
Allows users to create and share their recipes with pictures or videos and find new recipe ideas based on their own ingredients
- **Market:**
Anyone who takes an interesting in cooking, learning to cook, or sharing their own recipes can find this app interesting and useful
- **Habit:**
Users can post throughout the day many times depending on how many meals they would like to cook. Users can explore endless recipes on their feed based on what others have posted.
- **Scope:**
Simple platform that allows people to connect through their interest in cooking and supporting one another through a "like" feature

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User can register a new account
* User can login and logout
* User can share photos to timeline feed
* Users can like photos on all feeds (including their timeline, profile page, or trending feed)
* Users can see likes on all posts
* User can navigate between which feed they want to view
* User can create a new post and add a picture using the camera
* User can pull to refresh to update whatever feed they are in
* User can have a filter that only shows liked photos
* User can tap a post to view the recipe to it
* User can add a profile picture
* Users can search for recipes from any feed
* User can login through facebook
* User can double tap to like a photo


**Optional Nice-to-have Stories**

* User can login through facebook
* Usable in landscape mode


### 2. Screen Archetypes

* Login Screen
   * User can log in with their account information
* Registration Screen
   * User can register a new acccount
* Stream
    * User can view shared photos on their feed
* Creation
    * User can post their own photo to their feed
* Search
    * User can search for recipes
* Saved
    * User can have a feed of their liked photos

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Home Feed
* Liked Photos
* User Photos
* Most Popular

**Flow Navigation** (Screen to Screen)

* Registration Screen
    * Login Screen
* Logout Screen
   * Home Screen
* Search
   * Home Screen
* Create
    * Home Screen

## Wireframes

![](https://i.imgur.com/QJJAlt8.jpg)

### Milestones
 *  MS0:
 [x] Setup Parse and Heroku
 [x] Create layouts for all fragments and activities
 [x] Handle all tab navigations and flow to activities

*  MS1:
 [x] Finish all recyclerviews
 [x] Allow pull to refresh for posts
 [x] Add functionality for liking/saving posts

*  MS2:
 [x] Add searching algorithm
    - sort through and find closest match
 [x] Incorporate SDK
    - login through facebook or google
    - use either facebook or google SDK
 [x] Click a post to view the recipe for it

* Nice to haves:
 - Add more accuracy to searching
 - Usable in landscape mode
## Schema
