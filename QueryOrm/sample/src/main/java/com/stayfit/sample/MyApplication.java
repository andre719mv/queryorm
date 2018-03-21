package com.stayfit.sample;

import android.app.Application;
import android.content.Context;

import com.stayfit.queryorm.lib.CommonFields;
import com.stayfit.queryorm.lib.DOBase;
import com.stayfit.queryorm.lib.DbHelper;
import com.stayfit.queryorm.lib.QueryParms;

public class MyApplication extends Application {
    public static Context AppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext = this;
        DbHelper.setHelperClass(AppDb.class);

        //This is first call to DB in usual app start. So in most cases DB create or upgrade happens here. Move this call to first activity and display splash screen to make heavy work in background.
        //Also db init can happen from services or if app started with another activity. We do not care about those cases.
        DbHelper.getHelper().getWritableDatabase();

        PersonEntity entity = new PersonEntity();
        entity.name = "JohnDoe";
        entity.save();

        //now entity have id
        //let`s select it
        entity = DOBase.selectById(PersonEntity.class, entity._id);
        //or
        QueryParms param = new QueryParms(PersonEntity.class)
                .addCriteria("person", CommonFields.Id);
        entity = DOBase.selectSingle(PersonEntity.class, param);
    }
}
