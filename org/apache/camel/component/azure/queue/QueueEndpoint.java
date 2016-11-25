package org.apache.camel.component.azure.queue;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.api.management.ManagedResource;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.impl.SynchronousDelegateProducer;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriPath;


/**
 * @author PabloBravo
 * 
 * Camel endpoint for azure queues. 
 * 
 *
 */
@ManagedResource(description = "Managed Queue Endpoint")
@UriEndpoint(scheme = QueueComponent.COMPONENT_SCHEME, title = "Queue Endpoint", syntax = QueueComponent.COMPONENT_SCHEME + ":queueCode", producerOnly = true)
public class QueueEndpoint extends DefaultEndpoint {
        
    @UriPath
    @Metadata(required="true")
    private String queueCode;

    @Override
    public Consumer createConsumer(final Processor arg0) throws Exception {
        // YTODO Auto-generated method stub
        return null;
    }

    @Override
    public Producer createProducer() throws Exception {
        // YTODO Auto-generated method stub
        Producer answer = new QueueEndPointProducer(this);
        if (isSynchronous()) {
          return new SynchronousDelegateProducer(answer);
        }
        return answer;
    }

    @Override
    public boolean isSingleton() {
        // YTODO Auto-generated method stub
        return false;
    }

    public String getQueueCode()
    {
        return queueCode;
    }

    public void setQueueCode(final String queueCode)
    {
        this.queueCode = queueCode;
    }
    
    @Override
    protected String createEndpointUri() {
        return getQueueCode();
    }
    

}
