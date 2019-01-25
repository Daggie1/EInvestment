package com.example.anonymous.e_investment.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.anonymous.e_investment.databases.EInvestimentSchemaDb.MemberTable;
import com.example.anonymous.e_investment.databases.EInvestimentSchemaDb.GroupTable;
import com.example.anonymous.e_investment.databases.EInvestimentSchemaDb.ContributionTable;
import com.example.anonymous.e_investment.databases.EInvestimentSchemaDb.TransactionTable;
public class EInvestimentsBaseHelper extends SQLiteOpenHelper {


    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "eIvestimentBase.db";


    public EInvestimentsBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /*sqLiteDatabase.execSQL("CREATE TABLE "+ MemberTable.NAME+"("+
                "_id integer primary key autoincrement,"+
                MemberTable.Cols.MEMBERID+","+
                MemberTable.Cols.USERNAME+","+
                MemberTable.Cols.EMAIL+","+
                MemberTable.Cols.PHONE+","+
                MemberTable.Cols.PHOTOCURL+","+
                MemberTable.Cols.PASSWORD+


                ")"
        );
        sqLiteDatabase.execSQL("CREATE TABLE "+ GroupTable.NAME+"("+
                "_id integer primary key autoincrement,"+
                GroupTable.Cols.GROUPID+","+
                GroupTable.Cols.GROUPNAME+","+
                GroupTable.Cols.AMOUNTCONTRIBUTED+","+
                GroupTable.Cols.PHOTOCURL+


                ")"
        );
        sqLiteDatabase.execSQL("CREATE TABLE "+ ContributionTable.NAME+"("+
                "_id integer primary key autoincrement,"+
                ContributionTable.Cols.CONTRIBUTIONID+","+
                ContributionTable.Cols.USERID+","+
                ContributionTable.Cols.GROUPID+","+
                ContributionTable.Cols.AMOUNTCONTRIBUTED+


                ")"
        );

        sqLiteDatabase.execSQL("CREATE TABLE "+ TransactionTable.NAME+"("+
                "_id integer primary key autoincrement,"+
                TransactionTable.Cols.TRANSACTIONID+","+
                TransactionTable.Cols.GROUPID+","+
                TransactionTable.Cols.USERID+","+
                TransactionTable.Cols.AMOUNTCONTRIBUTED+","+
                TransactionTable.Cols.DATETRANSACTED+



                ")"
        );*/

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
