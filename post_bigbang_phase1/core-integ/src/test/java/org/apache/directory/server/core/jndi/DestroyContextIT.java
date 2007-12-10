/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.apache.directory.server.core.jndi;


import org.apache.directory.server.core.DirectoryService;
import org.apache.directory.server.core.integ.CiRunner;
import static org.apache.directory.server.core.integ.IntegrationUtils.getSystemContext;
import org.apache.directory.shared.ldap.exception.LdapNameNotFoundException;
import org.apache.directory.shared.ldap.message.AttributeImpl;
import org.apache.directory.shared.ldap.message.AttributesImpl;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.ldap.LdapContext;


/**
 * Tests the destroyContext methods of the provider.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$
 */
@RunWith ( CiRunner.class )
public class DestroyContextIT
{
    public static DirectoryService service;

    
    /**
     * @todo Replace this with an LDIF directive!!!!!!
     *
     * @throws NamingException on error
     */
    public void createEntries() throws NamingException
    {
        LdapContext sysRoot = getSystemContext( service );

        /*
         * create ou=testing00,ou=system
         */
        Attributes attributes = new AttributesImpl( true );
        Attribute attribute = new AttributeImpl( "objectClass" );
        attribute.add( "top" );
        attribute.add( "organizationalUnit" );
        attributes.put( attribute );
        attributes.put( "ou", "testing00" );
        DirContext ctx = sysRoot.createSubcontext( "ou=testing00", attributes );
        assertNotNull( ctx );

        ctx = ( DirContext ) sysRoot.lookup( "ou=testing00" );
        assertNotNull( ctx );

        attributes = ctx.getAttributes( "" );
        assertNotNull( attributes );
        assertEquals( "testing00", attributes.get( "ou" ).get() );
        attribute = attributes.get( "objectClass" );
        assertNotNull( attribute );
        assertTrue( attribute.contains( "top" ) );
        assertTrue( attribute.contains( "organizationalUnit" ) );

        /*
         * create ou=testing01,ou=system
         */
        attributes = new AttributesImpl( true );
        attribute = new AttributeImpl( "objectClass" );
        attribute.add( "top" );
        attribute.add( "organizationalUnit" );
        attributes.put( attribute );
        attributes.put( "ou", "testing01" );
        ctx = sysRoot.createSubcontext( "ou=testing01", attributes );
        assertNotNull( ctx );

        ctx = ( DirContext ) sysRoot.lookup( "ou=testing01" );
        assertNotNull( ctx );

        attributes = ctx.getAttributes( "" );
        assertNotNull( attributes );
        assertEquals( "testing01", attributes.get( "ou" ).get() );
        attribute = attributes.get( "objectClass" );
        assertNotNull( attribute );
        assertTrue( attribute.contains( "top" ) );
        assertTrue( attribute.contains( "organizationalUnit" ) );

        /*
         * create ou=testing02,ou=system
         */
        attributes = new AttributesImpl( true );
        attribute = new AttributeImpl( "objectClass" );
        attribute.add( "top" );
        attribute.add( "organizationalUnit" );
        attributes.put( attribute );
        attributes.put( "ou", "testing02" );
        ctx = sysRoot.createSubcontext( "ou=testing02", attributes );
        assertNotNull( ctx );

        ctx = ( DirContext ) sysRoot.lookup( "ou=testing02" );
        assertNotNull( ctx );

        attributes = ctx.getAttributes( "" );
        assertNotNull( attributes );
        assertEquals( "testing02", attributes.get( "ou" ).get() );
        attribute = attributes.get( "objectClass" );
        assertNotNull( attribute );
        assertTrue( attribute.contains( "top" ) );
        assertTrue( attribute.contains( "organizationalUnit" ) );

        /*
         * create ou=subtest,ou=testing01,ou=system
         */
        ctx = ( DirContext ) sysRoot.lookup( "ou=testing01" );

        attributes = new AttributesImpl( true );
        attribute = new AttributeImpl( "objectClass" );
        attribute.add( "top" );
        attribute.add( "organizationalUnit" );
        attributes.put( attribute );
        attributes.put( "ou", "subtest" );
        ctx = ctx.createSubcontext( "ou=subtest", attributes );
        assertNotNull( ctx );

        ctx = ( DirContext ) sysRoot.lookup( "ou=subtest,ou=testing01" );
        assertNotNull( ctx );

        attributes = ctx.getAttributes( "" );
        assertNotNull( attributes );
        assertEquals( "subtest", attributes.get( "ou" ).get() );
        attribute = attributes.get( "objectClass" );
        assertNotNull( attribute );
        assertTrue( attribute.contains( "top" ) );
        assertTrue( attribute.contains( "organizationalUnit" ) );
    }


    /**
     * Tests the creation and subsequent read of a new JNDI context under the
     * system context root.
     *
     * @throws NamingException if there are failures
     */
    @Test
    public void testDestroyContext() throws NamingException
    {
        LdapContext sysRoot = getSystemContext( service );

        createEntries();

        /*
         * delete ou=testing00,ou=system
         */
        sysRoot.destroySubcontext( "ou=testing00" );

        try
        {
            sysRoot.lookup( "ou=testing00" );
            fail( "ou=testing00, ou=system should not exist" );
        }
        catch ( NamingException e )
        {
            assertTrue( e instanceof LdapNameNotFoundException );
        }

        /*
         * delete ou=subtest,ou=testing01,ou=system
         */
        sysRoot.destroySubcontext( "ou=subtest,ou=testing01" );

        try
        {
            sysRoot.lookup( "ou=subtest,ou=testing01" );
            fail( "ou=subtest,ou=testing01,ou=system should not exist" );
        }
        catch ( NamingException e )
        {
            assertTrue( e instanceof LdapNameNotFoundException );
        }

        /*
         * delete ou=testing01,ou=system
         */
        sysRoot.destroySubcontext( "ou=testing01" );

        try
        {
            sysRoot.lookup( "ou=testing01" );
            fail( "ou=testing01, ou=system should not exist" );
        }
        catch ( NamingException e )
        {
            assertTrue( e instanceof LdapNameNotFoundException );
        }

        /*
         * delete ou=testing01,ou=system
         */
        sysRoot.destroySubcontext( "ou=testing02" );

        try
        {
            sysRoot.lookup( "ou=testing02" );
            fail( "ou=testing02, ou=system should not exist" );
        }
        catch ( NamingException e )
        {
            assertTrue( e instanceof LdapNameNotFoundException );
        }
    }

}
