/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */
package org.apache.usergrid.persistence.collection.serialization.impl;


import org.apache.usergrid.persistence.collection.astyanax.AstyanaxKeyspaceProvider;
import org.apache.usergrid.persistence.collection.migration.Migration;
import org.apache.usergrid.persistence.collection.migration.MigrationManager;
import org.apache.usergrid.persistence.collection.migration.MigrationManagerImpl;
import org.apache.usergrid.persistence.collection.mvcc.MvccEntitySerializationStrategy;
import org.apache.usergrid.persistence.collection.mvcc.MvccLogEntrySerializationStrategy;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.netflix.astyanax.Keyspace;
import org.apache.usergrid.persistence.collection.mvcc.stage.write.UniqueValueSerializationStrategy;
import org.apache.usergrid.persistence.collection.mvcc.stage.write.UniqueValueSerializationStrategyImpl;


/**
 * @author tnine
 */
public class SerializationModule extends AbstractModule {

    @Override
    protected void configure() {

        // bind our keyspace to the AstyanaxKeyspaceProvider
        bind( Keyspace.class ).toProvider( AstyanaxKeyspaceProvider.class );

        // bind our migration manager
        bind( MigrationManager.class ).to( MigrationManagerImpl.class );

        // bind the serialization strategies
        bind( MvccEntitySerializationStrategy.class ).to( MvccEntitySerializationStrategyImpl.class );
        bind( MvccLogEntrySerializationStrategy.class ).to( MvccLogEntrySerializationStrategyImpl.class );
        bind( UniqueValueSerializationStrategy.class ).to( UniqueValueSerializationStrategyImpl.class );

        //do multibindings for migrations
        Multibinder<Migration> uriBinder = Multibinder.newSetBinder( binder(), Migration.class );
        uriBinder.addBinding().to( MvccEntitySerializationStrategyImpl.class );
        uriBinder.addBinding().to( MvccLogEntrySerializationStrategyImpl.class );
        uriBinder.addBinding().to( UniqueValueSerializationStrategyImpl.class );
    }
}
