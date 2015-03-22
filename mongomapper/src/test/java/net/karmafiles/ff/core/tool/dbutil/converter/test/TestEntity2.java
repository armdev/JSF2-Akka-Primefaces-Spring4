package net.karmafiles.ff.core.tool.dbutil.converter.test;

/**
 * Created by Ilya Brodotsky
 * Date: 08.10.2010
 * Time: 15:27:37
 * <p/>
 * All rights reserved.
 * <p/>
 * Contact me:
 * email, jabber: ilya.brodotsky@gmail.com
 * skype: ilya.brodotsky
 */

public class TestEntity2 {
    private String _id;
    private String stringField;

    public TestEntity2() {
    }

    public TestEntity2(String _id, String stringField) {
        this._id = _id;
        this.stringField = stringField;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getStringField() {
        return stringField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    @Override
    public boolean equals(Object obj) {
        return ((TestEntity2)obj).get_id().equals(_id) && ((TestEntity2)obj).getStringField().equals(stringField); 
    }
}
