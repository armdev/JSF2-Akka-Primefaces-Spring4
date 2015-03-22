package net.karmafiles.ff.core.tool.dbutil.converter.test;

import java.util.List;
import java.util.Map;

/**
 * Created by Ilya Brodotsky
 * Date: 13.10.2010
 * Time: 18:44:13
 * <p/>
 * All rights reserved.
 * <p/>
 * Contact me:
 * email, jabber: ilya.brodotsky@gmail.com
 * skype: ilya.brodotsky
 */

public class TestEntity5 {
    private String _id;

    private List<String> stringList;

    private List<TestEntity4> entity4List;

    private Map<String, String> stringToStringMap;

    public Map<String, String> getStringToStringMap() {
        return stringToStringMap;
    }

    public void setStringToStringMap(Map<String, String> stringToStringMap) {
        this.stringToStringMap = stringToStringMap;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public List<TestEntity4> getEntity4List() {
        return entity4List;
    }

    public void setEntity4List(List<TestEntity4> entity4List) {
        this.entity4List = entity4List;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
