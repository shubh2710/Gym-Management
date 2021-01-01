package db_keys;

public class Keys {

	// TABLES
	public static final String TABLE_USER_DATA = "userData";
	public static final String TABLE_ADMIN_DATA = "admin_data";
	public static final String TABLE_OLD_BILLS_DATA = "old_bills";
	public static final String TABLE_ACTIVE_BILLS_DATA = "active_bills";
	public static final String TABLE_ATENDANCE_DATA = "atendance_table";
	public static final String TABLE_FINGER_PRINT_DATA = "finger_print_table";
	public static final String TABLE_PLAN_DATA = "plans_table";

	public static final String COMPANY_TABLE_NAME = "company_table";

	// TABLE_USER_DATA
	public static final String USER_ID = "user_id";
	public static final String USER_UID = "user_uid";
	public static final String USER_NAME = "user_name";
	public static final String USER_AGE = "user_age";
	public static final String USER_WEIGHT = "user_weight";
	public static final String USER_DOB = "user_dob";
	public static final String USER_PROFILE_URL = "user_profile_url";
	public static final String USER_REG_NO="user_reg_no";
	public static final String USER_JOINING_DATE = "user_joing_date";
	public static final String USER_EMAIL = "user_email";
	public static final String USER_ADDRESS = "user_address";
	public static final String USER_MOB_NO = "user_mob_no";
	public static final String USER_SELETED_PLAN = "user_seleted_plan";

	// Active_bills
	public static final String ACTIVE_BILL_ID = "active_bill_id";
	public static final String ACTIVE_BILL_BID = "active_bill_bid";
	public static final String ACTIVE_BILL_UID = "active_bill_uid";
	public static final String ACTIVE_BILL_SESSION_FROM = "active_bill_session_from";
	public static final String ACTIVE_BILL_SESSION_TO = "active_bill_session_to";
	public static final String ACTIVE_BILL_AMOUNT_PAID = "active_bill_paid";
	public static final String ACTIVE_BILL_AMOUNT_PENDING = "active_bill_amt_pending";

	// oldbill

	public static final String OLD_BILL_ID = "old_bill_id";
	public static final String OLD_BILL_BID = "old_bill_bid";
	public static final String OLD_BILL_UID = "old_bill_uid";
	public static final String OLD_BILL_SESSION_FROM = "old_bill_session_from";
	public static final String OLD_BILL_SESSION_TO = "old_bill_session_to";
	public static final String OLD_BILL_AMOUNT_PAID = "old_bill_paid";
	public static final String OLD_BILL_DATE = "old_bill_DATE";

	// admin_table
	public static final String ADMIN_ID = "admin_id";
	public static final String ADMIN_AID = "admin_aid";
	public static final String ADMIN_NAME = "admin_name";
	public static final String ADMIN_TYPE = "admin_type";
	public static final String ADMIN_USER_NAME = "admin_user_name";
	public static final String ADMIN_USER_PASSWORD = "admin_user_pwd";
	public static final String ADMIN_PROFILE_URL = "admin_profile_url";
	public static final String ADMIN_EMAIL = "admin_email";

	//Company table

	public static final String COMPANY_ID = "company_id";
	public static final String COMPANY_NAME = "company_name";
	public static final String COMPANY_EMAIL = "company_email";
	public static final String COMPANY_ADDRESS = "company_address";
	public static final String COMPANY_MOB = "company_mob";
	public static final String COMPANY_GSTNO = "company_gst";
	public static final String COMPANY_GSTPERSENTAGE = "company_gstpersent";
	// atendance_table
	public static final String ATENDANCE_ID = "atendance_id";
	public static final String ATENDANCE_AID = "atendance_AID";
	public static final String ATENDANCE_UID = "atendance_UID";
	public static final String ATENDANCE_DATE = "atendance_DATE";
	public static final String ATENDANCE_IS_PRESENT = "atendance_is_present";
	public static final String ATENDANCE_DAY = "atendance_day";
	public static final String ATENDANCE_MONTH = "atendance_month";
	public static final String ATENDANCE_YEAR = "atendance_year";

	// fnger print data
	public static final String FP_ID = "FP_id";
	public static final String FP_FID = "FP_fid";
	public static final String FP_UID = "FP_uid";
	public static final String FP_url = "FP_url";
	// plans table
	public static final String PLAN_ID = "plan_id";
	public static final String PLAN_PID = "plan_pid";
	public static final String PLAN_NAME = "plan_name";
	public static final String PLAN_DETAIL = "plan_details";
	public static final String PLAN_NO_OF_MONTHS = "plan_no_of_months";
	public static final String PLAN_AMOUNT = "plan_amount";

	// prefs boolean
 	public static final String PREF_IS_SETUP = "setup";
	public static final String PREF_REG_NO = "reg_no";
	public static final String PREF_IS_VALID = "ISValid";
	public static final String PREF_SAVE_PASSWORD = "save_pass";
	public static final String PREF_FRONT_SAVE_PASSWORD = "front_pass";
	// prefs string
	public static final String PREFS_USERNAME = "username";
	public static final String PREFS_LOGIN_ADMIN = "login_admin";
	public static final String PREFS_PASSWORd = "password";
	public static final String BACKIMAGE = "backimage";
	public static final String LOGO = "logo";
	public static final String FRONT_PREFS_USERNAME = "font_username";
	public static final String FRONT_PREFS_PASSWORd = "FRONT-passowrd";
	public static final String PREFS_SMS_SERVICE = "sms_service";
	public static final String PREFS_SMS_NAME="SMS_name";
	public static final String PREFS_SMS_SID="sms_sid";
	public static final String PREFS_SMS_TOKEN="sms_tokoen";
	public static final String PREFS_COLOR_TOP="colort";
	public static final String PREFS_COLOR_CENTER="colorc";
	public static final String PREFS_COLOR_OTHER="coloro";
}
