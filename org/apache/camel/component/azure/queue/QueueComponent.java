package org.apache.camel.component.azure.queue;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;


/**
 * @author PabloBravo
 *
 * Queue functions for Camel
 */
public class QueueComponent extends DefaultComponent
{

	public static final String COMPONENT_SCHEME = "azure-queue";

	@Override
	protected Endpoint createEndpoint(final String uri, final String queueCode, final Map<String, Object> options) throws Exception
	{
       	final QueueEndpoint endpoint = new QueueEndpoint();
       	setProperties(endpoint, options);
       	endpoint.setQueueCode(queueCode);       	
      	return endpoint;
	}

}
