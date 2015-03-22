package net.karmafiles.ff.core.tool.dbutil.converter.test;

import com.mongodb.*;
import junit.framework.TestCase;
import net.karmafiles.ff.core.tool.dbutil.converter.Converter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Ilya Brodotsky
 * Date: 26.10.2010
 * Time: 14:32:08
 * <p/>
 * All rights reserved.
 * <p/>
 * Contact me:
 * email, jabber: ilya.brodotsky@gmail.com
 * skype: ilya.brodotsky
 */

public class GenericListTest extends TestCase {
    @BeforeTest
    protected void setUp() throws Exception {
        try { assert true == false; throw new RuntimeException("Please enable assertions.");} catch(AssertionError e) {}
    }

    @Test
    public void testList() throws Exception {
        GenericListEntity entity = new GenericListEntity();
        entity.set_id("z1");

        entity.setList(new ArrayList<AbstractAncestorEntity>());
        entity.getList().add(new AncestorEntity("id1", "base1"));
        entity.getList().add(new Descendant1Entity("id2", "base2", "aaaa"));
        entity.getList().add(new Descendant2Entity("id3", "base3", "bbbb"));

        Mongo m = new Mongo("localhost", 27017);
        DB db = m.getDB("mydb1");
        DBCollection coll = db.getCollection("testCollection");
        coll.drop();

        DBObject dbObject = Converter.toDBObject(entity);
        coll.save(dbObject);

        DBObject newDbObject = coll.findOne();

        GenericListEntity newEntity = Converter.toObject(GenericListEntity.class, newDbObject);
        assertNotNull(newEntity);
        assertNotNull(newEntity.getList());
        assertEquals(3, newEntity.getList().size());

        boolean aFound = false, d1Found = false, d2Found = false;

        for(int x = 0; x < newEntity.getList().size(); x++) {
            Object o = newEntity.getList().get(x);
            String className = o.getClass().getName();
            System.out.println(className);
            if(o instanceof Descendant1Entity) {
                d1Found = true;
                Descendant1Entity d1 = (Descendant1Entity) o;
                assertEquals("aaaa", d1.getS1());
                assertEquals("id2", d1.get_id());
                assertEquals("base2", d1.getBaseField());
                assertEquals("entity1", d1.abstractMethod());

            } else if(o instanceof Descendant2Entity) {
                d2Found = true;
                Descendant2Entity d2 = (Descendant2Entity) o;
                assertEquals("bbbb", d2.getS2());
                assertEquals("id3", d2.get_id());
                assertEquals("base3", d2.getBaseField());
                assertEquals("entity2", d2.abstractMethod());

            } else if(o instanceof AncestorEntity) {
                aFound = true;
                AncestorEntity aa = (AncestorEntity) o;
                assertEquals("id1", aa.get_id());
                assertEquals("base1", aa.getBaseField());
                assertEquals("ancestor", aa.abstractMethod());
            }
        }
        assertTrue(aFound && d1Found && d2Found);

        newEntity.getList().remove(2);
        newEntity.getList().remove(1);
        newEntity.getList().add(new Descendant2Entity("blah", "blah", "blah"));
        coll.update(idQuery(newEntity), Converter.toDBObject(newEntity));

        newDbObject = coll.findOne();

        newEntity = Converter.toObject(GenericListEntity.class, newDbObject);
        assertNotNull(newEntity);
        assertNotNull(newEntity.getList());
        assertEquals(2, newEntity.getList().size());
        assertTrue(newEntity.getList().get(0) instanceof AncestorEntity);
        AncestorEntity aa = (AncestorEntity) newEntity.getList().get(0);
        assertEquals("id1", aa.get_id());
        assertEquals("base1", aa.getBaseField());
        assertEquals("ancestor", aa.abstractMethod());

        assertTrue(newEntity.getList().get(1) instanceof Descendant2Entity);
        Descendant2Entity d2 = (Descendant2Entity) newEntity.getList().get(1);
        assertEquals("blah", d2.getS2());
        assertEquals("blah", d2.get_id());
        assertEquals("blah", d2.getBaseField());
        assertEquals("entity2", d2.abstractMethod());


    }

    private DBObject idQuery(GenericListEntity newEntity) {
        return QueryBuilder.start("_id").is(newEntity.get_id()).get();
    }

