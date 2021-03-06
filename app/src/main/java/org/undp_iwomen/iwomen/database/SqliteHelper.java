package org.undp_iwomen.iwomen.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Khin Sandsar on 28/07/15.
 */
public class SqliteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "IWOMENDB";
    private static int DATABASE_VERSION = 11;//Version Code 5 in 4 , Version Code 6 in 5 ,Versison code 7 6
    //,Versison code 8 7 //Version 9 8 //Version Code 10 in 9////Version Code 11 in 10
    // Android app version 2.0 , version code 11 , Db version 10
    // Android app version 2.1 , version code 12 , Db version 11



    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(TableAndColumnsName.UserUtil.CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(TableAndColumnsName.PostUtil.CREATE_POST_TABLE);
        sqLiteDatabase.execSQL(TableAndColumnsName.UserPostUtil.CREATE_USER_POST_TABLE);
        sqLiteDatabase.execSQL(TableAndColumnsName.CommentUtil.CREATE_COMMENT_TABLE);

        sqLiteDatabase.execSQL(TableAndColumnsName.ResourceUtil.CREATE_RESOURCE_TABLE);
        sqLiteDatabase.execSQL(TableAndColumnsName.SubResourceUtil.CREATE_SUB_RESOURCE_TABLE);
        sqLiteDatabase.execSQL(TableAndColumnsName.SisterAppUtil.CREATE_SISTERAPP_TABLE);
        sqLiteDatabase.execSQL(TableAndColumnsName.SubResourceDetailUtil.CREATE_SUB_RESOURCE_DETAIL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldversion, int newversion) {


        sqLiteDatabase.execSQL("Drop Table If Exists " + TableAndColumnsName.TableNames.USER);
        sqLiteDatabase.execSQL("Drop Table If Exists " + TableAndColumnsName.TableNames.POST);
        sqLiteDatabase.execSQL("Drop Table If Exists " + TableAndColumnsName.TableNames.USER_POST);
        sqLiteDatabase.execSQL("Drop Table If Exists " + TableAndColumnsName.TableNames.COMMENT);

        sqLiteDatabase.execSQL("Drop Table If Exists " + TableAndColumnsName.TableNames.RESOURCE);
        sqLiteDatabase.execSQL("Drop Table If Exists " + TableAndColumnsName.TableNames.SUBRESOURCE);

        sqLiteDatabase.execSQL("Drop Table If Exists " + TableAndColumnsName.TableNames.SISTERAPP);
        sqLiteDatabase.execSQL("Drop Table If Exists " + TableAndColumnsName.TableNames.SUBRESOURCEDETAIL);

    }


}
