package com.atlantis.orders.onebox;

import com.atlantis.orders.constants.OneboxApiEndpoints;
import com.atlantis.orders.onebox.model.OneboxOrder;
import com.atlantis.orders.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;

@Service
public class OneboxApiOrdersService {

    IOneboxApiSecurityService securityService;

    @Autowired
    public OneboxApiOrdersService(IOneboxApiSecurityService securityService) {
        this.securityService = securityService;
    }

    public List<OneboxOrder> getSupplierOrderListByStatus(Integer statusId) {

        Map<String,Object> body = new HashMap<>();
        List<String> orderFields = Arrays.asList("id","cdate","name","client","workflow","status","orderproducts","customfields");
        List<String> productFields = Arrays.asList("id","name","articul","brand");

//        List<String> orderFields = Arrays.asList("id","cdate","name");
        Map<String,Integer> filters = new HashMap<>();
        filters.put("workflowid",16);
        filters.put("statusid",statusId);
        body.put("fields",orderFields);
        body.put("productfields",productFields);
        body.put("filter",filters);
        String response = OneboxApiRequest.performHttpRequest(OneboxApiEndpoints.ONEBOX_GET_ORDERS, securityService.getToken(), body);

        List<OneboxOrder> oneboxOrders = JsonUtils.parseJson(response, new TypeReference<>() {});

        return oneboxOrders;


    }

}
