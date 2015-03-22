package net.karmafiles.ff.core.tool.dbutil.converter.test;

import java.util.List;
import java.util.Map;

/**
 * Created by Ilya Brodotsky
 * Date: 08.10.2010
 * Time: 16:21:38
 * <p/>
 * All rights reserved.
 * <p/>
 * Contact me:
 * email, jabber: ilya.brodotsky@gmail.com
 * skype: ilya.brodotsky
 */

public class TestEntity3 {
    String _id;
    Map<String, TestEntity2> map;
    List<TestEntity2> list;

    public TestEntity3() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Map<String, TestEntity2> getMap() {
        return map;
    }

    public void setMap(Map<String, TestEntity2> map) {
        this.map = map;
    }

    public List<TestEntity2> getList() {
        return list;
    }

    public void setList(List<TestEntity2> list) {
        this.list = list;
    }
}
