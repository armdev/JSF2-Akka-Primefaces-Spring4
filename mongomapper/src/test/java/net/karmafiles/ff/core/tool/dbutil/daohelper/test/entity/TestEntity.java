package net.karmafiles.ff.core.tool.dbutil.daohelper.test.entity;

/**
 * Created by Ilya Brodotsky
 * Date: 17.10.2010
 * Time: 20:09:00
 * <p/>
 * All rights reserved.
 * <p/>
 * Contact me:
 * email, jabber: ilya.brodotsky@gmail.com
 * skype: ilya.brodotsky
 */

public class TestEntity extends BaseEntity {

    private String stringField;

    private String notNullField;

    private String urlField;

    private EmbeddedTestEntity embeddedEntity;

    public String getStringField() {
        return stringField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    public String getNotNullField() {
        return notNullField;
    }

    public void setNotNullField(String notNullField) {
        this.notNullField = notNullField;
    }

    public String getUrlField() {
        return urlField;
    }

    public void setUrlField(String urlField) {
        this.urlField = urlField;
    }

    public EmbeddedTestEntity getEmbeddedEntity() {
        return embeddedEntity;
    }

    public void setEmbeddedEntity(EmbeddedTestEntity embeddedEntity) {
        this.embeddedEntity = embeddedEntity;
    }
}
