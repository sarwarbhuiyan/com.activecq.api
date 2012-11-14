<%@page session="false" 
        import="com.activecq.api.testing.ActiveComponentTestStub"
%><%@include file="/apps/activecq/api/testing/global/global.jsp" %><%
/** Begin Component Setup **/
ActiveComponentTestStub c = new ActiveComponentTestStub(slingRequest);
/** End Component Setup **/
%>

<%-- Edit Context IS NOT AVAILABLE DURING TESTING --%>

<!-- Begin Test -->
    <%= c.getEditContext() == null %>
<!-- End Test -->
