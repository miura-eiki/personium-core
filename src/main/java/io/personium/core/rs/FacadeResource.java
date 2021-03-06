/**
 * personium.io
 * Copyright 2014-2018 FUJITSU LIMITED
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
 */
package io.personium.core.rs;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.CookieParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.personium.common.utils.PersoniumCoreUtils;
import io.personium.core.PersoniumCoreException;
import io.personium.core.PersoniumUnitConfig;
import io.personium.core.auth.AccessContext;
import io.personium.core.model.Cell;
import io.personium.core.model.ModelFactory;
import io.personium.core.model.lock.CellLockManager;
import io.personium.core.rs.cell.CellResource;
import io.personium.core.rs.unit.UnitResource;
import io.personium.core.utils.ResourceUtils;

/**
 * Jax - RS Resource which will be the entrance of all requests.
 * Here, acquisition of URL information and acquisition of Authorization header are performed,
 * We pass these information to subresources.
 */
@Path("")
public class FacadeResource {

    /** Logger. */
    static Logger log = LoggerFactory.getLogger(FacadeResource.class);

    /** For cookie authentication, the key of the information embedded in the cookie. */
    public static final String P_COOKIE_KEY = "p_cookie";
    /** The key specified in the query parameter during cookie authentication. */
    public static final String COOKIE_PEER_QUERY_KEY = "p_cookie_peer";

//    /**
//     * @param cookieAuthValue クッキー内の p_cookieキーに指定された値
//     * @param cookiePeer p_cookie_peerクエリに指定された値
//     * @param authzHeaderValue Authorization ヘッダ
//     * @param host Host ヘッダ
//     * @param uriInfo UriInfo
//     * @param xPersoniumUnitUser X-Personium-UnitUser header
//     * @param xPersoniumRequestKey X-Personium-RequestKey header
//     * @param xPersoniumEventId X-Personium-EventId header
//     * @param xPersoniumRuleChain X-Personium-RuleChain header
//     * @param xPersoniumVia X-Personium-Via header
//     * @param httpServletRequest HttpServletRequest
//     * @return CellResourceオブジェクトまたはResponseオブジェクト
//     */
//    @Path("{path1}")
//    public final Object facade(
//            @CookieParam(P_COOKIE_KEY) final String cookieAuthValue,
//            @QueryParam(COOKIE_PEER_QUERY_KEY) final String cookiePeer,
//            @HeaderParam(HttpHeaders.AUTHORIZATION) final String authzHeaderValue,
//            @HeaderParam(HttpHeaders.HOST) final String host,
//            @HeaderParam(PersoniumCoreUtils.HttpHeaders.X_PERSONIUM_UNIT_USER) final String xPersoniumUnitUser,
//            @HeaderParam(PersoniumCoreUtils.HttpHeaders.X_PERSONIUM_REQUESTKEY) final String xPersoniumRequestKey,
//            @HeaderParam(PersoniumCoreUtils.HttpHeaders.X_PERSONIUM_EVENTID) final String xPersoniumEventId,
//            @HeaderParam(PersoniumCoreUtils.HttpHeaders.X_PERSONIUM_RULECHAIN) final String xPersoniumRuleChain,
//            @HeaderParam(PersoniumCoreUtils.HttpHeaders.X_PERSONIUM_VIA) final String xPersoniumVia,
//            @Context final UriInfo uriInfo,
//            @Context HttpServletRequest httpServletRequest) {
//        Cell cell = ModelFactory.cell(uriInfo);
//        AccessContext ac = AccessContext.create(authzHeaderValue,
//                uriInfo, cookiePeer, cookieAuthValue, cell, uriInfo.getBaseUri().toString(),
//                host, xPersoniumUnitUser);
//        if (cell == null) {
//            throw PersoniumCoreException.Dav.CELL_NOT_FOUND;
//        }
//
//        CellLockManager.STATUS cellStatus = CellLockManager.getCellStatus(cell.getId());
//        if (CellLockManager.STATUS.BULK_DELETION.equals(cellStatus)) {
//            throw PersoniumCoreException.Dav.CELL_NOT_FOUND;
//        }
//
//        CellLockManager.incrementReferenceCount(cell.getId());
//        httpServletRequest.setAttribute("cellId", cell.getId());
//        if (xPersoniumRequestKey != null) {
//            ResourceUtils.validateXPersoniumRequestKey(xPersoniumRequestKey);
//        }
//        return new CellResource(ac, xPersoniumRequestKey,
//                xPersoniumEventId, xPersoniumRuleChain, xPersoniumVia, httpServletRequest);
//    }

