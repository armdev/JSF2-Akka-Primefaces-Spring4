package net.karmafiles.ff.core.tool.dbutil.converter.test;

/**
 * Created by Ilya Brodotsky
 * Date: 26.10.2010
 * Time: 14:32:57
 * <p/>
 * All rights reserved.
 * <p/>
 * Contact me:
 * email, jabber: ilya.brodotsky@gmail.com
 * skype: ilya.brodotsky
 */

public abstract class AbstractAncestorEntity {
    private String _id;
    private String baseField;

    public AbstractAncestorEntity() {
    }

    public AbstractAncestorEntity(String _id, String baseField) {
        this._id = _id;
        this.baseField = baseField;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getBaseField() {
        return baseField;
    }

    public void setBaseField(String baseField) {
        this.baseField = baseField;
    }

    public abstract String abstractMethod();
}
