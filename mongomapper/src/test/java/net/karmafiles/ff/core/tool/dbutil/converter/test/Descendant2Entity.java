package net.karmafiles.ff.core.tool.dbutil.converter.test;

/**
 * Created by Ilya Brodotsky
 * Date: 26.10.2010
 * Time: 14:34:18
 * <p/>
 * All rights reserved.
 * <p/>
 * Contact me:
 * email, jabber: ilya.brodotsky@gmail.com
 * skype: ilya.brodotsky
 */

public class Descendant2Entity extends AncestorEntity {
    private String s2;

    public Descendant2Entity() {
    }

    public Descendant2Entity(String _id, String baseField, String s2) {
        super(_id, baseField);
        this.s2 = s2;
    }

    public String getS2() {
        return s2;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }

    @Override
    public String abstractMethod() {
        return "entity2";
    }
}
