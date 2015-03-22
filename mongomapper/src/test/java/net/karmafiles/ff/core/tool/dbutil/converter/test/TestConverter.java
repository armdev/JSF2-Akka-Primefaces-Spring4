package net.karmafiles.ff.core.tool.dbutil.converter.test;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import net.karmafiles.ff.core.tool.dbutil.converter.Converter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.UnknownHostException;
import java.util.*;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

/**
 * Created by Ilya Brodotsky
 * Date: 14.10.2010
 * Time: 18:13:32
 * <p/>
 * All rights reserved.
 * <p/>
 * Contact me:
 * email, jabber: ilya.brodotsky@gmail.com
 * skype: ilya.brodotsky
 */

public class TestConverter {

    @BeforeTest
    protected void setUp() throws Exception {
        try { assert true == false; throw new RuntimeException("Please enable assertions.");} catch(AssertionError e) {}        
    }

    @Test
    public void test1() throws Exception {
        TestEntity testEntity = new TestEntity();
        testEntity.set_id("12345");
        testEntity.setBooleanField(true);
        testEntity.setDoubleField(123456.789);
        testEntity.setIntegerField(12345);
        testEntity.setIntField(23456);
        testEntity.setStringField("test string");
        testEntity.setLongField(7890123L);

        TestEntity2 testEntity2 = new TestEntity2();
        testEntity2.set_id("23456");
        testEntity2.setStringField("shit");

        testEntity.setTestEntity2(testEntity2);

        Mongo m = new Mongo("localhost", 27017);
        DB db = m.getDB("mydb1");
        DBCollection coll = db.getCollection("testCollection");
        coll.drop();

        DBObject dbObject = Converter.toDBObject(testEntity);
        coll.save(dbObject);

        DBObject newDbObject = coll.findOne();
        assert dbObject.get("booleanField").equals(newDbObject.get("booleanField"));
        assert dbObject.get("doubleField").equals(newDbObject.get("doubleField"));
        assert dbObject.get("integerField").equals(newDbObject.get("integerField"));
        assert dbObject.get("intField").equals(newDbObject.get("intField"));
        assert dbObject.get("integerField").equals(newDbObject.get("integerField"));
        assert dbObject.get("longField").equals(newDbObject.get("longField"));
        assert ((DBObject)dbObject.get("testEntity2")).get("_id").equals(
                ((DBObject)newDbObject.get("testEntity2")).get("_id")
        );
        assert ((DBObject)dbObject.get("testEntity2")).get("stringField").equals(
                ((DBObject)newDbObject.get("testEntity2")).get("stringField")
        );

    }

    @SuppressWarnings({"unchecked"})
    @Test
    public void test2() throws UnknownHostException {
        TestEntity3 testEntity3 = new TestEntity3();
        testEntity3.set_id("entity3id");

        Map<String, TestEntity2> map = new HashMap();
        map.put("entry1", new TestEntity2("id1", "val1"));
        map.put("entry2", new TestEntity2("id2", "val2"));

        List<TestEntity2> list = new ArrayList();
        list.add(new TestEntity2("id3", "val3"));
        list.add(new TestEntity2("id4", "val4"));

        testEntity3.setList(list);
        testEntity3.setMap(map);

        Mongo m = new Mongo("localhost", 27017);
        DB db = m.getDB("mydb1");
        DBCollection coll = db.getCollection("testCollection");
        coll.drop();

        coll.save(Converter.toDBObject(testEntity3));
    }

