package com.stayfit.sample.dal;

/**
 * Created by Администратор on 6/13/2016.
 */
public class CreateTableScriptsOld {
    public static final String Person1 = "CREATE TABLE IF NOT EXISTS "
            + Tables.Person + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DbCoumns.Person.Name + " TEXT);";

}


