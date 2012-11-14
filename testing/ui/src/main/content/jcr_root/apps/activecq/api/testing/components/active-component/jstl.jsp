<%@page session="false"
        import="com.activecq.api.testing.ActiveComponentTestStub"
%><%@include file="/apps/activecq/api/testing/global/global.jsp" %>

<c:set var="c" value="<%= new ActiveComponentTestStub(slingRequest) %>"/>

<!-- Begin Test -->
    ${c.component.name}
<!-- End Test -->

