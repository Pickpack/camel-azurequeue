package org.apache.camel.component.azure.queue;


import java.util.concurrent.RejectedExecutionException;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultAsyncProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.queue.CloudQueue;
import com.microsoft.azure.storage.queue.CloudQueueClient;
import com.microsoft.azure.storage.queue.CloudQueueMessage;


/**
 * @author PabloBravo
 *
 * Run Queue endpoint
 * 
 * Set the account name and account key from azure as headers to the endpoint.
 * 
 */
public class QueueEndPointProducer extends DefaultAsyncProducer {
    
    private static final Logger LOG = LoggerFactory.getLogger(QueueEndpoint.class);

    private String queueCode;
    
    QueueEndpoint endpoint;

    public QueueEndPointProducer(final QueueEndpoint endpoint)
    {
        super(endpoint);
        
        this.endpoint = endpoint;
        this.queueCode = endpoint.getQueueCode();
    }


    @Override
    public boolean process(Exchange exchange, AsyncCallback callback) {
      
          if (!(isRunAllowed())) {
            if (exchange.getException() == null) {
              exchange.setException(new RejectedExecutionException());
            }

            callback.done(true);
            return true;
          }
          
          return exportToQueue(exchange, callback);        
    }
    
    /*
     * Sends the message to the queue in azure
     */
    public boolean exportToQueue(Exchange exchange, AsyncCallback callback){
        
       String messageToExport = exchange.getIn().getBody(String.class);
       
       String azureConnectionString = "";
       
       try {
            azureConnectionString = getAzureConnectionString(exchange);
       }
       catch (Exception e1) {
           exchange.setException(e1);
           callback.done(true); 
       }
       
       try
       {
           // Retrieve storage account from connection-string.
           CloudStorageAccount storageAccount =
              CloudStorageAccount.parse(azureConnectionString);
           
          // Create the queue client.
          CloudQueueClient queueClient = storageAccount.createCloudQueueClient();

          // Retrieve a reference to a queue.
          CloudQueue queue = queueClient.getQueueReference(queueCode);
          queue.createIfNotExists();
          
          // Create a message and add it to the queue.
          CloudQueueMessage message = new CloudQueueMessage(messageToExport);
          queue.addMessage(message);
          
       }
       catch (Exception e)
       {
           exchange.setException(e);
           callback.done(true); 
       }
       
        return true;
        
    }
    
    public String getAzureConnectionString(Exchange exchange) throws Exception{
        
        String accountName = (String)exchange.getIn().getHeader("CamelAzureAccountName", String.class);
        
        if(accountName == null){
            throw new Exception("The accountName is not set as header in the exchange (CamelAzureAccountName)");
        }
        
        String accountKey = (String)exchange.getIn().getHeader("CamelAzureAccountKey", String.class);
        
        if(accountKey == null){
            throw new Exception("The accountKey is not set as header in the exchange (CamelAzureAccountKey)");
        }
         
        return "DefaultEndpointsProtocol=http;AccountName=" + accountName + ";" + "AccountKey=" + accountKey + ";";
     }

    public String getQueueCode() {
        return queueCode;
    }

    public void setQueueCode(String queueCode) {
        this.queueCode = queueCode;
    }
    

}
