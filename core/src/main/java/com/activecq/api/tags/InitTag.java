package com.activecq.api.tags;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.felix.scr.annotations.Component;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.scripting.jsp.util.TagUtil;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitTag extends TagSupport {
	
	private static Logger logger = LoggerFactory.getLogger(InitTag.class);

	private String var = null;
	private String model = null;
	private PageContext pageContext;
	private ComponentContext componentContext;
	
	
	
	public int doEndTag() throws JspException
	{
		
		logger.info("ActiveCQ InitTag - doEnd() - start"); 
		if(this.model!=null && this.var!=null)
		{
			Class clz;
			try {
				clz = Class.forName(this.model);
				Constructor constructor = clz.getConstructor(new Class[]{SlingHttpServletRequest.class});
				SlingHttpServletRequest request = TagUtil.getRequest(this.pageContext);
				Object object = constructor.newInstance(request);
				pageContext.setAttribute(this.var, clz.cast(object));
				logger.info("ActiveCQ InitTag - object added to pageContext var:{} model:{}",this.var,clz.cast(object));
				
			} catch (ClassNotFoundException e) {
				logger.error("Error occurred", e);
			} catch (SecurityException e) {
				logger.error("Error occurred", e);
			} catch (NoSuchMethodException e) {
				logger.error("Error occurred", e);
			} catch (IllegalArgumentException e) {
				logger.error("Error occurred", e);
			} catch (InstantiationException e) {
				logger.error("Error occurred", e);
			} catch (IllegalAccessException e) {
				logger.error("Error occurred", e);
			} catch (InvocationTargetException e) {
				logger.error("Error occurred", e);
			}
			
		}
		else
		{
			throw new JspException("Var or Model not specified");
		}
		return super.doEndTag();
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
	
	public void setPageContext(PageContext p) {
		pageContext = p;
	}

}
