package com.adslate.utils;

/**
 * Created by pooja.b on 18-11-2015.
 */
public class  StaticUtils
{
    //public static String  SPACE_TYPE = "http://192.168.0.66/adslateWS/webservice/getSpaceType.php?";
    //public static String ORG_TYPE = "http://192.168.0.67/adslateWS/webservice/getOrgType.php?";


    // public static String BASE_URL1 = "http://mavegomobile.com/dev/AdSlate/webservice1/";


    public static String BASE_URL = "http://mavegomobile.com/dev/AdSlate/webservice/";

    public static String REGISTER = BASE_URL+"register1.php?";

    public static String LOGIN_URL = BASE_URL+"login.php?";
    public static String VALIDATE_OTP = BASE_URL+"verifyOTP.php?";
    public static String RESEND_OTP = BASE_URL+"resendOTP.php?";
    public static String SPACE_TYPE = BASE_URL+"getSpaceType.php?";
    public static String ORG_TYPE = BASE_URL+"getOrgType.php?";



    public static String CHECK_DETAILS = "Some fields might be missing";
    public static String CHECK_DETAILS_FULLNAME = "Full name fields might be missing";
    public static String CHECK_DETAILS_EMAIL = "Email fields might be missing";
    public static String CHECK_DETAILS_PASSWORD = "Password fields might be missing";
    public static String CHECK_DETAILS_DOB = "Date of Birth fields might be missing";
    public static String CHECK_DETAILS_PH = "Phone number fields might be missing";
    public static String CHECK_DETAILS_ORG = "Organization type field is mandatory";
    public static String CHECK_DETAILS_ORG_DESC = "Organization description field is mandatory";
    public static String CHECK_DETAILS_CONTPERSON_NAME =   "Contact PersonName field is mandatory";
    public static String CHECK_DETAILS_CONTPERSON_DESG =  "Contact PersonDesignation field is mandatory";
    public static String CHECK_DETAILS_CONTPERSON_PH =  "Contact PersonPhoneNumber field is mandatory";
    public static String CHECK_DETAILS_CONTPERSON_ODRORG =  "Organization type others field is mandatory";
    public static String EMAIL_ADDRESS = "Email Address entered was invalid, please check.";
    public static String ORG_TYPE_MSG = "Organization Type Others Field was found empty";
    public static String NO_INTERNET_TITLE = "Internet Settings.";
    public static String NO_INTERNET_MESSAGE = "Unable to connect to internet, please check your internet settings.";
    public static String ERROR_OCCURRED = "There was an error connecting to server, try after a while.";
    public static String ENTER_ALL_MANDATORY_FIELDS = "You have missed entering one of the mandatory fields.";
    public static String PROFILE_PICTURE = "You have missed uploading your profile picture.";
    public static String LOGOUT_TITLE = "Logout ?";
    public static String LOGOUT_MESSAGE = "Do you want to Logout?";
    public static String TERMS_CONDITION = "Terms and Conditions are not accepted";
    public static String GCM_REGISTRATION_ERROR = "Error while registering your device. Please try after a while";
    public static String DEVICE_NOT_SUPPORTED = "Sorry, your phone isn't supported.";
    public static String GET_OTP = "Get OTP Before verifying";
    public static String CHOOSE_USER_TYPE = "Please choose a user type by tapping on it";
    public static String NO_EMAIL_ID = "The account has no email address mentioned, hence login was denied";


    public static String DEVICE_TYPE = "A";
    public static String ORG_TYPENAME_OTHERS = "Others";


    public final static boolean isValidEmail(CharSequence target)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
