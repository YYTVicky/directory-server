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


import java.util.Hashtable;

import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

import org.apache.directory.server.core.CoreSession;
import org.apache.directory.server.core.DirectoryService;
import org.apache.directory.server.i18n.I18n;
import org.apache.directory.shared.ldap.constants.AuthenticationLevel;
import org.apache.directory.shared.ldap.jndi.JndiUtils;
import org.apache.directory.shared.ldap.name.LdapDN;
import org.apache.directory.shared.ldap.util.StringTools;


/**
 * A simplistic implementation of {@link AbstractContextFactory}.
 * This class simply extends {@link AbstractContextFactory} and leaves all
 * abstract event listener methods as empty.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$
 */
public class CoreContextFactory implements InitialContextFactory
{
    public synchronized Context getInitialContext( Hashtable env ) throws NamingException
    {
        env = ( Hashtable<String, Object> ) env.clone();
        LdapDN principalDn = new LdapDN( getPrincipal( env ) );
        byte[] credential = getCredential( env );
        String authentication = getAuthentication( env );
        String providerUrl = getProviderUrl( env );

        DirectoryService service = ( DirectoryService ) env.get( DirectoryService.JNDI_KEY );

        if ( service == null )
        {
            throw new ConfigurationException( I18n.err( I18n.ERR_477, env ) );
        }

        if ( ! service.isStarted() )
        {
            return new DeadContext();
        }

        ServerLdapContext ctx = null;
        try
        {
            CoreSession session = service.getSession( principalDn, credential );
            ctx = new ServerLdapContext( service, session, new LdapDN( providerUrl ) );
        }
        catch ( Exception e )
        {
            JndiUtils.wrap( e );
        }

        // check to make sure we have access to the specified dn in provider URL
        ctx.lookup( "" );
        return ctx;        
    }


    public static String getProviderUrl( Hashtable<String, Object> env )
    {
        String providerUrl;
        Object value;
        value = env.get( Context.PROVIDER_URL );
        if ( value == null )
        {
            value = "";
        }
        providerUrl = value.toString();

        env.put( Context.PROVIDER_URL, providerUrl );

        return providerUrl;
    }


    public static String getAuthentication( Hashtable<String, Object> env )
    {
        String authentication;
        Object value = env.get( Context.SECURITY_AUTHENTICATION );
        if ( value == null )
        {
            authentication = AuthenticationLevel.NONE.toString();
        }
        else
        {
            authentication = value.toString();
        }

        env.put( Context.SECURITY_AUTHENTICATION, authentication );

        return authentication;
    }


    public static byte[] getCredential( Hashtable<String, Object> env ) throws javax.naming.ConfigurationException
    {
        byte[] credential;
        Object value = env.get( Context.SECURITY_CREDENTIALS );
        if ( value == null )
        {
            credential = null;
        }
        else if ( value instanceof String )
        {
            credential = StringTools.getBytesUtf8( (String)value );
        }
        else if ( value instanceof byte[] )
        {
            credential = ( byte[] ) value;
        }
        else
        {
            throw new javax.naming.ConfigurationException( I18n.err( I18n.ERR_478, Context.SECURITY_CREDENTIALS ) );
        }

        if ( credential != null )
        {
            env.put( Context.SECURITY_CREDENTIALS, credential );
        }

        return credential;
    }


    public static String getPrincipal( Hashtable<String,Object> env )
    {
        String principal;
        Object value = env.get( Context.SECURITY_PRINCIPAL );
        if ( value == null )
        {
            principal = null;
        }
        else
        {
            principal = value.toString();
            env.put( Context.SECURITY_PRINCIPAL, principal );
        }

        return principal;
    }
}
