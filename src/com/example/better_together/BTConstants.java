package com.example.better_together;

/**
 * Created by ssdd on 10/21/14.
 */
public class BTConstants {
    public static final String SHARED_PREF_KEY_ACCESS_TOKEN = "access_token";
    public static final String SHARED_PREF_KEY_GROUPS = "GROUPS";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PROFILE_PICTURE_PATH = "profile_pic_path";
    public static final String SHARED_PREF_FILE_NAME = "better_together_shared_pref";
    public static final String JSON_ATTR_DATA = "data";
    public static final String JSON_ATTR_USERNAME = "username";
    public static final String JSON_ATTR_USER_FULL_NAME = "full_name";
    public static final String JSON_ATTR_USER_BIO = "bio";
    public static final String JSON_ATTR_USER_WEBSITE = "website";
    public static final String JSON_ATTR_USER_PROFILE_PIC_URL = "profile_picture";
    public static final String JSON_ATTR_USER_ID = "id";
    public static final String JSON_ATTR_USER_GROUP_NAME = "group_name";
    public static final String JSON_ATTR_IMAGES = "images";
    public static final String JSON_ATTR_STANDARD_RESOLUTION = "standard_resolution";
    public static final String JSON_ATTR_LOW_RESOLUTION = "low_resolution";
    public static final String JSON_ATTR_URL = "url";
    public static final String JSON_ATTR_CAPTION = "caption";
    public static final String JSON_ATTR_CAPTION_TEXT = "text";
    public static final String JSON_ATTR_CREATED_TIME = "created_time";

    public static final String USER_PHOTO_DATE_FORMAT = "MM/dd/yy HH:mm:ss";

    public static final String HTTP_STATUS_CODE = "status_code";


    public static final int SEARCH_USERS_RESULTS_COUNT = 10;
    public static final String INTENT_EXTRA_SEARCH_MORE_USERS = "search_more_users";
    public static final String SHARED_PREF_KEY_PROFILE_PICS_MAPPING = "profile_pictures_mapping";
    public static final String PROFILE_PICTURES_DIRECTORY = "profilePictures";

    public static final int MESSAGE_SEARCH_USER_NO_RESULTS_FOUND = 0;
    public static final int MESSAGE_FOUND_RESULTS = 1;
    public static final int MESSAGE_NO_PROFILE_PIC_FROM_URL_FOUND = 2;
    public static final int MESSAGE_FOUND_PROFILE_PIC_FROM_URL = 3;
    public static final int MESSAGE_FOUND_PROFILE_PIC_FROM_MEMORY = 4;
    public static final int MESSAGE_FOUND_USER_RECENT_MEDIA = 5;
    public static final int MESSAGE_FOUND_USER_PIC_FROM_URL = 6;

    public static final int NUM_OF_PHOTOS_PER_USER = 5;

    public static String sAppProfilePicDirectoryPrefixPath;

    public static void setAppProfilePicDirectoryPrefix(String path){
        sAppProfilePicDirectoryPrefixPath = path;
    }
}
