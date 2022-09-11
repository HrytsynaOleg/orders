package com.atlantis.orders.onebox;

import com.atlantis.orders.constants.OneboxApiEndpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OneboxApiOrdersService {

    IOneboxApiSecurityService securityService;

    @Autowired
    public OneboxApiOrdersService(IOneboxApiSecurityService securityService) {
        this.securityService = securityService;
    }

    public void getSupplierOrderListByStatus(Integer statusId) {

        Map<String,Object> body = new HashMap<>();
        List<String> orderFields = Arrays.asList("id","cdate","name","client","workflow","status","orderproducts","customfields");
        Map<String,Integer> filters = new HashMap<>();
        filters.put("workflowid",16);
        filters.put("statusid",statusId);
        body.put("fields",orderFields);
        body.put("filter",filters);

        String response = OneboxApiRequest.performHttpRequest(OneboxApiEndpoints.ONEBOX_GET_ORDERS, securityService.getToken(), body);

        System.out.println();


    }

}
