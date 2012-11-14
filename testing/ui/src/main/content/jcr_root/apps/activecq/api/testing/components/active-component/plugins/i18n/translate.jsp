<%@page session="false"
        import="com.activecq.api.testing.ActiveComponentTestStub"
%><%@include file="/apps/activecq/api/testing/global/global.jsp" %><%
/** Begin Component Setup **/
ActiveComponentTestStub c = new ActiveComponentTestStub(slingRequest);

/** End Component Setup **/
%>

<!-- Begin Test -->
    <% c.Plugins.I18n.setI18n("fr"); %>
    <%= c.Plugins.I18n.translate("Cancel") %>
<!-- End Test -->