    @Test
    public void test3() throws Exception {
        TestEntity testEntity = new TestEntity();
        testEntity.set_id("12345");
        testEntity.setBooleanField(true);
        testEntity.setDoubleField(123456.789);
        testEntity.setIntegerField(12345);
        testEntity.setIntField(23456);
        testEntity.setStringField("test string");

        TestEntity2 testEntity2 = new TestEntity2();
        testEntity2.set_id("23456");
        testEntity2.setStringField("shit");

        testEntity.setTestEntity2(testEntity2);

        Mongo m = new Mongo("localhost", 27017);
        DB db = m.getDB("mydb1");
        DBCollection coll = db.getCollection("testCollection");
        coll.drop();

        DBObject dbObject = Converter.toDBObject(testEntity);
        coll.save(dbObject);

        DBObject newDbObject = coll.findOne();

        TestEntity convertedTestEntity = Converter.toObject(TestEntity.class, newDbObject);

        assert convertedTestEntity != null;
        assert convertedTestEntity.get_id().equals(testEntity.get_id());
        assert convertedTestEntity.isBooleanField() == testEntity.isBooleanField();
        assert convertedTestEntity.getDoubleField() == testEntity.getDoubleField();
        assert convertedTestEntity.getIntegerField().equals(testEntity.getIntegerField());
        assert convertedTestEntity.getIntField() == testEntity.getIntField();
        assert convertedTestEntity.getStringField().equals(testEntity.getStringField());

        assert convertedTestEntity.getTestEntity2() != null;
        assert convertedTestEntity.getTestEntity2().get_id().equals(testEntity2.get_id());
        assert convertedTestEntity.getTestEntity2().getStringField().equals(testEntity2.getStringField());
    }

    @Test
    public void test4() throws UnknownHostException {
        TestEntity5 testEntity5 = new TestEntity5();
        testEntity5.set_id("12345678");
        testEntity5.setStringList(Arrays.asList(new String[] { "s1", "s2", "s3"}));

        Mongo m = new Mongo("localhost", 27017);
        DB db = m.getDB("mydb1");
        DBCollection coll = db.getCollection("testCollection");
        coll.drop();

        DBObject dbObject = Converter.toDBObject(testEntity5);
        coll.save(dbObject);

        DBObject newDbObject = coll.findOne();
        TestEntity5 newTestEntity5 = Converter.toObject(TestEntity5.class, newDbObject);
        assert newTestEntity5 != null;
        List<String> stringList = newTestEntity5.getStringList();
        assert stringList != null;
        assert stringList.size() == 3;
        assert stringList.get(0).equals("s1");
        assert stringList.get(1).equals("s2");
        assert stringList.get(2).equals("s3");

    }

    @Test
    public void test5() throws UnknownHostException {
        TestEntity5 testEntity5 = new TestEntity5();
        testEntity5.set_id("12345678");

        List<TestEntity4> testEntity4List = new ArrayList();
        testEntity4List.add(new TestEntity4("e1"));
        testEntity4List.add(new TestEntity4("e2"));
        testEntity4List.add(new TestEntity4("e3"));

        testEntity5.setEntity4List(testEntity4List);

        Mongo m = new Mongo("localhost", 27017);
        DB db = m.getDB("mydb1");
        DBCollection coll = db.getCollection("testCollection");
        coll.drop();

        DBObject dbObject = Converter.toDBObject(testEntity5);
        coll.save(dbObject);

        DBObject newDbObject = coll.findOne();
        TestEntity5 newTestEntity5 = Converter.toObject(TestEntity5.class, newDbObject);
        assert newTestEntity5 != null;
        List<TestEntity4> newTestEntity4List = testEntity5.getEntity4List();
        assert newTestEntity4List != null;
        assert newTestEntity4List.size() == 3;
        assert newTestEntity4List.get(0).getStringField().equals("e1");
        assert newTestEntity4List.get(1).getStringField().equals("e2");
        assert newTestEntity4List.get(2).getStringField().equals("e3");

    }

    @Test
    public void test6() throws UnknownHostException {
        TestEntity5 testEntity5 = new TestEntity5();
        testEntity5.set_id("12345678");

        Map<String,String> stringToStringMap = new HashMap();
        stringToStringMap.put("key1", "value1");
        stringToStringMap.put("key2", "value2");

        testEntity5.setStringToStringMap(stringToStringMap);

        Mongo m = new Mongo("localhost", 27017);
        DB db = m.getDB("mydb1");
        DBCollection coll = db.getCollection("testCollection");
        coll.drop();

        DBObject dbObject = Converter.toDBObject(testEntity5);
        coll.save(dbObject);

        DBObject newDbObject = coll.findOne();
        TestEntity5 newTestEntity5 = Converter.toObject(TestEntity5.class, newDbObject);
        assert newTestEntity5 != null;
        Map<String, String> newMap = newTestEntity5.getStringToStringMap();

        assert newMap != null;
        assert newMap.size() == 2;
        assert newMap.get("key1").equals("value1");
        assert newMap.get("key2").equals("value2");

    }

