package net.karmafiles.ff.core.tool.dbutil.daohelper.test.entity;

import java.util.Date;

/**
 * Created by Ilya Brodotsky
 * Date: 15.10.2010
 * Time: 17:39:26
 * <p/>
 * All rights reserved.
 * <p/>
 * Contact me:
 * email, jabber: ilya.brodotsky@gmail.com
 * skype: ilya.brodotsky
 */
public abstract class BaseEntity {

    /**
     * Technical (internal) ID
     */
    private String _id;

    /**
     * Human-readable name
     *
     * -- �� ������ �����, ��������, � ������ ���������� ������ ��� �����-������ ������ � ���
     */
    //private String name;

    /**
     * Creation time
     */
    private Date created;

    /**
     * Modification time
     */
    private Date modified;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }
}
