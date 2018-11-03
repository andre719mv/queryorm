package com.stayfit.sample.dal.entities;

import com.stayfit.queryorm.lib.DOBase;
import com.stayfit.queryorm.lib.MapColumn;
import com.stayfit.queryorm.lib.MapTable;
import com.stayfit.queryorm.lib.sqlinterfaces.ISQLiteCursor;
import com.stayfit.sample.dal.DbCoumns;

/**
 * Created by Администратор on 3/21/2018.
 */

@MapTable("person")
public class PersonEntity extends DOBase {
    public PersonEntity(ISQLiteCursor cursor) {
        super(cursor);
    }
    public PersonEntity() {}

    @MapColumn(DbCoumns.Person.Name)
    public String name;
    @MapColumn(DbCoumns.Person.LastName)
    public String lastName;
    @MapColumn(DbCoumns.Person.Age)
    public int age;
}
