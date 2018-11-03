package com.stayfit.sample.dal;

/**
 * Created by Администратор on 6/13/2016.
 */
public class CreateTableScripts {
    public static final String Person = "CREATE TABLE IF NOT EXISTS "
            + Tables.Person + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DbCoumns.Person.Name + " TEXT, "
            + DbCoumns.Person.LastName + " TEXT, "
            + DbCoumns.Person.Age + " INTEGER DEFAULT 0);";

}


