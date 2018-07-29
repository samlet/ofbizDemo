package com.sagas.ofbizdemo.events;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import graphql.ExecutionResult;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.webapp.event.EventHandlerException;

import java.io.Writer;

public class OfbizDemoEvents {

    public static final String module = OfbizDemoEvents.class.getName();

    public static String graphqlEvent(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

        String queryString = request.getParameter("query");
        if (UtilValidate.isEmpty(queryString) ) {
            String errMsg = "Query string are required fields on the form and can't be empty.";
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }

        try {
            response.setContentType("text/xml");
            Writer out = response.getWriter();
            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.write("<methodResponse>");
            out.write("<params><param>");
            out.write("<value><string><![CDATA[");

            ExecutionResult executionResult=GraphQLManager.getInstance().execute(queryString);
            out.write(executionResult.getData().toString());

            out.write("]]></string></value>");
            out.write("</param></params>");
            out.write("</methodResponse>");
            out.flush();
        } catch (Exception e) {
            String errMsg = "Unable to create new records in OfbizDemo entity: " + e.toString();
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }

        request.setAttribute("_EVENT_MESSAGE_", "query succesfully.");
        return "success";
    }

    public static String createOfbizDemoEvent(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

        String ofbizDemoTypeId = request.getParameter("ofbizDemoTypeId");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        if (UtilValidate.isEmpty(firstName) || UtilValidate.isEmpty(lastName)) {
            String errMsg = "First Name and Last Name are required fields on the form and can't be empty.";
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }
        String comments = request.getParameter("comments");

        try {
            Debug.logInfo("=======Creating OfbizDemo record in event using service createOfbizDemoByGroovyService=========", module);
            dispatcher.runSync("createOfbizDemoByGroovyService", UtilMisc.toMap("ofbizDemoTypeId", ofbizDemoTypeId,
                    "firstName", firstName, "lastName", lastName, "comments", comments, "userLogin", userLogin));
        } catch (GenericServiceException e) {
            String errMsg = "Unable to create new records in OfbizDemo entity: " + e.toString();
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }
        request.setAttribute("_EVENT_MESSAGE_", "OFBiz Demo created succesfully.");
        return "success";
    }
}
