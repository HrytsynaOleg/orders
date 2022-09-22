package com.atlantis.orders.repository.impl;

import com.atlantis.orders.dbtables.BrandSuffix;
import com.atlantis.orders.repository.IBrandSuffixDynamoDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import java.util.HashMap;
import java.util.Map;

@Repository
public class BrandSuffixDynamoDbRepository implements IBrandSuffixDynamoDbRepository {
    private final DynamoDbTable<BrandSuffix> brandSuffixDynamoDbTable;
    private Map<String,String> brandSuffixList;

    @Autowired
    public BrandSuffixDynamoDbRepository(DynamoDbEnhancedClient dynamoDbenhancedClient) {
        this.brandSuffixDynamoDbTable = dynamoDbenhancedClient.table("ProductSuffix", TableSchema.fromBean(BrandSuffix.class));
        this.brandSuffixList = new HashMap<>();
    }

    @Override
    public String getSuffix(String brand) {
        if (brandSuffixList.isEmpty()) {
            PageIterable<BrandSuffix> scan = brandSuffixDynamoDbTable.scan();
            for (Page<BrandSuffix> brandSuffixPage : scan) {
                for (BrandSuffix item : brandSuffixPage.items()) {
                    brandSuffixList.put(item.getName(),item.getSuffix());
                }
            }
        }
        return brandSuffixList.getOrDefault(brand,"");

    }






}
