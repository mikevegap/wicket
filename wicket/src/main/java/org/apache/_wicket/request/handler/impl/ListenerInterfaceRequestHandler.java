/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache._wicket.request.handler.impl;

import org.apache._wicket.IComponent;
import org.apache._wicket.IPage;
import org.apache._wicket.PageParameters;
import org.apache._wicket.RequestCycle;
import org.apache._wicket.request.handler.ComponentRequestHandler;
import org.apache._wicket.request.handler.PageRequestHandler;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RequestListenerInterface;
import org.apache.wicket.WicketRuntimeException;

/**
 * Request handler that invokes the listener interface on component and renders page afterwards.
 * 
 * @author Matej Knopp
 */
public class ListenerInterfaceRequestHandler implements PageRequestHandler, ComponentRequestHandler
{
	private final IComponent component;
	private final IPage page;
	private final RequestListenerInterface listenerInterface;

	/**
	 * Construct.
	 * 
	 * @param page
	 * @param component
	 * @param listenerInterface
	 */
	public ListenerInterfaceRequestHandler(IPage page, IComponent component,
		RequestListenerInterface listenerInterface)
	{
		if (component == null)
		{
			throw new IllegalArgumentException("Argument 'component' may not be null.");
		}
		if (page == null)
		{
			throw new IllegalArgumentException("Argument 'page' may not be null.");
		}
		if (listenerInterface == null)
		{
			throw new IllegalArgumentException("Argument 'listenerInterface' may not be null.");
		}
		this.component = component;
		this.page = page;
		this.listenerInterface = listenerInterface;
	}

	public IComponent getComponent()
	{
		return component;
	}

	public IPage getPage()
	{
		return page;
	}

	public Class<? extends IPage> getPageClass()
	{
		return page.getClass();
	}

	public String getPageMapName()
	{
		return page.getPageMapName();
	}

	public PageParameters getPageParameters()
	{
		return page.getPageParameters();
	}

	public void detach(RequestCycle requestCycle)
	{
		page.detach();
	}

	/**
	 * Returns the listener interface.
	 * 
	 * @return listener interface
	 */
	public RequestListenerInterface getListenerInterface()
	{
		return listenerInterface;
	}

	public void respond(RequestCycle requestCycle)
	{
		if (component.getPage() == page)
		{
			listenerInterface.invoke((Page)page, (Component)component);
			requestCycle.replaceCurrentRequestHandler(new RenderPageRequestHandler(page));
		}
		else
		{
			throw new WicketRuntimeException("Component " + component +
				" has been removed from page.");
		}
	}

}
