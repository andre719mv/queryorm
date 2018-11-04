package com.stayfit.sample;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.stayfit.queryorm.lib.DOBase;
import com.stayfit.queryorm.lib.DbHelper;
import com.stayfit.queryorm.lib.QueryParms;
import com.stayfit.queryorm.lib.SqlExecutor;
import com.stayfit.queryorm.lib.sqlinterfaces.ISQLiteDatabase;
import com.stayfit.sample.dal.AppDb;
import com.stayfit.sample.dal.DbCoumns;
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
                long startTime = System.currentTimeMillis();
                ISQLiteDatabase db = DbHelper.getHelper().getWritableDatabase();

                db.beginTransaction();

                for (int i = 0; i < 10000; i++) {
                    PersonEntity personEntity = new PersonEntity();
                    personEntity.name = "John" + i;
                    personEntity.lastName = "Doe" + i;
                    personEntity.save();
                }

                db.commitTransaction();
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
                stopTime = System.currentTimeMillis();
                Log.i("DbTest", "Delete " + (stopTime - startTime));

            }
        };

        thread.start();

        SqlExecutor executor = new SqlExecutor();
        SmartSqlQuery query = new SmartSqlQuery();
        executor.executeSelect(PersonEntity.class, query);

    }
}
