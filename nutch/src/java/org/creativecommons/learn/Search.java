package org.creativecommons.learn;

public class Search {

    public static String TAGS_INDEX_FIELD = "_dct_subject";
    public static String TAGS_QUERY_FIELD = "tag";
    public static float  TAGS_BOOST = 2.0f;

    public static String CURATOR_INDEX_FIELD = "_ccl_hasCurator";
    public static String CURATOR_QUERY_FIELD = "curator";
    public static float  CURATOR_BOOST = 1.0f;
    
    public static String FEED_FIELD = "feed";
    public static float  FEED_BOOST = 1.0f;
    
    public static String ED_LEVEL_FIELD = "_ccl_educationLevel";
    public static float ED_LEVEL_BOOST = 1.0f;
    
    public static String LANGUAGE_FIELD = "_dct_language";
    public static float LANGUAGE_BOOST = 1.0f;
    
	// public static String CURATOR_NAME_FIELD = "curator_name";
    public static String LICENSE_FIELD = "license";

} // Search
