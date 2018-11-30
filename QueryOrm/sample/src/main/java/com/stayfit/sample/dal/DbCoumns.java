package com.stayfit.sample.dal;

import com.stayfit.queryorm.lib.CommonFields;

public class DbCoumns {
    public static class Person{
        public static final  String Name = "name";
        public static final  String LastName = "last_name";
        public static final  String Age = "age";
    }

    public static class Exercise {
        public static final  String YoutubeId = "video_normative";
        @Deprecated
        public static final  String Thumb = "thumb_normative";
        @Deprecated
        public static final  String IsUpdated = "is_updated_normative";
        public static final  String MainMuscle = "main_muscle_normative";
        public static final  String OtherMuscles = "muscle_part_flags_normative";
        public static final  String IdExternal = "id_external_normative";
        public static final  String UnitType = "unit_type_normative";
        public static final  String Weight = "weight_normative";
        public static final  String Version = "version_normative";
        public static final  String Name = "name_exercise";
        public static final  String Description = "description_exercise";
        public static final  String IdUser = "id_user_exercise";
        public static final  String Category = "category";
        public static final  String Equipment = "equipment";
        public static final  String MechanicsType = "mechanics_type";
        public static final  String Level = "level";
        public static final  String Rating = "rating";
        public static final  String ImagesCount = "images_count";
        public static final  String IsDeleted = CommonFields.IsDeleted;
    }
}