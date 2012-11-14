<%@page session="false"
        import="com.activecq.api.testing.ActiveComponentTestStub,
                org.apache.commons.lang.ArrayUtils"
%><%@include file="/apps/activecq/api/testing/global/global.jsp" %><%
/** Begin Component Setup **/
ActiveComponentTestStub c = new ActiveComponentTestStub(slingRequest);
Resource propResource = resourceResolver.resolve(c.getResource().getPath() + "/../../extra");
final String DEFAULT_STR_VALUE = "This is the extra default value";
/** End Component Setup **/
%>

<!-- Begin Test -->
<div class="plain-text">
  <%= c.getProperty(propResource, "plain-text", String.class) %>
</div>
<!-- End Test -->

<!-- Begin Test -->
<div class="rich-text">
  <%= c.getProperty(propResource, "rich-text", String.class) %>
</div>
<!-- End Test -->

<!-- Begin Test -->
<div class="double">
    <%= c.getProperty(propResource, "double", Double.class) %>
</div>
<!-- End Test -->

<!-- Begin Test -->
<div class="long">
  <%= c.getProperty(propResource, "long", Long.class) %>
</div>
<!-- End Test -->

<!-- Begin Test -->
<div class="boolean">
  <%= c.getProperty(propResource, "boolean", Boolean.class) %>
</div>
<!-- End Test -->

<!-- Begin Test -->
<div class="default-value">
  <%= c.getProperty(propResource, "default-value", DEFAULT_STR_VALUE) %>
</div>
<!-- End Test -->

<!-- Begin Test -->
<div class="str-array">
  <%= ArrayUtils.toString(c.getProperty(propResource, "str-array", new String[] {})) %>
</div>
<!-- End Test -->