    /**
     * @param cookieAuthValue The value specified for the p_cookie key in the cookie
     * @param cookiePeer p_cookie_peer Value specified in the query
     * @param headerAuthz Authorization header
     * @param headerHost Host header
     * @param uriInfo UriInfo
     * @param headerPersoniumUnitUser X-Personium-UnitUser header
     * @param headerPersoniumRequestKey X-Personium-RequestKey header
     * @param headerPersoniumEventId X-Personium-EventId header
     * @param headerPersoniumRuleChain X-Personium-RuleChain header
     * @param headerPersoniumVia X-Personium-Via header
     * @param httpServletRequest HttpServletRequest
     * @return CellResource object or Response object
     */
    @Path("/")
    public Object facade(
            @CookieParam(P_COOKIE_KEY) final String cookieAuthValue,
            @QueryParam(COOKIE_PEER_QUERY_KEY) final String cookiePeer,
            @HeaderParam(HttpHeaders.AUTHORIZATION) final String headerAuthz,
            @HeaderParam(HttpHeaders.HOST) final String headerHost,
            @HeaderParam(PersoniumCoreUtils.HttpHeaders.X_PERSONIUM_UNIT_USER) final String headerPersoniumUnitUser,
            @HeaderParam(PersoniumCoreUtils.HttpHeaders.X_PERSONIUM_REQUESTKEY) final String headerPersoniumRequestKey,
            @HeaderParam(PersoniumCoreUtils.HttpHeaders.X_PERSONIUM_EVENTID) final String headerPersoniumEventId,
            @HeaderParam(PersoniumCoreUtils.HttpHeaders.X_PERSONIUM_RULECHAIN) final String headerPersoniumRuleChain,
            @HeaderParam(PersoniumCoreUtils.HttpHeaders.X_PERSONIUM_VIA) final String headerPersoniumVia,
            @Context final UriInfo uriInfo,
            @Context HttpServletRequest httpServletRequest) {

        if (PersoniumUnitConfig.isPathBasedCellUrlEnabled()) {
            return new UnitResource(cookieAuthValue, cookiePeer, headerAuthz, headerHost,
                    headerPersoniumUnitUser, uriInfo);
        }

        String accessUrl = uriInfo.getBaseUri().toString();
        String configUrl = PersoniumUnitConfig.getBaseUrl();
        if (configUrl.equals(accessUrl)) {
            return new UnitResource(cookieAuthValue, cookiePeer, headerAuthz, headerHost,
                    headerPersoniumUnitUser, uriInfo);
        } else {
            // {CellName}.{FQDN} access
            String cellName = headerHost.split("\\.")[0];
            Cell cell = ModelFactory.cellFromName(cellName);
            if (cell == null) {
                throw PersoniumCoreException.Dav.CELL_NOT_FOUND;
            }
            AccessContext ac = AccessContext.create(headerAuthz, uriInfo, cookiePeer, cookieAuthValue, cell,
                    uriInfo.getBaseUri().toString(), headerHost, headerPersoniumUnitUser);

            CellLockManager.STATUS cellStatus = CellLockManager.getCellStatus(cell.getId());
            if (CellLockManager.STATUS.BULK_DELETION.equals(cellStatus)) {
                throw PersoniumCoreException.Dav.CELL_NOT_FOUND;
            }

            CellLockManager.incrementReferenceCount(cell.getId());
            httpServletRequest.setAttribute("cellId", cell.getId());
            if (headerPersoniumRequestKey != null) {
                ResourceUtils.validateXPersoniumRequestKey(headerPersoniumRequestKey);
            }
            return new CellResource(ac, headerPersoniumRequestKey,
                    headerPersoniumEventId, headerPersoniumRuleChain, headerPersoniumVia, httpServletRequest);
        }
    }

//    /**
//     * @param cookieAuthValue The value specified for the p_cookie key in the cookie
//     * @param cookiePeer p_cookie_peer Value specified in the query
//     * @param authzHeaderValue Authorization header
//     * @param host Host header
//     * @param xPersoniumUnitUser header
//     * @param uriInfo UriInfo
//     * @return UnitCtlResource object
//     */
//    @Path("__ctl")
//    public final UnitCtlResource ctl(
//            @CookieParam(P_COOKIE_KEY) final String cookieAuthValue,
//            @QueryParam(COOKIE_PEER_QUERY_KEY) final String cookiePeer,
//            @HeaderParam(HttpHeaders.AUTHORIZATION) final String authzHeaderValue,
//            @HeaderParam(HttpHeaders.HOST) final String host,
//            @HeaderParam(PersoniumCoreUtils.HttpHeaders.X_PERSONIUM_UNIT_USER) final String xPersoniumUnitUser,
//            @Context final UriInfo uriInfo) {
//        AccessContext ac = AccessContext.create(authzHeaderValue,
//                uriInfo, cookiePeer, cookieAuthValue, null, uriInfo.getBaseUri().toString(),
//                host, xPersoniumUnitUser);
//        return new UnitCtlResource(ac, uriInfo);
//    }
//
//    /**
//     * @param authzHeaderValue Authorization header
//     * @param host Host header
//     * @param xPersoniumUnitUser header
//     * @param uriInfo UriInfo
//     * @return UnitCtlResource object
//     */
//    @Path("__status")
//    public final StatusResource status(
//            @HeaderParam(HttpHeaders.AUTHORIZATION) final String authzHeaderValue,
//            @HeaderParam(HttpHeaders.HOST) final String host,
//            @HeaderParam(PersoniumCoreUtils.HttpHeaders.X_PERSONIUM_UNIT_USER) final String xPersoniumUnitUser,
//            @Context final UriInfo uriInfo) {
//        return new StatusResource();
//    }
//
//    static final String CROSSDOMAIN_XML = PersoniumCoreUtils.readStringResource("crossdomain.xml", CharEncoding.UTF_8);
//
//    /**
//     * Returns Crossdomain.xml.
//     * @return String of Crossdomain.xml.
//     */
//    @Path("crossdomain.xml")
//    @Produces(MediaType.APPLICATION_XML)
//    @GET
//    public final String crosdomainXml() {
//        return CROSSDOMAIN_XML;
//    }
}
