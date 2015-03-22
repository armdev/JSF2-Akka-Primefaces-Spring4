MongoMapper
===========

This library provides clean and powerful mapping between Java POJOs and MongoDB DBObject.

Features
--------

* Just 2 methods of API for mapping to and from
* No annotations, no factories, just POJOs all the way
* Full support of Java collections: Maps, Lists, Sets
* Full support of polymorphism: generified collections containing different subclasses, complex beans containing polymorphic attributes
* Convenient template for DAO classes

Using raw converter API
-----------------------

    import com.mongodb.*;
    import net.karmafiles.ff.core.tool.dbutil.converter.Converter;

    ...

    MyTopLevelEntity entity = ...
    // here the graph of objects is created, starting from top-level entity

    Mongo m = new Mongo("localhost", 27017);
    DB db = m.getDB("mydb1");
    DBCollection coll = db.getCollection("testCollection");

    // convert entitiy to MongoDB DBObject

    DBObject dbObject = Converter.toDBObject(entity);
    coll.save(dbObject);

    // restore entity from MongoDB DBObject
    DBObject newDbObject = coll.findOne();
    MyTopLevelEntity newEntity = Converter.toObject(MyTopLevelEntity.class, newDbObject);

More examples in test directory under ...converter.test package.

Using DAO template
------------------

    import net.karmafiles.ff.core.tool.dbutil.ConnectionImpl;
    import net.karmafiles.ff.core.tool.dbutil.daohelper.DaoHelper;

    import java.util.Date;

    public class JobDao<T> extends DaoHelper<T> {

        // provide some method of getting a connection to MongoDB
        // e.g. Spring @Autowire

        private ConnectionImpl connection;

        public ConnectionImpl getConnection() {
            return connection;
        }

        public void setConnection(ConnectionImpl connection) {
            this.connection = connection;
        }


        // custom find method
        public List<Job> findNewJobs() {

            BasicDBObject query = new BasicDBObject();
            query.put("state", JobState.NOT_STARTED.toString());
            DBCursor c = getDbCollection().find(query);

            List<Job> jobs = new ArrayList<Job>();
            while (c.hasNext()) {
                jobs.add(Converter.toObject(Job.class, c.next()));
            }
            return jobs;

        }

        // custom update method
        public Job update(Job entity) {
            Job oldJob = get(entity.get_id());
            if (oldJob.getModified().after(entity.getModified())) {
                throw new IllegalStateException("Job " + entity.get_id() + " copy in the database is newer than in request, can't update");
            }
            return super.update(entity);
        }


    }

More examples in TestEntityDaoTest.

Caveats
-------

In order to prevent endless looping in some polymorhpic cases, Converter.toObject() creates a clean bean copy:

    PropertyUtils.copyProperties(copyObject, newObject);

This is rather a kludge than a proper solution.