    @Test
    public void testEmbeddedAndEnums() throws Exception {
        TestEntity6 t = new TestEntity6();
        t.set_id("bababa");
        t.setEmbeddedEnum(TestEntity6.TestEnumEmbeddedEntity.ONE);
        t.setEnumEntity(TestEnumEntity.FOUR);
        TestEntity6.TestEmbeddedEntity embedded = new TestEntity6.TestEmbeddedEntity();
        embedded.setValue1("value1");
        embedded.setValue2(12345);
        t.setEmbedded(embedded);

        Mongo m = new Mongo("localhost", 27017);
        DB db = m.getDB("mydb1");
        DBCollection coll = db.getCollection("testCollection");
        coll.drop();

        DBObject dbObject = Converter.toDBObject(t);
        coll.save(dbObject);

        DBObject newDbObject = coll.findOne();
        TestEntity6 newT = Converter.toObject(TestEntity6.class, newDbObject);
        assert newT != null;
        assert newT.get_id().equals("bababa");
        assert newT.getEmbeddedEnum() == TestEntity6.TestEnumEmbeddedEntity.ONE;
        assert newT.getEnumEntity() == TestEnumEntity.FOUR;
        assert newT.getEmbedded() != null;
        assert newT.getEmbedded().getValue1().equals("value1");
        assert newT.getEmbedded().getValue2() == 12345;

    }

    @Test
    public void testMap() throws Exception {

        TestEntity testEntity1_1 = new TestEntity();
        testEntity1_1.set_id("1_1");
        testEntity1_1.setBooleanField(true);
        testEntity1_1.setDoubleField(123456.789);
        testEntity1_1.setIntegerField(12345);
        testEntity1_1.setIntField(23456);
        testEntity1_1.setStringField("test string");
        testEntity1_1.setLongField(7890123L);

        TestEntity2 testEntity2_1 = new TestEntity2();
        testEntity2_1.set_id("2_1");
        testEntity2_1.setStringField("shit");

        testEntity1_1.setTestEntity2(testEntity2_1);

        TestEntity testEntity1_2 = new TestEntity();
        testEntity1_2.set_id("1_2");
        testEntity1_2.setBooleanField(true);
        testEntity1_2.setDoubleField(123456.789);
        testEntity1_2.setIntegerField(12345);
        testEntity1_2.setIntField(23456);
        testEntity1_2.setStringField("test string");
        testEntity1_2.setLongField(7890123L);

        TestEntity2 testEntity2_2 = new TestEntity2();
        testEntity2_2.set_id("2_2");
        testEntity2_2.setStringField("shit");

        testEntity1_2.setTestEntity2(testEntity2_2);

        TestEntity7 t = new TestEntity7();
        t.set_id("bababa");
        Map<TestEnumEntity, TestEntity> map = new HashMap<TestEnumEntity, TestEntity>();
        map.put(TestEnumEntity.FIVE, testEntity1_1);
        map.put(TestEnumEntity.FOUR, testEntity1_2);
        t.setEnumToEntityMap(map);

        Mongo m = new Mongo("localhost", 27017);
        DB db = m.getDB("mydb1");
        DBCollection coll = db.getCollection("testCollection");
        coll.drop();

        DBObject dbObject = Converter.toDBObject(t);
        coll.save(dbObject);

        DBObject newDbObject = coll.findOne();
        TestEntity7 newT = Converter.toObject(TestEntity7.class, newDbObject);
        assertNotNull(newT);
        assertEquals("bababa", newT.get_id());
        assertNotNull(newT.getEnumToEntityMap());
        assertEquals(newT.getEnumToEntityMap().size(), 2);

        TestEntity t1 = newT.getEnumToEntityMap().get(TestEnumEntity.FIVE);
        TestEntity t2 = newT.getEnumToEntityMap().get(TestEnumEntity.FOUR);

        assertNotNull(t1);
        assertNotNull(t2);

        assertEquals("1_1", t1.get_id());
        assertEquals("1_2", t2.get_id());

        assertEquals("2_1", t1.getTestEntity2().get_id());
        assertEquals("2_2", t2.getTestEntity2().get_id());



    }

}
