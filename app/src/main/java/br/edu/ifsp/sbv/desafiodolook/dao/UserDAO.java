package br.edu.ifsp.sbv.desafiodolook.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.edu.ifsp.sbv.desafiodolook.model.User;

/**
 * Created by Adriel on 11/20/2017.
 */

public class UserDAO extends DAO<User> {

    private SQLiteDatabase database;

    public UserDAO(Context context) {
        super(context);
        campos = new String[]{"userInfoID","email","password","userName", "nickName",
                "description","urlAvatar","dateBirth","deviceID","status","dateCreation"};
        tableName = "userInfo";
        database = getWritableDatabase();

    }

    public boolean save(User userInfo) {
        ContentValues values = serializeContentValues(userInfo);
        if(database.insert(tableName, null, values)>0)
            return true;
        else
            return false;
    }

    public boolean update(User userInfo) {
        ContentValues values = serializeContentValues2(userInfo);
        //new String[]{String.valueOf(userInfo.getUserID())}
        if(database.update(tableName,values,"userInfoID = ?", new String[]{String.valueOf(userInfo.getUserID())})>0)
            return true;
        else
            return false;
    }

    private ContentValues serializeContentValues(User userInfo)
    {
        ContentValues values = new ContentValues();
        values.put("userInfoID", userInfo.getUserID());
        values.put("email", userInfo.getEmail());
        values.put("password", userInfo.getPassword());
        values.put("userName", userInfo.getUserName());
        values.put("nickName", userInfo.getNickName());
        values.put("description", userInfo.getDescription());
        values.put("urlAvatar", userInfo.getUrlAvatar());
        values.put("dateBirth", "null");
        values.put("deviceID", userInfo.getDeviceID());
        values.put("status", userInfo.getStatus());
        values.put("dateCreation", "null");

        return values;
    }

    private ContentValues serializeContentValues2(User userInfo)
    {
        ContentValues values = new ContentValues();
        values.put("email", userInfo.getEmail());
        values.put("password", userInfo.getPassword());
        values.put("userName", userInfo.getUserName());
        values.put("nickName", userInfo.getNickName());
        values.put("description", userInfo.getDescription());
        values.put("urlAvatar", userInfo.getUrlAvatar());
        values.put("dateBirth", "null");
        values.put("deviceID", userInfo.getDeviceID());
        values.put("status", false);
        values.put("dateCreation", "null");

        return values;
    }

    public User getByID(Integer userInfoID) {
        User userInfo = null;

        Cursor cursor = executeSelect("userInfoID = ?", new String[]{String.valueOf(userInfoID)}, null);

        if(cursor!=null && cursor.moveToFirst())
        {
            userInfo = serializeByCursor(cursor);
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }

        return userInfo;
    }

    public User getUser() {
        User userInfo = null;

        Cursor cursor = executeSelect(null, null, null);

        if(cursor!=null && cursor.moveToFirst())
        {
            userInfo = serializeByCursor(cursor);
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }

        return userInfo;
    }

    public Integer getCount() {
        Integer i = 0;
        Cursor cursor = database.rawQuery("select count(*) from userInfo",null);

        if(cursor.moveToFirst())
        {
            i = cursor.getInt(0);
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }

        return i;
    }

    private User serializeByCursor(Cursor cursor)
    {
        User userInfo = new User();
        userInfo.setUserID(cursor.getInt(0));
        userInfo.setEmail(cursor.getString(1));
        userInfo.setUserName(cursor.getString(3));

        return userInfo;

    }

    private Cursor executeSelect(String selection, String[] selectionArgs, String orderBy)
    {
        return database.query(tableName,campos, selection, selectionArgs, null, null, orderBy);
    }

    public boolean deletar() {
        if(database.delete(tableName,null,null)>0)
            return true;
        else
            return false;
    }
}
