package net.karmafiles.ff.core.tool.dbutil.converter.test;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ilya Brodotsky
 * Date: 26.10.2010
 * Time: 14:38:04
 * <p/>
 * All rights reserved.
 * <p/>
 * Contact me:
 * email, jabber: ilya.brodotsky@gmail.com
 * skype: ilya.brodotsky
 */

public class GenericListEntity {
    private String _id;
    private List<AbstractAncestorEntity> list;
    private Set<AbstractAncestorEntity> set;
    private Map<TestEnumEntity, AbstractAncestorEntity> map;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<AbstractAncestorEntity> getList() {
        return list;
    }

    public void setList(List<AbstractAncestorEntity> list) {
        this.list = list;
    }

    public Set<AbstractAncestorEntity> getSet() {
        return set;
    }

    public void setSet(Set<AbstractAncestorEntity> set) {
        this.set = set;
    }

    public Map<TestEnumEntity, AbstractAncestorEntity> getMap() {
        return map;
    }

    public void setMap(Map<TestEnumEntity, AbstractAncestorEntity> map) {
        this.map = map;
    }
}
