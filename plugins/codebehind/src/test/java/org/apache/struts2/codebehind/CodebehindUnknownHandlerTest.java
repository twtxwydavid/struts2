/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.struts2.codebehind;

import java.util.HashMap;

import javax.servlet.ServletContext;

import org.apache.struts2.StrutsTestCase;
import org.apache.struts2.config.NullResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;

import com.mockobjects.dynamic.Mock;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.config.entities.ResultTypeConfig;

public class CodebehindUnknownHandlerTest extends StrutsTestCase {

    CodebehindUnknownHandler handler;
    Mock mockServletContext;
    
    public void setUp() throws Exception {
        super.setUp();
        mockServletContext = new Mock(ServletContext.class);
        handler = new CodebehindUnknownHandler("codebehind-default", configuration);
        handler.setPathPrefix("/");
        handler.setObjectFactory(container.getInstance(ObjectFactory.class));
        handler.setServletContext((ServletContext)mockServletContext.proxy());
    }

    public void testBuildResult() {
        ActionContext ctx = new ActionContext(new HashMap());
        ResultTypeConfig config = new ResultTypeConfig("null", SomeResult.class.getName(), "location");
        
        Result result = handler.buildResult("/foo.jsp", "success", config, ctx);
        assertNotNull(result);
        assertTrue(result instanceof SomeResult);
        assertEquals("/foo.jsp", ((SomeResult) result).location);
        
    }

    public void testString() {
        assertEquals("foo.bar.jim", handler.string("foo", ".", "bar", ".", "jim"));
    }

    public void testDeterminePath() {
        assertEquals("/", handler.determinePath("/", ""));
        assertEquals("/", handler.determinePath("/", null));
        assertEquals("/", handler.determinePath("/", "/"));
        assertEquals("/foo/", handler.determinePath("/", "/foo"));
        assertEquals("/foo/", handler.determinePath("/", "/foo/"));
        assertEquals("/foo/", handler.determinePath("/", "foo"));
    }
    
    public static class SomeResult implements Result {

        public String location;
        public void setLocation(String loc) {
            this.location = loc;
        }
        
        public void execute(ActionInvocation invocation) throws Exception {
        }
        
    }

}
