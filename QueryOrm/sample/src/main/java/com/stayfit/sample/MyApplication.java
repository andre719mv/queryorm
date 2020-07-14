package com.stayfit.sample;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.stayfit.queryorm.lib.CommonFields;
import com.stayfit.queryorm.lib.DOBase;
import com.stayfit.queryorm.lib.DbHelper;
import com.stayfit.queryorm.lib.QueryParms;
import com.stayfit.queryorm.lib.SmartSqlQuery;
import com.stayfit.queryorm.lib.SqlExecutor;
import com.stayfit.queryorm.lib.sqlinterfaces.ISQLiteContentValues;
import com.stayfit.queryorm.lib.sqlinterfaces.ISQLiteDatabase;
import com.stayfit.queryorm.lib.sqlinterfaces.ISQLiteDatabaseHelper;
import com.stayfit.sample.dal.AppDb;
import com.stayfit.sample.dal.DbCoumns;
import com.stayfit.sample.dal.Tables;
import com.stayfit.sample.dal.entities.Exercise;
import com.stayfit.sample.dal.entities.PersonEntity;

import java.util.List;

public class MyApplication extends Application {
    public static Context AppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext = this;
        DbHelper.setHelper(new AppDb(AppContext));

        //This is first call to DB in usual app start. So in most cases DB create or upgrade happens here. Move this call to first activity and display splash screen to make heavy work in background.
        //Also db init can happen from services or if app started with another activity. We do not care about those cases.
        DbHelper.getHelper().getWritableDatabase();

        PersonEntity entity = new PersonEntity();
        entity.name = "John Doe";
        entity.save();

        //now entity have id
        //let`s select it
        entity = DOBase.selectById(PersonEntity.class, entity._id);
        //or
        QueryParms param = new QueryParms(PersonEntity.class)
                .addCriteria(DbCoumns.Person.Name, "John Doe");
        entity = DOBase.selectSingle(PersonEntity.class, param);

        Thread thread = new Thread(){
            @Override
            public void run() {
                //Speed test
                ISQLiteDatabase db = DbHelper.getHelper().getWritableDatabase();

                //testExercise(db);
                //speedTest(db);
                testIsDeleted();

            }
        };

        thread.start();

        //SqlExecutor executor = new SqlExecutor();
        //SmartSqlQuery query = new SmartSqlQuery();
        //executor.executeSelect(PersonEntity.class, query);

    }

    private void testIsDeleted() {
        Exercise ex = new Exercise();
        ex.name = "test";
        ex.IsDeleted = true;
        ex.save();

        Long id = ex._id;

        QueryParms parms = new QueryParms(Exercise.class)
                .addCriteria(CommonFields.Id, id);
        ex = DOBase.selectSingle(Exercise.class, parms);
        if(ex != null)
            throw new RuntimeException("Failed");

        ex = DOBase.selectById(Exercise.class, id);
        if(ex == null)
            throw new RuntimeException("Failed");

        parms = new QueryParms(Exercise.class)
                .addCriteria(CommonFields.Id, id)
                .withDeleted();
        ex = DOBase.selectSingle(Exercise.class, parms);
        if(ex == null)
            throw new RuntimeException("Failed");
    }

    private void speedTest(ISQLiteDatabase db) {
        long startTime = System.currentTimeMillis();
        db.beginTransaction();

        for (int i = 0; i < 10000; i++) {
            PersonEntity personEntity = new PersonEntity();
            personEntity.name = "John" + i;
            personEntity.lastName = "Doe" + i;
            personEntity.save();
        }

        long stopTime = System.currentTimeMillis();
        Log.i("DbTest", "Insert " + (stopTime - startTime));

        startTime = System.currentTimeMillis();
        List<PersonEntity> persons = DOBase.selectAll(PersonEntity.class, new QueryParms(PersonEntity.class));
        stopTime = System.currentTimeMillis();
        Log.i("DbTest", "Select " + (stopTime - startTime));

        startTime = System.currentTimeMillis();
        for (int i = 0; i < persons.size(); i++) {
            persons.get(i).delete();
        }

        db.commitTransaction();
        stopTime = System.currentTimeMillis();
        Log.i("DbTest", "Delete " + (stopTime - startTime));
    }

    private void testExercise(ISQLiteDatabase db) {
        long externalId = 36;
        db.execSQL("delete from " + Tables.Exercise);

        Exercise model = new Exercise();
        model.unit_type = 1;
        model.id_external = externalId;
        model.mainMuscle = 2;
        model.otherMuscles = 2;
        model.weight = 2;
        model.version = 2;
        model.category = 2;
        model.equipment = 2;
        model.mechanicsType = 2;
        model.level = 2;
        model.rating = 36.4F;
        model.imagesCount = 0;
        model.IsDeleted = false;

        ISQLiteContentValues values = db.newContentValues();
        values.put(DbCoumns.Exercise.UnitType, model.unit_type);
        values.put(DbCoumns.Exercise.IdExternal, model.id_external);
        values.put(DbCoumns.Exercise.MainMuscle, model.mainMuscle);
        values.put(DbCoumns.Exercise.OtherMuscles, model.otherMuscles);
        values.put(DbCoumns.Exercise.Weight, model.weight);
        values.put(DbCoumns.Exercise.Version, model.version);
        values.put(DbCoumns.Exercise.Category, model.category);
        values.put(DbCoumns.Exercise.Equipment, model.equipment);
        values.put(DbCoumns.Exercise.MechanicsType, model.mechanicsType);
        values.put(DbCoumns.Exercise.Level, model.level);
        values.put(DbCoumns.Exercise.Rating, model.rating);
        values.put(DbCoumns.Exercise.ImagesCount, model.imagesCount);
        values.put(DbCoumns.Exercise.IsDeleted, model.IsDeleted);
        values.put(DbCoumns.Exercise.IdUser, 0);

        db.insert(Tables.Exercise, DbCoumns.Exercise.IdExternal, values);
        List<Exercise> exercises = DOBase.selectAll(Exercise.class, new QueryParms(Exercise.class).withDeleted());
        Exercise ex2 = DOBase.selectByColumnVal(Exercise.class, DbCoumns.Exercise.IdExternal,externalId);

        values.put(DbCoumns.Exercise.IsDeleted, true);
        values.put(DbCoumns.Exercise.Rating, 4.5F);

        String[] whereArgs = new String[]{String.valueOf(model.id_external)};
        db.update(Tables.Exercise, values, DbCoumns.Exercise.IdExternal + " = ?", whereArgs);

        exercises = DOBase.selectAll(Exercise.class, new QueryParms(Exercise.class).withDeleted());
        ex2 = DOBase.selectByColumnVal(Exercise.class, DbCoumns.Exercise.IdExternal, externalId);

        db.execSQL("delete from " + Tables.Exercise);

        if(ex2 != null || exercises.size() != 1)
            throw  new RuntimeException();
    }
}
