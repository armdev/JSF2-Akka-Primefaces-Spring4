package net.karmafiles.ff.core.tool.dbutil.converter.test;

import com.mongodb.*;
import junit.framework.TestCase;
import net.karmafiles.ff.core.tool.dbutil.converter.Converter;
import net.karmafiles.ff.core.tool.dbutil.daohelper.test.dao.TestEntityDaoTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by Ilya Brodotsky
 * Date: 21.10.2010
 * Time: 2:06:51
 * <p/>
 * All rights reserved.
 * <p/>
 * Contact me:
 * email, jabber: ilya.brodotsky@gmail.com
 * skype: ilya.brodotsky
 */

public class TestPrimitive extends TestCase {
    @BeforeTest
    protected void setUp() throws Exception {
        try {
            assert true == false;
            throw new RuntimeException("Please enable assertions.");
        } catch (AssertionError e) {
        }
    }


    @Test
    public void test1() throws Exception {
        Mongo m = new Mongo("localhost", 27017);
        DB db = m.getDB("mydb1");
        DBCollection coll = db.getCollection("primitives");
        coll.drop();

        PrimitiveEntity primitiveEntity = new PrimitiveEntity();
        primitiveEntity.set_id("id1");
        primitiveEntity.setValue(false);

        coll.save(Converter.toDBObject(primitiveEntity));

        DBObject loadedObject = coll.findOne();
        PrimitiveEntity primitiveEntity1 = Converter.toObject(PrimitiveEntity.class, loadedObject);

        assert primitiveEntity1.get_id().equals("id1");
        assert !primitiveEntity1.isValue();

        primitiveEntity1.setValue(true);

        coll.update(QueryBuilder.start("_id").is("id1").get(), Converter.toDBObject(primitiveEntity1));

        primitiveEntity1 = Converter.toObject(PrimitiveEntity.class, coll.findOne());

        assert primitiveEntity1.isValue();

        

        
        
    }
}
