package com.coderpakistan.learningbank.HelperClasses;

public class URLHelper {

    private static final String BASE_URL = "http://test.conversionsbpo.com/elearning/public/api/";
    public static final String BASE_IMAGE = "http://test.conversionsbpo.com/elearning/public/uploads/";
    public static final String REGISTER = BASE_URL + "user/register";
    public static final String LOGIN = BASE_URL + "user/login";
    public static final String REGISTER_SOCIAL = BASE_URL+"user/social_register";
    public static final String LOGIN_SOCIAL = BASE_URL+"user/social_login";
    public static final String UPDATE_PROFILE = BASE_URL+"user/update_profile";
    public static final String GET_LEARN_OPTIONS = BASE_URL+"categories";
    public static final String FEEDBACK = BASE_URL+"user/feedback";
    public static final String DIRECT_SESSION = BASE_URL+"sessions";
    public static final String DIRECT_LEVELS = BASE_URL+"levels";
    public static final String DIRECT_WORDS = BASE_URL+"words";
    public static final String DIRECT_FINISH = BASE_URL+"update/progress";
    public static final String SUB_CATEGORIES_VOCABULARY = BASE_URL+"sub_categories";
    public static final String GAME_SESSION = BASE_URL+"get/game_sessions";
    public static final String GAME_LEVELS = BASE_URL+"get/game_levels";
    public static final String GAME_QUIZ = BASE_URL+"game/questions";
    public static final String GAME_PROGRESS = BASE_URL+"game/progress";
    public static final String QUIZ_WEEKLY = BASE_URL+"quiz/weekly";
    public static final String QUIZ_MONTHLY = BASE_URL+"quiz/monthly";
    public static final String QUIZ_QUESTIONS = BASE_URL+"quiz/questions";
    public static final String QUIZ_PROGRESS_WEEKLY=BASE_URL+"weekly/progress";
    public static final String QUIZ_PROGRESS_MONTHLY=BASE_URL+"monthly/progress";
    public static final String GET_PRONOUNCATION = BASE_URL+"pronunciations";
}
