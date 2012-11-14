<%@ include file="/apps/activecq/api/testing/global/global.jsp" %><%
%><%@ page session="false" contentType="text/html; charset=utf-8" %><%
    final String className = component.getProperties().get("className", "");
%>
<!--[if IE 7 ]> <body class="ie7 <%= xssAPI.encodeForHTMLAttr(className) %>"> <![endif]-->
<!--[if IE 8 ]> <body class="ie8 <%= xssAPI.encodeForHTMLAttr(className) %>"> <![endif]-->
<!--[if IE 9 ]> <body class="ie9 <%= xssAPI.encodeForHTMLAttr(className) %>"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <body class="<%= xssAPI.encodeForHTMLAttr(className) %>"> <!--<![endif]-->
    <cq:include path="clientcontext" resourceType="cq/personalization/components/clientcontext"/>
    <div id="main" class="test-page-main">
        <cq:include path="par" resourceType="foundation/components/parsys"/>
    </div>
    <cq:includeClientLib js="apps.testing.all"/>
    <cq:include path="timing" resourceType="foundation/components/timing"/>
    <cq:include path="cloudservices" resourceType="cq/cloudserviceconfigs/components/servicecomponents"/>
</body>