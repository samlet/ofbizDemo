package com.sagas.ofbizdemo.events;

import graphql.ExecutionResult;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;

public class GraphQLEvents {

    public static final String module = GraphQLEvents.class.getName();

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
            response.setContentType("application/json; charset=utf-8");
            Writer out = response.getWriter();

            ExecutionResult executionResult=GraphQLManager.getInstance().execute(queryString);
            out.write(executionResult.getData().toString());

            out.flush();
        } catch (Exception e) {
            String errMsg = "Unable to create new records in OfbizDemo entity: " + e.toString();
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }

        request.setAttribute("_EVENT_MESSAGE_", "query succesfully.");
        return "success";
    }

}
