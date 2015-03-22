package net.karmafiles.ff.core.tool.dbutil.converter.test;

/**
 * @author timur
 */

public class TestEntity6 {

    private String _id;

    private TestEnumEmbeddedEntity embeddedEnum;

    private TestEmbeddedEntity embedded;

    private TestEnumEntity enumEntity;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public TestEnumEmbeddedEntity getEmbeddedEnum() {
        return embeddedEnum;
    }

    public void setEmbeddedEnum(TestEnumEmbeddedEntity embeddedEnum) {
        this.embeddedEnum = embeddedEnum;
    }

    public TestEmbeddedEntity getEmbedded() {
        return embedded;
    }

    public void setEmbedded(TestEmbeddedEntity embedded) {
        this.embedded = embedded;
    }

    public TestEnumEntity getEnumEntity() {
        return enumEntity;
    }

    public void setEnumEntity(TestEnumEntity enumEntity) {
        this.enumEntity = enumEntity;
    }

    /**
     * @author timur
     */
    public static enum TestEnumEmbeddedEntity {
        ONE, TWO, THREE
    }

    public static class TestEmbeddedEntity {
        private String value1;
        private int value2;

        public String getValue1() {
            return value1;
        }

        public void setValue1(String value1) {
            this.value1 = value1;
        }

        public int getValue2() {
            return value2;
        }

        public void setValue2(int value2) {
            this.value2 = value2;
        }
    }
}
