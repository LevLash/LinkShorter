package com.LinkShorter.Controllers;

import com.LinkShorter.Entities.GenerateRequest;
import com.LinkShorter.Entities.Link;
import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.CosmosAsyncDatabase;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.models.CosmosDatabaseResponse;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.PartitionKey;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AzureCosmosDBController {
    private static final String ENDPOINT = "https://linksshorter.documents.azure.com:443/";
    private static final String PKEY =
            "lYuTYaT2Wc4sAffNvdyUtHoNDF8sKUHA3M9EIZPtjYq9FBGJK0ZmGRJIxZtg24xgtoYkafoRHQbs79I4u49Csg==";
    private static final String CONTAINERNAME = "linksContainer";
    private static final String DBNAME = "linksDB";
    static CosmosAsyncClient cosmosAsyncClient;
    static CosmosAsyncDatabase database;
    static CosmosAsyncContainer container;
    
    private void initialisationAsync() {
        
        cosmosAsyncClient = new CosmosClientBuilder()
                .endpoint(ENDPOINT)
                .key(PKEY)
                .buildAsyncClient();
        Mono<CosmosDatabaseResponse> databaseIfNotExists = cosmosAsyncClient.createDatabaseIfNotExists(DBNAME);
        databaseIfNotExists.flatMap(databaseResponse -> {
            database = cosmosAsyncClient.getDatabase(databaseResponse.getProperties().getId());
            return Mono.empty();
        }).block();
        
        cosmosAsyncClient.createDatabaseIfNotExists(DBNAME)
                .flatMap(databaseResponse -> {
                    String databaseId = databaseResponse.getProperties().getId();
                    return cosmosAsyncClient.getDatabase(databaseId)
                            .createContainerIfNotExists(CONTAINERNAME, "/id")
                            .map(containerResponse -> cosmosAsyncClient.getDatabase(databaseId)
                                    .getContainer(containerResponse.getProperties().getId()));
                })
                .subscribe();
        
        
        container = database.getContainer(CONTAINERNAME);
        
    }
    
    public void addLink(Link link) {
        try{
            container.createItem(link).block();
        }catch(Exception ignored){}
    }
    
    public String getLink(String id) {
        CosmosItemResponse<Link> linkCosmosResponse = container.readItem(id,
                new PartitionKey(id), Link.class).block();
        
        return linkCosmosResponse != null ? linkCosmosResponse.getItem().getLink() : null;
        
    }
    
    public AzureCosmosDBController() {
        initialisationAsync();
    }
}
