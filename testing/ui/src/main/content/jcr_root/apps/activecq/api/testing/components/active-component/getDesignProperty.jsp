<%@page session="false"
        import="com.activecq.api.testing.ActiveComponentTestStub,
                org.apache.commons.lang.ArrayUtils"
%><%@include file="/apps/activecq/api/testing/global/global.jsp" %><%
/** Begin Component Setup **/
ActiveComponentTestStub c = new ActiveComponentTestStub(slingRequest);
final String DEFAULT_STR_VALUE = "This is the default design value";
/** End Component Setup **/
%>

<!-- Begin Test -->
<div class="plain-text">
     <%= c.getDesignProperty("plain-text", String.class) %>
</div>
<!-- End Test -->

<!-- Begin Test -->
<div class="rich-text">
     <%= c.getDesignProperty("rich-text", String.class) %>
</div>
<!-- End Test -->

<!-- Begin Test -->
<div class="double">
    <%= c.getDesignProperty("double", Double.class) %>
</div>
<!-- End Test -->

<!-- Begin Test -->
<div class="long">
     <%= c.getDesignProperty("long", Long.class) %>
</div>
<!-- End Test -->

<!-- Begin Test -->
<div class="boolean">
     <%= c.getDesignProperty("boolean", Boolean.class) %>
</div>
<!-- End Test -->

<!-- Begin Test -->
<div class="default-value">
     <%= c.getDesignProperty("default-value", DEFAULT_STR_VALUE) %>
</div>
<!-- End Test -->

<!-- Begin Test -->
<div class="str-array">
     <%= ArrayUtils.toString(c.getDesignProperty("str-array", new String[] {})) %>
</div>
<!-- End Test -->