package net.karmafiles.ff.core.tool.dbutil.daohelper.test.entity;

public class EmbeddedTestEntity {

    private String eStringField;

    public EmbeddedTestEntity() {
    }

    public EmbeddedTestEntity(String eStringField) {
        this.eStringField = eStringField;
    }

    public String geteStringField() {
        return eStringField;
    }

    public void seteStringField(String eStringField) {
        this.eStringField = eStringField;
    }
}