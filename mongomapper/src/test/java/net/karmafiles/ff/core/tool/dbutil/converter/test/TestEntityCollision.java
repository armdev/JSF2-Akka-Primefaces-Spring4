package net.karmafiles.ff.core.tool.dbutil.converter.test;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import junit.framework.TestCase;
import net.karmafiles.ff.core.tool.dbutil.converter.Converter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by Ilya Brodotsky
 * Date: 20.10.2010
 * Time: 16:01:22
 * <p/>
 * All rights reserved.
 * <p/>
 * Contact me:
 * email, jabber: ilya.brodotsky@gmail.com
 * skype: ilya.brodotsky
 */

public class TestEntityCollision extends TestCase {

    
    @Test
    public void test1() throws Exception {
        Mongo m = new Mongo("localhost", 27017);
        DB db = m.getDB("mydb1");
        DBCollection coll = db.getCollection("collision");
        coll.drop();

        OldEntity oldEntity = new OldEntity();
        oldEntity.set_id("id1");
        oldEntity.setStringField("oldEntityField");

        DBObject dbObject = Converter.toDBObject(oldEntity);
        coll.save(dbObject);

        DBObject loadedDBObject = coll.findOne();
        NewEntity newEntity = Converter.toObject(NewEntity.class, loadedDBObject);

        assertNotNull(newEntity);
        assertFalse(newEntity.isBooleanField());
        assertEquals("oldEntityField", newEntity.getStringField());


    }

    

}
