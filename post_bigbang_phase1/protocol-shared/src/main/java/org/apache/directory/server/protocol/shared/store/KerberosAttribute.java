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
package org.apache.directory.server.protocol.shared.store;


/**
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class KerberosAttribute
{
    // ------------------------------------------------------------------------
    // Krb5 Schema Attributes
    // ------------------------------------------------------------------------

    /** the krb5kdc schema key for a krb5KDCEntry */
    public static final String KEY = "krb5Key";
    /** the krb5kdc schema key encryption type for a krb5KDCEntry */
    public static final String TYPE = "krb5EncryptionType";
    /** the krb5kdc schema principal name for a krb5KDCEntry */
    public static final String PRINCIPAL = "krb5PrincipalName";
    /** the krb5kdc schema key version identifier for a krb5KDCEntry */
    public static final String VERSION = "krb5KeyVersionNumber";
    /** the Apache specific SAM type attribute */
    public static final String SAM_TYPE = "apacheSamType";
}
