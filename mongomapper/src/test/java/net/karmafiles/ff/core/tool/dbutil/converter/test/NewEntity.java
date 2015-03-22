package net.karmafiles.ff.core.tool.dbutil.converter.test;

/**
 * Created by Ilya Brodotsky
 * Date: 20.10.2010
 * Time: 16:02:28
 * <p/>
 * All rights reserved.
 * <p/>
 * Contact me:
 * email, jabber: ilya.brodotsky@gmail.com
 * skype: ilya.brodotsky
 */

public class NewEntity extends OldEntity {
    private boolean booleanField;

    public boolean isBooleanField() {
        return booleanField;
    }

    public void setBooleanField(boolean booleanField) {
        this.booleanField = booleanField;
    }
}
