package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import bean.UserDetails;

/**
 * Created by deepak on 8/10/15.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "homeMaid.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper dbHelper;
    private static Context mContext;

    private RuntimeExceptionDao<UserDetails, Integer> UserDetailsDataClassRuntimeDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DatabaseHelper(context);
            dbHelper.getWritableDatabase();
            mContext = context;
        }
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        Log.i(DatabaseHelper.class.getName(), "onCreate");
        try {
            TableUtils.createTable(connectionSource, UserDetails.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {
        Log.i(DatabaseHelper.class.getName(), "onUpgrade");
        try {
            TableUtils.dropTable(connectionSource, UserDetails.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public RuntimeExceptionDao<UserDetails, Integer> getUsersDetailDao() {
        if (UserDetailsDataClassRuntimeDao == null) {
            UserDetailsDataClassRuntimeDao = getRuntimeExceptionDao(UserDetails.class);
        }
        return UserDetailsDataClassRuntimeDao;
    }

    public void insertUserDetails(UserDetails userDetails) {
        UserDetailsDataClassRuntimeDao = getUsersDetailDao();
            UserDetailsDataClassRuntimeDao.create(userDetails);
    }

    public List<UserDetails> getUserDetails(){
        RuntimeExceptionDao<UserDetails, Integer> dao = getUsersDetailDao();
        return dao.queryForAll();
    }

    public void deleteUserDetails() {
        try {
            TableUtils.clearTable(connectionSource, UserDetails.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
