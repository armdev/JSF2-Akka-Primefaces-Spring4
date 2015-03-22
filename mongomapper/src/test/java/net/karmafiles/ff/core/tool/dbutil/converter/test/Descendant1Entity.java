package net.karmafiles.ff.core.tool.dbutil.converter.test;

/**
 * Created by Ilya Brodotsky
 * Date: 26.10.2010
 * Time: 14:34:09
 * <p/>
 * All rights reserved.
 * <p/>
 * Contact me:
 * email, jabber: ilya.brodotsky@gmail.com
 * skype: ilya.brodotsky
 */

public class Descendant1Entity extends AncestorEntity {
    private String s1;

    public Descendant1Entity() {
    }

    public Descendant1Entity(String _id, String baseField, String s1) {
        super(_id, baseField);
        this.s1 = s1;
    }

    public String getS1() {
        return s1;
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    @Override
    public String abstractMethod() {
        return "entity1";
    }
}
