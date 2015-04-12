/*
 * Copyright (C) 2010 Teleal GmbH, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.gcc.imusic.callback;

//import org.teleal.cling.model.action.ActionInvocation;
//import org.teleal.cling.model.message.UpnpResponse;
//import org.teleal.cling.model.meta.Service;
//import org.teleal.cling.support.model.BrowseFlag;
//import org.teleal.cling.support.model.DIDLContent;
//import org.teleal.cling.support.model.SortCriterion;
//import org.teleal.cling.support.contentdirectory.callback.Browse;

import android.app.Activity;

import java.util.logging.Logger;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.contentdirectory.callback.Browse;
import org.fourthline.cling.support.model.BrowseFlag;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.SortCriterion;

/**
 * Updates a tree model after querying a backend <em>ContentDirectory</em>
 * service.
 * 
 * @author Christian Bauer
 */
public class ContentBrowseActionCallback extends Browse {

	private static Logger log = Logger
			.getLogger(ContentBrowseActionCallback.class.getName());

	@SuppressWarnings("rawtypes")
	private Service service;
	private ContentBrowseActionListener listener;
	
	@SuppressWarnings("rawtypes")
	public ContentBrowseActionCallback(Service service,
			String containerId, ContentBrowseActionListener listener) {
		super(service, containerId, BrowseFlag.DIRECT_CHILDREN, "*", 0,
				null, new SortCriterion(true, "dc:title"));
		this.listener = listener;
		this.service = service;
	}

	@SuppressWarnings("rawtypes")
	public void received(final ActionInvocation actionInvocation,
			final DIDLContent didl) {
		log.fine("Received browse action DIDL descriptor, creating tree nodes");
		if (listener != null) {
			listener.received(service, actionInvocation, didl);
		}
	}

	public void updateStatus(final Status status) {
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void failure(ActionInvocation invocation, UpnpResponse operation,
			final String defaultMsg) {
		if (listener != null) {
			listener.failure(invocation, operation, defaultMsg);
		}
	}
	
	public interface ContentBrowseActionListener{
		public void received(final Service service, final ActionInvocation actionInvocation,
				final DIDLContent didl);
		public void failure(ActionInvocation invocation, UpnpResponse operation,
				final String defaultMsg);
	}
}
