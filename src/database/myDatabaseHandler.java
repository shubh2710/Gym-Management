package database;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.prefs.Preferences;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import db_keys.Keys;
import extra_classes.genrateUniqueID;
import models.ActiveBillModel;
import models.AttendanceModel;
import models.CompanyDetailModel;
import models.OldBillModel;
import models.PlansModel;
import models.User;
import models.admin;
import preferences.getPrefsSingletan;

public class myDatabaseHandler {

	private Connection connection = null;

	public Statement getStatement() {
		Statement statement = null;
		try {
			String path = "C://gym_manngment//database";
			try {
				Files.createDirectories(Paths.get(path));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			connection = DriverManager.getConnection("jdbc:sqlite:C://gym_manngment//database//managment.db");
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			// createDb();
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, new JLabel("Cant connect to database"));
		}
		return statement;
	}
	
	public Statement getStatement(String path) {
		Statement statement = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:"+path);
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			// createDb();
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, new JLabel("Cant connect to database"));
		}
		return statement;
	}
public Connection getConnection(){
	return this.connection;
}
	public boolean checkTable(String tableName,Connection c) {
		
		boolean isExist = false;
	
		try {
			//c = DriverManager.getConnection("jdbc:sqlite:C://gym_manngment//database//managment.db");
			DatabaseMetaData md = c.getMetaData();
			ResultSet rs = md.getTables(null, null, "%", null);
			while (rs.next()) {
				if (rs.getString(3).equals(tableName)) {
					isExist = true;
					System.out.println(rs.getString(3));
				}
			}
			} catch (SQLException e) {
				isExist = false;
				e.printStackTrace();
			}

		return isExist;
	}

	public void createDb(Statement statement){
		try {
			createUserTable(statement);
			createUserActiveBills(statement);
			createOldBills(statement);
			createAdminTable(statement);
			createAtendanceTable(statement);
			createPlansTable(statement);
			createCompanyTable(statement);
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// connection close failed.
				System.err.println(e);
			}
		}
	}

	public void createCompanyTable(Statement  statement){

		try {
			statement.executeUpdate("drop table if exists " + Keys.COMPANY_TABLE_NAME);
			statement.executeUpdate("create table " + Keys.COMPANY_TABLE_NAME + " (" 
					+ Keys.COMPANY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT," 
					+ Keys.COMPANY_NAME + " string," 
					+ Keys.COMPANY_ADDRESS + " string,"
					+ Keys.COMPANY_EMAIL + " string,"
					+ Keys.COMPANY_MOB + " string,"
					+ Keys.COMPANY_GSTPERSENTAGE + " integer,"
					+ Keys.COMPANY_GSTNO + " string)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	public void createPlansTable(Statement statement) {
		// TODO Auto-generated method stub

		try {
			statement.executeUpdate("drop table if exists " + Keys.TABLE_PLAN_DATA);
			statement.executeUpdate("create table " + Keys.TABLE_PLAN_DATA + " (" 
					+ Keys.PLAN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT," 
					+ Keys.PLAN_PID + " string," 
					+ Keys.PLAN_NAME + " string,"
					+ Keys.PLAN_DETAIL + " string,"
					+ Keys.PLAN_AMOUNT + " integer," 
					+ Keys.PLAN_NO_OF_MONTHS + " integer)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void createFPTable(Statement statement) {
		// TODO Auto-generated method stub
		try {
			statement.executeUpdate("drop table if exists " + Keys.TABLE_FINGER_PRINT_DATA);
			statement.executeUpdate("create table " + Keys.TABLE_FINGER_PRINT_DATA + " (" + Keys.FP_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Keys.FP_FID + " string," + Keys.FP_UID + " string,"
					+ Keys.FP_url + " string)");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createAtendanceTable(Statement statement) {
		// TODO Auto-generated method stub
		try {
			statement.executeUpdate("drop table if exists " + Keys.TABLE_ATENDANCE_DATA);

			statement.executeUpdate("create table "
			+ Keys.TABLE_ATENDANCE_DATA + " ("
					+ Keys.ATENDANCE_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ Keys.ATENDANCE_AID + " string,"
					+ Keys.ATENDANCE_DATE+ " long," 
					+ Keys.ATENDANCE_UID + " string," 
					+ Keys.ATENDANCE_DAY + " integer," 
					+ Keys.ATENDANCE_MONTH + " integer," 
					+ Keys.ATENDANCE_YEAR + " integer," 
					+ Keys.ATENDANCE_IS_PRESENT + " integer)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean createAdminTable(Statement statement) {
		// TODO Auto-generated method stub
		boolean isAdded = false;
		try {
			statement.executeUpdate("drop table if exists " + Keys.TABLE_ADMIN_DATA);
			statement.executeUpdate("create table " + Keys.TABLE_ADMIN_DATA + " (" 
			+ Keys.ADMIN_ID	+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ Keys.ADMIN_AID + " string UNIQUE NOT NULL,"
			+ Keys.ADMIN_EMAIL + " string UNIQUE NOT NULL,"
			+ Keys.ADMIN_NAME + " string,"
			+ Keys.ADMIN_TYPE + " int,"
			+ Keys.ADMIN_PROFILE_URL + " string,"
			+ Keys.ADMIN_USER_NAME + " string,"
			+ Keys.ADMIN_USER_PASSWORD	+ " string)"

			);
			isAdded = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			isAdded = false;
			e.printStackTrace();
		}
		return isAdded;

	}

	public void createOldBills(Statement statement) {
		try {
			statement.executeUpdate("drop table if exists " + Keys.TABLE_OLD_BILLS_DATA);

			statement.executeUpdate("create table " + Keys.TABLE_OLD_BILLS_DATA + " (" + Keys.OLD_BILL_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Keys.OLD_BILL_BID + " string ,"
					+ Keys.OLD_BILL_DATE + " long," + Keys.OLD_BILL_SESSION_FROM + " long," + Keys.OLD_BILL_SESSION_TO
					+ " long," + Keys.OLD_BILL_UID + " string," + Keys.OLD_BILL_AMOUNT_PAID + " integer)");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void createUserActiveBills(Statement statement) {
		// TODO Auto-generated method stub
		try {
			statement.executeUpdate("drop table if exists  " + Keys.TABLE_ACTIVE_BILLS_DATA);
			statement.executeUpdate("create table " + Keys.TABLE_ACTIVE_BILLS_DATA + " (" + Keys.ACTIVE_BILL_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Keys.ACTIVE_BILL_BID + " string UNIQUE NOT NULL,"
					+ Keys.ACTIVE_BILL_UID + " string," + Keys.ACTIVE_BILL_SESSION_FROM + " long,"
					+ Keys.ACTIVE_BILL_SESSION_TO + " long," + Keys.ACTIVE_BILL_AMOUNT_PAID + " integer,"
					+ Keys.ACTIVE_BILL_AMOUNT_PENDING + " integer)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void createUserTable(Statement statement) {
		// TODO Auto-generated method stub
		try {
			statement.executeUpdate("drop table if exists " + Keys.TABLE_USER_DATA);
			statement.executeUpdate(
					"create table " + Keys.TABLE_USER_DATA + " (" 
							+ Keys.USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
							+ Keys.USER_NAME + " string,"
							+ Keys.USER_UID + " string,"
							+ Keys.USER_AGE + " integer,"
							+ Keys.USER_ADDRESS + " string,"
							+ Keys.USER_DOB + " string,"
							+ Keys.USER_EMAIL + " string  UNIQUE NOT NULL,"
							+ Keys.USER_REG_NO + " integer  UNIQUE NOT NULL,"
							+ Keys.USER_JOINING_DATE + " long,"
							+ Keys.USER_MOB_NO + " string," 
							+ Keys.USER_WEIGHT+ " integer," 
							+ Keys.USER_PROFILE_URL + " string,"
							+ Keys.USER_SELETED_PLAN + " string)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// get data
	public admin getAdminAuth(String username, String password, Statement statement) {
		admin a = null;
		String sql = "select * from " + Keys.TABLE_ADMIN_DATA + " where " + Keys.ADMIN_USER_NAME + " = '" + username
				+ "' and " + Keys.ADMIN_USER_PASSWORD + " = '" + password + "';";
		System.out.println(sql);
		try {
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				a = new admin(rs.getString(Keys.ADMIN_AID), rs.getString(Keys.ADMIN_NAME),
						rs.getString(Keys.ADMIN_EMAIL), rs.getString(Keys.ADMIN_USER_PASSWORD),
						rs.getString(Keys.ADMIN_USER_NAME),
						rs.getString(Keys.ADMIN_PROFILE_URL),
						rs.getInt(Keys.ADMIN_TYPE));
			}
		} catch (SQLException e) {
		}
		return a;
	}

	public boolean addAdminData(String uid, String email, String name, String username, String password, String profile,int type,
			Statement statment) {
		boolean isSuccess;
		String sql = "insert into " + Keys.TABLE_ADMIN_DATA + "(" + Keys.ADMIN_AID + "," + Keys.ADMIN_EMAIL + ","
				+ Keys.ADMIN_USER_NAME + "," + Keys.ADMIN_USER_PASSWORD + "," + Keys.ADMIN_NAME + ","
				+ Keys.ADMIN_PROFILE_URL +","+Keys.ADMIN_TYPE+ ")" + " values ('" + uid + "','" + email + "','" + username + "','"
				+ password + "','" + name + "','" + profile + "',"+type+");";
		try {
			statment.executeUpdate(sql);
			isSuccess = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			isSuccess = false;
			e.printStackTrace();
		}
		System.out.println(sql);
		return isSuccess;
	}
	public admin getAdminData(String aid, Statement statement) {
		admin a = null;
		String sql = "select * from " + Keys.TABLE_ADMIN_DATA + " where " + Keys.ADMIN_AID + " = '" + aid + "';";
		System.out.println(sql);
		try {
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				a = new admin(rs.getString(Keys.ADMIN_AID), rs.getString(Keys.ADMIN_NAME),
						rs.getString(Keys.ADMIN_EMAIL), rs.getString(Keys.ADMIN_USER_PASSWORD),
						rs.getString(Keys.ADMIN_USER_NAME), rs.getString(Keys.ADMIN_PROFILE_URL),rs.getInt(Keys.ADMIN_TYPE));
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return a;
	}

	public admin getAdminData(Statement statement) {
		admin a = null;
		String sql = "select * from " + Keys.TABLE_ADMIN_DATA + ";";
		System.out.println(sql);
		try {
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				a = new admin(rs.getString(Keys.ADMIN_AID), rs.getString(Keys.ADMIN_NAME),
						rs.getString(Keys.ADMIN_EMAIL), rs.getString(Keys.ADMIN_USER_PASSWORD),
						rs.getString(Keys.ADMIN_USER_NAME), rs.getString(Keys.ADMIN_PROFILE_URL),rs.getInt(Keys.ADMIN_TYPE));
				System.out.println(a.getAid());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}

	public User getUserData() {
		User u = null;
		return u;
	}

	public boolean addUser(User u, Statement statement) {
		boolean isAdded = false;
		String sql = "insert into " 
				+ Keys.TABLE_USER_DATA + "(" 
				+ Keys.USER_UID + "," 
				+ Keys.USER_EMAIL + ","
				+ Keys.USER_NAME + ","
				+ Keys.USER_AGE + ","
				+ Keys.USER_DOB + "," 
				+ Keys.USER_MOB_NO + ","
				+ Keys.USER_WEIGHT + "," 
				+ Keys.USER_SELETED_PLAN + "," 
				+ Keys.USER_JOINING_DATE + ","
				+ Keys.USER_REG_NO + ","
				+ Keys.USER_ADDRESS + ","
				+ Keys.USER_PROFILE_URL + ")"
				+ " values ('" +
				u.getUid() + "','" 
				+ u.getEmail() + "','" 
				+ u.getName()+ "'," 
				+ u.getAge() + ",'" 
				+ u.getDob() + "','"
				+ u.getMob() + "'," 
				+ u.getWeight() + ",'" 
				+ u.getPlan()+ "'," 
				+ u.getJoiningDate().getTime() + ","
				+ u.getReg_no() + ",'" 
				+ u.getAddress() + "','" 
				+ u.getProfile() + "');";
		System.out.println(sql);
		try {
			statement.executeUpdate(sql);
			isAdded = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isAdded = false;
		}
		return isAdded;
	}

	public boolean addPlan(PlansModel plan, Statement statement) {
		// TODO Auto-generated method stub
		boolean isAdded = false;
		String sql = "insert into " + Keys.TABLE_PLAN_DATA + "(" +
				Keys.PLAN_PID + "," +
				Keys.PLAN_NAME + ","+
				Keys.PLAN_AMOUNT +"," +
				Keys.PLAN_DETAIL +","+
				Keys.PLAN_NO_OF_MONTHS + ")" +
				" values ('" +
					plan.getPid() + "','"+
					plan.getName() +"'," +
					plan.getAmt() +",'" +
					plan.getDetail()+"',"+
					plan.getNoOfMonth() + ");";
		System.out.println(sql);
		try {
			statement.executeUpdate(sql);
			isAdded = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isAdded = false;
		}
		return isAdded;

	}
	public boolean updatePlan(PlansModel p, Statement statement) {
		// TODO Auto-generated method stub
		boolean isAdded=false;
		String sql = "update " + Keys.TABLE_PLAN_DATA + " set " 
			
				+ Keys.PLAN_NAME + " ='"
				+ p.getName() + "'," 
				+ Keys.PLAN_NO_OF_MONTHS + " ="
				+ p.getNoOfMonth() + ","
				+ Keys.PLAN_AMOUNT + " ="
				+ p.getAmt()+","
				+ Keys.PLAN_DETAIL +"="
				+ p.getDetail()
				+ " where "
				+ Keys.PLAN_PID + " ='" 
				+ p.getPid() + "';";
		System.out.println(sql);
		try {
			statement.executeUpdate(sql);
			isAdded = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isAdded;
	}
	public boolean removePlan(String pid, Statement statement) {
		// TODO Auto-generated method stub
		boolean isRemoved = false;
		String sql = "DELETE FROM " + Keys.TABLE_PLAN_DATA + " where " + Keys.PLAN_PID + " = '" + pid + "';";
		System.out.println(sql);
		try {
			statement.executeUpdate(sql);
			isRemoved = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isRemoved = false;
		}
		return isRemoved;
	}

	public boolean updateUser(User u, Statement statement) {
		boolean isAdded = false;
		String sql = "update " + Keys.TABLE_USER_DATA + " set " 
				+ Keys.USER_EMAIL + " ='" + u.getEmail() + "' ,"
				+ Keys.USER_NAME + " ='" + u.getName() + "' ," + Keys.USER_AGE + " =" + u.getAge() + " ,"
				+ Keys.USER_DOB + " ='" + u.getDob() + "' ," + Keys.USER_MOB_NO + " ='" + u.getMob() + "' ,"
				+ Keys.USER_WEIGHT + " =" + u.getWeight() + " ," + Keys.USER_SELETED_PLAN + " ='" + u.getPlan() + "' ,"
				+ Keys.USER_JOINING_DATE + " =" + u.getJoiningDate().getTime() + " ," + Keys.USER_PROFILE_URL + " ='"
				+ u.getProfile() + "'" + " where " + Keys.USER_UID + " ='" + u.getUid() + "';";
		System.out.println(sql);
		try {
			statement.executeUpdate(sql);
			isAdded = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isAdded = false;
		}
		return isAdded;
	}

	public boolean updateUserPlan(String uid, String pid, Statement statement) {
		boolean isAdded = false;
		String sql = "update " + Keys.TABLE_USER_DATA + " set " + Keys.USER_SELETED_PLAN + " ='" + pid + "'" + " where "
				+ Keys.USER_UID + " ='" + uid + "';";
		System.out.println(sql);
		try {
			statement.executeUpdate(sql);
			isAdded = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isAdded = false;
		}
		return isAdded;
	}

	public ArrayList<User> getUserList(Statement statement) {
		// TODO Auto-generated method stub
		ArrayList<User> userList = new ArrayList();
		User u;
		String sql = "select * from " + Keys.TABLE_USER_DATA + ";";
		System.out.println(sql);
		try {
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				u = new User(rs.getString(Keys.USER_DOB),
						rs.getString(Keys.USER_NAME),
						rs.getString(Keys.USER_UID),
						rs.getString(Keys.USER_EMAIL),
						rs.getString(Keys.USER_MOB_NO),
						rs.getString(Keys.USER_PROFILE_URL),
						rs.getInt(Keys.USER_AGE),
						rs.getInt(Keys.USER_WEIGHT),
						rs.getString(Keys.USER_SELETED_PLAN),
						rs.getInt(Keys.USER_REG_NO),
						new Date(rs.getLong(Keys.USER_JOINING_DATE)),
						rs.getString(Keys.USER_ADDRESS));
				userList.add(u);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userList;
	}

	public ArrayList<PlansModel> getPlansList(Statement statement) {
		// TODO Auto-generated method stub
		ArrayList<PlansModel> planList = new ArrayList();
		PlansModel p;
		String sql = "select * from " + Keys.TABLE_PLAN_DATA + ";";
		System.out.println(sql);
		try {
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				p = new PlansModel(rs.getString(Keys.PLAN_PID), rs.getString(Keys.PLAN_NAME),
						rs.getInt(Keys.PLAN_AMOUNT), rs.getInt(Keys.PLAN_NO_OF_MONTHS),rs.getString(Keys.PLAN_DETAIL));
				planList.add(p);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return planList;
	}

	public ArrayList<ActiveBillModel> getActiveBillList(Statement statement) {
		// TODO Auto-generated method stub
		ArrayList<ActiveBillModel> billList = new ArrayList();
		ActiveBillModel bill;
		String sql = "select * from " + Keys.TABLE_ACTIVE_BILLS_DATA + ";";
		System.out.println(sql);
		try {
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				bill = new ActiveBillModel(rs.getString(Keys.ACTIVE_BILL_BID), rs.getString(Keys.ACTIVE_BILL_UID),
						new Date(rs.getLong(Keys.ACTIVE_BILL_SESSION_FROM)),
						new Date(rs.getLong(Keys.ACTIVE_BILL_SESSION_TO)), rs.getInt(Keys.ACTIVE_BILL_AMOUNT_PENDING),
						rs.getInt(Keys.ACTIVE_BILL_AMOUNT_PAID));
				System.out.println(bill.getUid());
				billList.add(bill);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return billList;
	}
	public ActiveBillModel getActiveBillListUser(Statement statement,String uid) {
		// TODO Auto-generated method stub
		ActiveBillModel bill=null;
		String sql = "select * from " + Keys.TABLE_ACTIVE_BILLS_DATA + " where "+ Keys.ACTIVE_BILL_UID+"='"+uid+"';";
		System.out.println(sql);
		try {
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				bill = new ActiveBillModel(rs.getString(Keys.ACTIVE_BILL_BID), rs.getString(Keys.ACTIVE_BILL_UID),
						new Date(rs.getLong(Keys.ACTIVE_BILL_SESSION_FROM)),
						new Date(rs.getLong(Keys.ACTIVE_BILL_SESSION_TO)), rs.getInt(Keys.ACTIVE_BILL_AMOUNT_PENDING),
						rs.getInt(Keys.ACTIVE_BILL_AMOUNT_PAID));
				System.out.println(bill.getUid());
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bill;
	}

	public boolean removeUser(String uid, Statement statement) {
		// TODO Auto-generated method stub
		boolean isRemoved = false;
		String sql = "DELETE FROM " + Keys.TABLE_USER_DATA + " where " + Keys.USER_UID + " = '" + uid + "';";
		System.out.println(sql);
		try {
			statement.executeUpdate(sql);
			isRemoved = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isRemoved = false;
		}
		return isRemoved;
	}

	public boolean addBill(ActiveBillModel activeBill, Statement statement) {
		// TODO Auto-generated method stub
		boolean isAdded = false;
		String sql = "insert into " + Keys.TABLE_ACTIVE_BILLS_DATA + " ( " + Keys.ACTIVE_BILL_BID + ","
				+ Keys.ACTIVE_BILL_UID + "," + Keys.ACTIVE_BILL_SESSION_FROM + "," + Keys.ACTIVE_BILL_SESSION_TO + ","
				+ Keys.ACTIVE_BILL_AMOUNT_PAID + "," + Keys.ACTIVE_BILL_AMOUNT_PENDING + ") values" + "('"
				+ activeBill.getBid() + "','" + activeBill.getUid() + "'," + activeBill.getSessionFrom().getTime() + ","
				+ activeBill.getSessionTo().getTime() + "," + activeBill.getAmountPaid() + ","
				+ activeBill.getAmountPending() + ");";
		System.out.println(sql);

		try {
			statement.executeUpdate(sql);
			isAdded = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isAdded = false;
		}
		return isAdded;
	}

	public boolean updateBill(ActiveBillModel u, Statement statement) {
		boolean isAdded = false;
		String sql = "update " + Keys.TABLE_ACTIVE_BILLS_DATA + " set " + Keys.ACTIVE_BILL_AMOUNT_PAID + " ="
				+ u.getAmountPaid() + " ," + Keys.ACTIVE_BILL_AMOUNT_PENDING + " =" + u.getAmountPending() + " ,"
				+ Keys.ACTIVE_BILL_SESSION_FROM + " =" + u.getSessionFrom().getTime() + " ,"
				+ Keys.ACTIVE_BILL_SESSION_TO + " =" + u.getSessionTo().getTime() + " " + " where "
				+ Keys.ACTIVE_BILL_BID + " ='" + u.getBid() + "';";
		System.out.println(sql);
		try {
			statement.executeUpdate(sql);
			isAdded = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isAdded = false;
		}
		return isAdded;
	}

	public boolean addOldBill(String oid, ActiveBillModel bill, Statement statement) {
		// TODO Auto-generated method stub
		boolean isAdded = false;
		//createOldBills(statement);
		String sql = "insert into " + Keys.TABLE_OLD_BILLS_DATA + " ( " + Keys.OLD_BILL_BID + "," + Keys.OLD_BILL_UID
				+ "," + Keys.OLD_BILL_SESSION_FROM + "," + Keys.OLD_BILL_SESSION_TO + "," + Keys.OLD_BILL_AMOUNT_PAID
				+ "," + Keys.OLD_BILL_DATE + ") values" + "('" + bill.getBid() + "','" + bill.getUid() + "',"
				+ bill.getSessionFrom().getTime() + "," + bill.getSessionTo().getTime() + "," + bill.getAmountPaid()
				+ "," + new Date().getTime() + ");";
		System.out.println(sql);

		try {
			statement.executeUpdate(sql);
			isAdded = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isAdded = false;
		}
		return isAdded;
	}
	public boolean addOldBillFromOldBill(OldBillModel ob, Statement statement) {
		// TODO Auto-generated method stub
		boolean isAdded = false;
		//createOldBills(statement);
		String sql = "insert into " + Keys.TABLE_OLD_BILLS_DATA + " ( " + Keys.OLD_BILL_BID + "," + Keys.OLD_BILL_UID
				+ "," + Keys.OLD_BILL_SESSION_FROM + "," + Keys.OLD_BILL_SESSION_TO + "," + Keys.OLD_BILL_AMOUNT_PAID
				+ "," + Keys.OLD_BILL_DATE + ") values" 
				+ "('" + ob.getBid() + "','" 
				+ ob.getUid() + "',"
				+ ob.getSession_from() + "," 
				+ ob.getSession_to() + "," 
				+ ob.getAmount_paid()
				+ "," + ob.getBill_date() + ");";
		System.out.println(sql);

		try {
			statement.executeUpdate(sql);
			isAdded = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isAdded = false;
		}
		return isAdded;
	}

	public boolean updateAdmin(admin a, Statement statement) {
		boolean isAdded = false;
		String sql = "update " + Keys.TABLE_ADMIN_DATA + " set " + Keys.ADMIN_EMAIL + " ='" + a.getEmail() + "' ,"
				+ Keys.ADMIN_NAME + " ='" + a.getName() + "' ," + Keys.ADMIN_USER_NAME + " ='" + a.getUsername() + "' ,"
				+ Keys.ADMIN_PROFILE_URL + " ='" + a.getProfile() + "', " + Keys.ADMIN_USER_PASSWORD + " ='"
				+ a.getPassword() + "' " + " where " + Keys.ADMIN_AID + " ='" + a.getAid() + "';";
		System.out.println(sql);
		try {
			statement.executeUpdate(sql);
			isAdded = true;
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isAdded = false;
		}
		return isAdded;
	}
	public boolean checkAttendance(AttendanceModel att, Statement statment){
		boolean isExist=false;
		String sql="select * from "+Keys.TABLE_ATENDANCE_DATA +
				" where "+Keys.ATENDANCE_UID+" = "+att.getUid()+" AND "
				+Keys.ATENDANCE_DATE+" = "+att.getDate();
		try {
			ResultSet rs = statment.executeQuery(sql);
			if(rs.next()) {
				isExist=true;
				}
			}catch(SQLException e){
				e.printStackTrace();
			}
		return isExist;
	}
	public boolean addAttendance(AttendanceModel att, Statement statment){
		// TODO Auto-generated method stub
		//createAtendanceTable(statment);
		boolean isAdded=false;
		int isP=0;
		if(att.isPresent())
			isP=1;
				String sql="insert into "+Keys.TABLE_ATENDANCE_DATA +"("+
				Keys.ATENDANCE_AID+","
				+Keys.ATENDANCE_UID+","
				+Keys.ATENDANCE_DATE+","
				+Keys.ATENDANCE_DAY+","
				+Keys.ATENDANCE_MONTH+","
				+Keys.ATENDANCE_YEAR+","
				+Keys.ATENDANCE_IS_PRESENT+") values ("
				+att.getAid()+","
				+att.getUid()+","
				+att.getDate()+","
						+att.getDay()+","
								+att.getMonth()+","
										+att.getYear()+","
				+isP+");";
				try {
					//System.out.println(sql);
					statment.executeUpdate(sql);
					statment.close();
					isAdded = true;
					statment.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					isAdded = false;
				}
		return isAdded;
	}
	public ArrayList<AttendanceModel> getUserAttendanceDates(String uid, Statement statement) {
		AttendanceModel att=null;
		ArrayList<AttendanceModel> list=new ArrayList();
		String sql="select * from "+Keys.TABLE_ATENDANCE_DATA +" where " 
				+Keys.ATENDANCE_UID+" = '"+uid+"';";
		//String sql="select * from "+Keys.TABLE_ATENDANCE_DATA ;
		
		System.out.println(sql);
		try {
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				boolean isp=false;
				if(rs.getInt(Keys.ATENDANCE_IS_PRESENT)==1)
					isp=true;
				att = new AttendanceModel(rs.getString(Keys.ATENDANCE_AID),
						rs.getString(Keys.ATENDANCE_UID),
						rs.getLong(Keys.ATENDANCE_DATE),
						isp,
						rs.getInt(Keys.ATENDANCE_DAY),
						rs.getInt(Keys.ATENDANCE_MONTH),
						rs.getInt(Keys.ATENDANCE_YEAR));
				list.add(att);
			}
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public ArrayList<AttendanceModel> getAllUserAttendanceDates(Statement statement) {
		AttendanceModel att=null;
		ArrayList<AttendanceModel> list=new ArrayList();
		String sql="select * from "+Keys.TABLE_ATENDANCE_DATA +";";
		
		System.out.println(sql);
		try {
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				boolean isp=false;
				if(rs.getInt(Keys.ATENDANCE_IS_PRESENT)==1)
					isp=true;
				att = new AttendanceModel(rs.getString(Keys.ATENDANCE_AID),
						rs.getString(Keys.ATENDANCE_UID),
						rs.getLong(Keys.ATENDANCE_DATE),
						isp,
						rs.getInt(Keys.ATENDANCE_DAY),
						rs.getInt(Keys.ATENDANCE_MONTH),
						rs.getInt(Keys.ATENDANCE_YEAR));
				list.add(att);
			}
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public ArrayList<OldBillModel> getOldBillList(String uid, Statement statement) {
		ArrayList<OldBillModel> billList=new ArrayList();
		OldBillModel bill=null;
		String sql="select * from "+Keys.TABLE_OLD_BILLS_DATA +" where " 
				+Keys.OLD_BILL_UID+" = '"+uid+"';";
		System.out.println(sql);
		try {
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				bill= new OldBillModel(rs.getString(Keys.OLD_BILL_BID),
						rs.getString(Keys.OLD_BILL_UID),
						rs.getLong(Keys.OLD_BILL_SESSION_FROM),
						rs.getLong(Keys.OLD_BILL_SESSION_TO),
						rs.getInt(Keys.OLD_BILL_AMOUNT_PAID),
						rs.getLong(Keys.OLD_BILL_DATE));
				billList.add(bill);
			}
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return billList;
	}
	public ArrayList<OldBillModel> getAllOldBillList(Statement statement) {
		ArrayList<OldBillModel> billList=new ArrayList();
		OldBillModel bill=null;
		String sql="select * from "+Keys.TABLE_OLD_BILLS_DATA +";";
		System.out.println(sql);
		try {
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				bill= new OldBillModel(rs.getString(Keys.OLD_BILL_BID),
						rs.getString(Keys.OLD_BILL_UID),
						rs.getLong(Keys.OLD_BILL_SESSION_FROM),
						rs.getLong(Keys.OLD_BILL_SESSION_TO),
						rs.getInt(Keys.OLD_BILL_AMOUNT_PAID),
						rs.getLong(Keys.OLD_BILL_DATE));
				billList.add(bill);
			}
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return billList;
	}
	public admin getAdminForgetAuth(String user, String email, Statement statement) {
		admin a = null;
		String sql = "select * from " + Keys.TABLE_ADMIN_DATA + " where " + Keys.ADMIN_USER_NAME + " = '" + user
				+ "' and " + Keys.ADMIN_EMAIL + " = '" + email + "';";
		System.out.println(sql);
		try {
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				a = new admin(rs.getString(Keys.ADMIN_AID), rs.getString(Keys.ADMIN_NAME),
						rs.getString(Keys.ADMIN_EMAIL), rs.getString(Keys.ADMIN_USER_PASSWORD),
						rs.getString(Keys.ADMIN_USER_NAME), rs.getString(Keys.ADMIN_PROFILE_URL),rs.getInt(Keys.ADMIN_TYPE));
			}
		} catch (SQLException e) {
		}
		return a;
	}
	public boolean addCompany(CompanyDetailModel company, Statement statement) {
		boolean isAdded=false;
		
		String sql="insert into "+Keys.COMPANY_TABLE_NAME+"("+
				Keys.COMPANY_NAME+","
				+Keys.COMPANY_EMAIL+","
				+Keys.COMPANY_MOB+","
				+Keys.COMPANY_ADDRESS+","
				+Keys.COMPANY_GSTNO+","
				+Keys.COMPANY_GSTPERSENTAGE+") values ('"
				+company.getName()+"','"
				+company.getEmail()+"','"
				+company.getMob()+"','"
						+company.getAddress()+"','"
								+company.getGstNo()+"',"
										+company.getGstPercent()+");";
				try {
					statement.executeUpdate(sql);
					statement.close();
					isAdded = true;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					isAdded = false;
				}
		
		return isAdded;
	}
	public CompanyDetailModel getCompanyDetail(Statement statement){
		CompanyDetailModel company=null;
		String sql="select * from "+Keys.COMPANY_TABLE_NAME ;
		System.out.println(sql);
		try {
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				company= new CompanyDetailModel(rs.getString(Keys.COMPANY_NAME),
						rs.getString(Keys.COMPANY_MOB),
						rs.getString(Keys.COMPANY_EMAIL),
						rs.getString(Keys.COMPANY_ADDRESS),
						rs.getString(Keys.COMPANY_GSTNO),
						rs.getInt(Keys.COMPANY_GSTPERSENTAGE));
			}
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return company; 
	}
	public boolean UpdateCompany(CompanyDetailModel company, Statement statement) {
	boolean isAdded=false;

	String sql = "update " + Keys.COMPANY_TABLE_NAME + " set " 
			+ Keys.COMPANY_NAME + " ='" + company.getName() + "' ,"
			+ Keys.COMPANY_MOB + " ='" +company.getMob() + "' ,"
			+ Keys.COMPANY_ADDRESS + " ='" + company.getAddress() + "', "
			+ Keys.COMPANY_GSTPERSENTAGE + " =" + company.getGstPercent() + ", "
			+ Keys.COMPANY_GSTNO + " ='"+ company.getGstNo() + "' " + " where " 
			+ Keys.COMPANY_EMAIL + " ='" + company.getEmail() + "';";
				try {
					statement.executeUpdate(sql);
					statement.close();
					isAdded = true;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					isAdded = false;
				}
		
		return isAdded;
	}
}