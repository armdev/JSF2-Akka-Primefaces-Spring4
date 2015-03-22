/*
 * Copyright 2011 Dadastream. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY DADASTREAM ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL DADASTREAM OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of Dadastream.
 *
 * @author Ilya Brodotsky
 * @author Timur Evdokimov
 */

package net.karmafiles.ff.core.tool.dbutil;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.ServerAddress;
import net.karmafiles.ff.core.tool.dbutil.daohelper.DaoException;

import javax.annotation.PostConstruct;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionImpl implements MongoConnection {

    private DB db;

    private String connectionHost;
    private Integer connectionPort;
    private String connectionDatabase;
    private String connectionDescriptor;

    public void setConnectionDatabase(String connectionDatabase) {
        this.connectionDatabase = connectionDatabase;
    }

    public void setConnectionPort(Integer connectionPort) {
        this.connectionPort = connectionPort;
    }

    public void setConnectionHost(String connectionHost) {
        this.connectionHost = connectionHost;
    }

    public String getConnectionDescriptor() {
        return connectionDescriptor;
    }

    public void setConnectionDescriptor(String connectionDescriptor) {
        this.connectionDescriptor = connectionDescriptor;
    }

    @PostConstruct
    public void connect() {
        try {
            Mongo mongo;
            if (connectionDescriptor != null) {
                String[] hosts = connectionDescriptor.split(",");
                List<ServerAddress> addr = new ArrayList<ServerAddress>();
                for (String host: hosts) {
                    String[] hostPortPair = host.split(":");
                    int port = 27017;
                    if (hostPortPair.length > 1) {
                        try {
                            port = Integer.parseInt(hostPortPair[1]);
                        } catch (NumberFormatException e) {
                            // port doesn't look as port
                        }
                    }
                    addr.add(new ServerAddress(host, port));
                }
                mongo = new Mongo(addr);
            } else {
                mongo = new Mongo(connectionHost, connectionPort);
            }
            db = mongo.getDB(connectionDatabase);
        } catch (UnknownHostException e) {
            throw new DaoException(e);
        }
    }

    public DBCollection getCollection(String name) {
        return db.getCollection(name);
    }

    public void dropDatabase() {
        db.dropDatabase();
    }
}
