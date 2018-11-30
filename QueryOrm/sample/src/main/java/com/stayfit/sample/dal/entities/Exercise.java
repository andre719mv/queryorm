package com.stayfit.sample.dal.entities;

import com.stayfit.queryorm.lib.ColumnDeleted;
import com.stayfit.queryorm.lib.DOBase;
import com.stayfit.queryorm.lib.MapColumn;
import com.stayfit.queryorm.lib.sqlinterfaces.ISQLiteCursor;
import com.stayfit.sample.dal.DbCoumns;

public class Exercise extends DOBase {
    public Exercise(ISQLiteCursor cursor) {
        super(cursor);
    }

    public Exercise(){
        youtube_id = "";
        name = "";
    }

    @MapColumn(DbCoumns.Exercise.YoutubeId)
    public String youtube_id;
    @MapColumn(DbCoumns.Exercise.MainMuscle)
    public int mainMuscle;
    @MapColumn(DbCoumns.Exercise.OtherMuscles)
    public int otherMuscles;
    @MapColumn(DbCoumns.Exercise.IdExternal)
    public long id_external;
    @MapColumn(DbCoumns.Exercise.UnitType)
    public int unit_type;
    @MapColumn(DbCoumns.Exercise.Weight)
    public int weight;
    /**Not language depended entity data version**/
    @MapColumn(DbCoumns.Exercise.Version)
    public int version;
    @MapColumn(DbCoumns.Exercise.Name)
    public String name;
    @MapColumn(DbCoumns.Exercise.IdUser)
    public long idUser;
    @MapColumn(DbCoumns.Exercise.Description)
    public String description;
    @MapColumn(DbCoumns.Exercise.Category)
    public int category;
    @MapColumn(DbCoumns.Exercise.Equipment)
    public int equipment;
    @MapColumn(DbCoumns.Exercise.MechanicsType)
    public int mechanicsType;
    @MapColumn(DbCoumns.Exercise.Level)
    public int level;
    @MapColumn(DbCoumns.Exercise.Rating)
    public float rating;
    @MapColumn(DbCoumns.Exercise.ImagesCount)
    public int imagesCount;
    @MapColumn(DbCoumns.Exercise.IsDeleted)
    public boolean IsDeleted;

    @Deprecated @ColumnDeleted
    @MapColumn(DbCoumns.Exercise.IsUpdated)
    public boolean is_updated;

    @Deprecated @ColumnDeleted
    @MapColumn(DbCoumns.Exercise.Thumb)
    public String thumb;
}

