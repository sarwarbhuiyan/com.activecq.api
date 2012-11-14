<%@page session="false"
        import="com.activecq.api.testing.ActiveComponentTestStub"
%><%@include file="/apps/activecq/api/testing/global/global.jsp" %><%
/** Begin Component Setup **/
ActiveComponentTestStub c = new ActiveComponentTestStub(slingRequest);
/** End Component Setup **/
%>

<!-- Begin Test -->
    <%= c.Plugins.XSS.protect("<script>alert('this is an xss attack!');</script>") %>
<!-- End Test -->