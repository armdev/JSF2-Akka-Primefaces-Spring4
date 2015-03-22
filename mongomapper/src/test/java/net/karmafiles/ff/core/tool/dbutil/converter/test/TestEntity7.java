package net.karmafiles.ff.core.tool.dbutil.converter.test;

import java.util.Map;

/**
 * @author timur
 */

public class TestEntity7 {

    private String _id;

    private Map<TestEnumEntity,TestEntity> enumToEntityMap;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }


    public Map<TestEnumEntity, TestEntity> getEnumToEntityMap() {
        return enumToEntityMap;
    }

    public void setEnumToEntityMap(Map<TestEnumEntity, TestEntity> enumToEntityMap) {
        this.enumToEntityMap = enumToEntityMap;
    }
}
