<%@page session="false"
        import="com.activecq.api.testing.ActiveComponentTestStub,
                org.apache.commons.lang.ArrayUtils"
%><%@include file="/apps/activecq/api/testing/global/global.jsp" %><%
/** Begin Component Setup **/
ActiveComponentTestStub c = new ActiveComponentTestStub(slingRequest);
final String DEFAULT_STR_VALUE = "This is the content resource default value";
/** End Component Setup **/
%>

<!-- Begin Test -->
<div class="plain-text">
  <%= c.getProperty("plain-text", String.class) %>
</div>
<!-- End Test -->

<!-- Begin Test -->
<div class="rich-text">
  <%= c.getProperty("rich-text", String.class) %>
</div>
<!-- End Test -->

<!-- Begin Test -->
<div class="double">
  <%= c.getProperty("double", Double.class) %>
</div>
<!-- End Test -->

<!-- Begin Test -->
<div class="long">
  <%= c.getProperty("long", Long.class) %>
</div>
<!-- End Test -->

<!-- Begin Test -->
<div class="boolean">
  <%= c.getProperty("boolean", Boolean.class) %>
</div>
<!-- End Test -->

<!-- Begin Test -->
<div class="default-value">
  <%= c.getProperty("default-value", DEFAULT_STR_VALUE) %>
 </div>
<!-- End Test -->

<!-- Begin Test -->
<div class="str-array">
  <%= ArrayUtils.toString(c.getProperty("str-array", new String[] {})) %>
</div>
<!-- End Test -->