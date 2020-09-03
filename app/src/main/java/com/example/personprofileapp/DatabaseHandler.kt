package com.example.personprofileapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException

//Here i have created  the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "PersonDatabase" //this is the Database name
        private val TABLE_CONTACTS = "PersonTable" //table name contacts
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_EMAIL = "email"
    }
    override fun onCreate(db: SQLiteDatabase?) {

        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //here i have created  table with fields
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        onCreate(db)
    }


    //method to insert data
    fun addPerson(per: PerModelClass):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, per.personId)
        contentValues.put(KEY_NAME, per.personName) // EmpModelClass Name
        contentValues.put(KEY_EMAIL,per.personEmail ) // EmpModelClass Phone
        // Inserting Row
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        //2nd argument is String
        db.close() // closing database connection .............................
        return success
    }
    //method to read data
    fun viewPerson():List<PerModelClass>{
        val perList:ArrayList<PerModelClass> = ArrayList<PerModelClass>()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var personId: Int
        var personName: String
        var personEmail: String
        if (cursor.moveToFirst()) {
            do {
                personId = cursor.getInt(cursor.getColumnIndex("id"))
                personName = cursor.getString(cursor.getColumnIndex("name"))
                personEmail = cursor.getString(cursor.getColumnIndex("email"))
                val per= PerModelClass(personId = personId, personName = personName, personEmail = personEmail)
                perList.add(per)
            } while (cursor.moveToNext())
        }
        return perList
    }
    //method to update data
    fun updatePerson(per: PerModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, per.personId)
        contentValues.put(KEY_NAME, per.personName) // PerModelClass Name
        contentValues.put(KEY_EMAIL,per.personEmail ) // PerModelClass Email

        // to update a Row
        val success = db.update(TABLE_CONTACTS, contentValues,"id="+per.personId,null)
        //2nd argument is String
        db.close() // here is the closing database connection
        return success
    }
    //Here is a method to delete data
    fun deletePerson(per: PerModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, per.personId) // PerModelClass UserId
        // Deleting Row
        val success = db.delete(TABLE_CONTACTS,"id="+per.personId,null)
        //2nd argument is String containing nullColumnHack
        db.close() // closing database connection
        return success
    }
}
