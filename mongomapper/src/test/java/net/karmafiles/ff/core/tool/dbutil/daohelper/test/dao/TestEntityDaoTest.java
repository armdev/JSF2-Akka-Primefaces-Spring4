package net.karmafiles.ff.core.tool.dbutil.daohelper.test.dao;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import net.karmafiles.ff.core.tool.IdGenerator;
import net.karmafiles.ff.core.tool.dbutil.ConnectionImpl;
import net.karmafiles.ff.core.tool.dbutil.MongoConnection;
import net.karmafiles.ff.core.tool.dbutil.daohelper.DaoHelper;
import net.karmafiles.ff.core.tool.dbutil.daohelper.test.entity.EmbeddedTestEntity;
import net.karmafiles.ff.core.tool.dbutil.daohelper.test.entity.TestEntity;
import net.karmafiles.ff.core.tool.dbutil.daohelper.DaoException;
import net.karmafiles.ff.core.tool.dbutil.daohelper.FieldValueChecker;
import net.karmafiles.ff.core.tool.dbutil.daohelper.FieldValueProvider;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.UnknownHostException;

/**
 * Created by Ilya Brodotsky
 * Date: 17.10.2010
 * Time: 20:11:29
 * <p/>
 * All rights reserved.
 * <p/>
 * Contact me:
 * email, jabber: ilya.brodotsky@gmail.com
 * skype: ilya.brodotsky
 */

@Test
public class TestEntityDaoTest {

    @BeforeTest
    protected void setUp() throws Exception {
        try { assert true == false; throw new RuntimeException("Please enable assertions.");} catch(AssertionError e) {}
    }

    
    
    public static class Connection implements MongoConnection {
        private Mongo mongo;
        private DB db;


        private void connect() {
            try {
                mongo = new Mongo("localhost", 27017);
                db = mongo.getDB("vv");
            } catch (UnknownHostException e) {
                throw new DaoException(e);
            }
        }

        public DBCollection getCollection(String name) {
            return db.getCollection("blogCollection");
        }

        public void dropDatabase() {
            db.dropDatabase();
        }
    }


    public static class TestEntityHelper<TestEntity> extends DaoHelper<TestEntity> {

        private ConnectionImpl connection;

        public ConnectionImpl getConnection() {
            return connection;
        }

        public void setConnection(ConnectionImpl connection) {
            this.connection = connection;
        }

        public void init(Class<TestEntity> type, String dbCollectionName) {
            super.init(type, dbCollectionName);

            addUniqueField("stringField");
            addNotNullField("notNullField");
            addFieldValueChecker("urlField", new FieldValueChecker() {
                public void check(DBCollection dbCollection, DBObject dbObject, String fieldName) {
                    String url = (String)dbObject.get(fieldName);
                    if(url != null && !url.startsWith("http://")) {
                        throw new DaoException("Invalid url format.");
                    }
                }
            });
            renewFieldOnCreation("embeddedEntity", new FieldValueProvider(){
                public Object provide(DBCollection dbCollection, DBObject dbObject, String fieldName) {
                    return new EmbeddedTestEntity("eee");
                }
            });

            
        }

        @Override
        public String generateNewId() {
            return "test-" + IdGenerator.createSecureId();
        }
    }

    public TestEntity createNew(TestEntityHelper<TestEntity> testEntityHelper,
                                String stringField,
                                String urlField,
                                String notNullField) {
        TestEntity testEntity = new TestEntity();
        //testEntity.set_id(testEntityHelper.generateNewId());
        testEntity.setStringField(stringField);
        testEntity.setUrlField(urlField);
        testEntity.setNotNullField(notNullField);

        return testEntityHelper.add(testEntity);
    }

    @Test
    public void test() {
        ConnectionImpl connection = new ConnectionImpl();
        connection.setConnectionDatabase("ffcoretest");
        connection.setConnectionHost("localhost");
        connection.setConnectionPort(27017);

        connection.connect();
        connection.getCollection("blogCollection").drop();


        TestEntityHelper<TestEntity> testEntityHelper = new TestEntityHelper<TestEntity>();
        testEntityHelper.setConnection(connection);
        testEntityHelper.init(TestEntity.class, "blogCollection");

        String id = createNew(testEntityHelper, "stringField1", "http://url", "something").get_id();
        assert id != null;
        createNew(testEntityHelper,  "stringField2", "http://url", "something");

        try {
            createNew(testEntityHelper,  "stringField2", "http://url", "something");
            assert true==false; // duplicate "stringField2"
        } catch(Throwable t) {}
        
        try {
            createNew(testEntityHelper,  "stringField3", null, "something"); // ok
            createNew(testEntityHelper,  "stringField4", "http://url", null); // not null constraint failed
            assert true == false; // duplicate "stringField2"
        } catch(Throwable t) {}

        try {
            createNew(testEntityHelper,  "stringField5", "ftp://url", "something"); // not null constraint failed
            assert true == false;
        } catch (Throwable t) {}

        TestEntity t1 = testEntityHelper.get(id);
        assert t1 != null;
        assert t1.getEmbeddedEntity() != null;
        assert t1.getEmbeddedEntity().geteStringField().equals("eee");
    }

}
