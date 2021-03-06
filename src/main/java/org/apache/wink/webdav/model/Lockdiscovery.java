/**
 * personium.io
 * Modifications copyright 2018 FUJITSU LIMITED
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * --------------------------------------------------
 * This code is based on Lockdiscovery.java of wink-webdav, and some modifications
 * for personium.io are applied by us.
 * --------------------------------------------------
 * The copyright and the license text of the original code is as follows:
 */
/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 *******************************************************************************/
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.1-b02-fcs
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2008.12.04 at 02:20:17 PM IST
//

package org.apache.wink.webdav.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The <code>lockdiscovery</code> Property per the WebDAV specification [RFC
 * 4918]
 *
 * <pre>
 *    Name:       lockdiscovery
 *    Namespace:  DAV:
 *    Purpose:    Describes the active locks on a resource
 *    Description: The lockdiscovery property returns a listing of who has
 *    a lock, what type of lock he has, the timeout type and the time
 *    remaining on the timeout, and the associated lock token.  The server
 *    is free to withhold any or all of this information if the requesting
 *    principal does not have sufficient access rights to see the requested
 *    data.
 *
 *    &lt;!ELEMENT lockdiscovery (activelock)* &gt;
 *
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"activelock"})
@XmlRootElement(namespace = "DAV:", name = "lockdiscovery")
public class Lockdiscovery {

    @XmlElements({@XmlElement(namespace = "DAV:", name = "activelock", type = Activelock.class) })
    protected List<Activelock> activelock;

    /**
     * Gets the value of the activelock property.
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the activelock property.
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getActivelock().add(newItem);
     * </pre>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Activelock }
     */
    public List<Activelock> getActivelock() {
        if (activelock == null) {
            activelock = new ArrayList<Activelock>();
        }
        return this.activelock;
    }

}
