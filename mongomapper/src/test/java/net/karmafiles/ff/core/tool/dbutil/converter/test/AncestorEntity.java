package net.karmafiles.ff.core.tool.dbutil.converter.test;

/**
 * @author timur
 */
public class AncestorEntity extends AbstractAncestorEntity {

    public AncestorEntity() {
    }

    public AncestorEntity(String _id, String baseField) {
        super(_id, baseField);
    }

    @Override
    public String abstractMethod() {
        return "ancestor";
    }
}