    @Test
    public void testSet() throws Exception {
        GenericListEntity entity = new GenericListEntity();
        entity.set_id("z1");

        entity.setSet(new HashSet<AbstractAncestorEntity>());
        entity.getSet().add(new AncestorEntity("id1", "base1"));
        entity.getSet().add(new Descendant1Entity("id2", "base2", "aaaa"));
        entity.getSet().add(new Descendant2Entity("id3", "base3", "bbbb"));

        Mongo m = new Mongo("localhost", 27017);
        DB db = m.getDB("mydb1");
        DBCollection coll = db.getCollection("testCollection");
        coll.drop();

        DBObject dbObject = Converter.toDBObject(entity);
        coll.save(dbObject);

        DBObject newDbObject = coll.findOne();

        GenericListEntity newEntity = Converter.toObject(GenericListEntity.class, newDbObject);
        assertNotNull(newEntity);

        assertNotNull(newEntity.getSet());
        assertEquals(3, newEntity.getSet().size());

        boolean aFound = false, d1Found = false, d2Found = false;

        for(Object o: newEntity.getSet()) {

            System.out.println(o.getClass().getName());
            if(o instanceof Descendant1Entity) {
                d1Found = true;
                Descendant1Entity d1 = (Descendant1Entity) o;
                assertEquals("aaaa", d1.getS1());
                assertEquals("id2", d1.get_id());
                assertEquals("base2", d1.getBaseField());
                assertEquals("entity1", d1.abstractMethod());

            } else if(o instanceof Descendant2Entity) {
                d2Found = true;
                Descendant2Entity d2 = (Descendant2Entity) o;
                assertEquals("bbbb", d2.getS2());
                assertEquals("id3", d2.get_id());
                assertEquals("base3", d2.getBaseField());
                assertEquals("entity2", d2.abstractMethod());

            } else if(o instanceof AncestorEntity) {
                aFound = true;
                AncestorEntity aa = (AncestorEntity) o;
                assertEquals("id1", aa.get_id());
                assertEquals("base1", aa.getBaseField());
                assertEquals("ancestor", aa.abstractMethod());
            }
        }


        assertTrue(aFound && d1Found && d2Found);

        newEntity.getSet().clear();
        newEntity.getSet().add(new Descendant2Entity("blah", "blah", "blah"));
        coll.update(idQuery(newEntity), Converter.toDBObject(newEntity));

        newDbObject = coll.findOne();

        newEntity = Converter.toObject(GenericListEntity.class, newDbObject);
        assertNotNull(newEntity);
        assertNotNull(newEntity.getSet());
        assertEquals(1, newEntity.getSet().size());
        assertTrue(newEntity.getSet().iterator().next() instanceof Descendant2Entity);

        Descendant2Entity d2 = (Descendant2Entity) newEntity.getSet().iterator().next();
        assertEquals("blah", d2.getS2());
        assertEquals("blah", d2.get_id());
        assertEquals("blah", d2.getBaseField());
        assertEquals("entity2", d2.abstractMethod());

    }

    @Test
    public void testMap() throws Exception {
        GenericListEntity entity = new GenericListEntity();
        entity.set_id("z1");

        entity.setMap(new HashMap<TestEnumEntity, AbstractAncestorEntity>());
        entity.getMap().put(TestEnumEntity.FOUR, new AncestorEntity("id1", "base1"));
        entity.getMap().put(TestEnumEntity.FIVE, new Descendant1Entity("id2", "base2", "aaaa"));
        entity.getMap().put(TestEnumEntity.SIX ,new Descendant2Entity("id3", "base3", "bbbb"));

        Mongo m = new Mongo("localhost", 27017);
        DB db = m.getDB("mydb1");
        DBCollection coll = db.getCollection("testCollection");
        coll.drop();

        DBObject dbObject = Converter.toDBObject(entity);
        coll.save(dbObject);

        DBObject newDbObject = coll.findOne();

        GenericListEntity newEntity = Converter.toObject(GenericListEntity.class, newDbObject);
        assertNotNull(newEntity);
        assertNotNull(newEntity.getMap());
        assertEquals(3, newEntity.getMap().size());

        boolean aFound = false, d1Found = false, d2Found = false;

        for(TestEnumEntity k: newEntity.getMap().keySet()) {

            Object o = newEntity.getMap().get(k);
            System.out.println(o.getClass().getName());
            if(o instanceof Descendant1Entity) {
                d1Found = true;
                Descendant1Entity d1 = (Descendant1Entity) o;
                assertEquals("aaaa", d1.getS1());
                assertEquals("id2", d1.get_id());
                assertEquals("base2", d1.getBaseField());
                assertEquals("entity1", d1.abstractMethod());

            } else if(o instanceof Descendant2Entity) {
                d2Found = true;
                Descendant2Entity d2 = (Descendant2Entity) o;
                assertEquals("bbbb", d2.getS2());
                assertEquals("id3", d2.get_id());
                assertEquals("base3", d2.getBaseField());
                assertEquals("entity2", d2.abstractMethod());

            } else if(o instanceof AncestorEntity) {
                aFound = true;
                AncestorEntity aa = (AncestorEntity) o;
                assertEquals("id1", aa.get_id());
                assertEquals("base1", aa.getBaseField());
                assertEquals("ancestor", aa.abstractMethod());
            }
        }


        assertTrue(aFound && d1Found && d2Found);

        newEntity.getMap().remove(TestEnumEntity.FOUR);
        newEntity.getMap().put(TestEnumEntity.SIX, new Descendant2Entity("blah", "blah", "blah"));
        coll.update(idQuery(newEntity), Converter.toDBObject(newEntity));

        newDbObject = coll.findOne();

        newEntity = Converter.toObject(GenericListEntity.class, newDbObject);
        assertNotNull(newEntity);
        assertNotNull(newEntity.getMap());
        assertEquals(2, newEntity.getMap().size());
        assertTrue(newEntity.getMap().get(TestEnumEntity.SIX) instanceof Descendant2Entity);

        Descendant2Entity d2 = (Descendant2Entity) newEntity.getMap().get(TestEnumEntity.SIX);
        assertEquals("blah", d2.getS2());
        assertEquals("blah", d2.get_id());
        assertEquals("blah", d2.getBaseField());
        assertEquals("entity2", d2.abstractMethod());

        assertTrue(newEntity.getMap().get(TestEnumEntity.FIVE) instanceof Descendant1Entity);

        Descendant1Entity d1 = (Descendant1Entity) newEntity.getMap().get(TestEnumEntity.FIVE);
        assertEquals("aaaa", d1.getS1());
        assertEquals("id2", d1.get_id());
        assertEquals("base2", d1.getBaseField());
        assertEquals("entity1", d1.abstractMethod());


    }


}
