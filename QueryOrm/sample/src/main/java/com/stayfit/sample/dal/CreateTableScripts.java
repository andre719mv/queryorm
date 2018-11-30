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

    public static final String Exercise = "CREATE TABLE IF NOT EXISTS "
            + Tables.Exercise
            + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DbCoumns.Exercise.YoutubeId + " TEXT, "
            + DbCoumns.Exercise.MainMuscle + " INTEGER DEFAULT 0, "
            + DbCoumns.Exercise.OtherMuscles + " INTEGER DEFAULT 0, "
            + DbCoumns.Exercise.IdExternal + " INTEGER DEFAULT 0, "
            + DbCoumns.Exercise.UnitType + " INTEGER DEFAULT 0, "
            + DbCoumns.Exercise.Weight + " INTEGER DEFAULT 0, "
            + DbCoumns.Exercise.Name + " TEXT NULL, "
            + DbCoumns.Exercise.Description + " TEXT NULL, "
            + DbCoumns.Exercise.IdUser + " INTEGER DEFAULT 0, "
            + DbCoumns.Exercise.Category + " INTEGER DEFAULT 0, "
            + DbCoumns.Exercise.Equipment + " INTEGER DEFAULT 0, "
            + DbCoumns.Exercise.MechanicsType + " INTEGER DEFAULT 0, "
            + DbCoumns.Exercise.Level + " INTEGER DEFAULT 0, "
            + DbCoumns.Exercise.Rating + " REAL DEFAULT 0, "
            + DbCoumns.Exercise.ImagesCount + " INTEGER DEFAULT 0, "
            + DbCoumns.Exercise.IsDeleted + " INTEGER DEFAULT 0, "
            + DbCoumns.Exercise.Version + " INTEGER DEFAULT 0);";

}


