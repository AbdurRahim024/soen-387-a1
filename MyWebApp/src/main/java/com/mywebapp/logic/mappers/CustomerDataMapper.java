package com.mywebapp.logic.mappers;

import com.mywebapp.logic.DataMapperException;
import com.mywebapp.logic.models.Customer;
import com.mywebapp.logic.models.Order;

import java.util.ArrayList;
import java.util.UUID;

public class CustomerDataMapper {

    
    public static Customer findByGuid(UUID uniqueID) throws DataMapperException {
        return null;
    }

    
    public static void insert(Object object) throws DataMapperException {

    }

    
    public static void update(Object object) throws DataMapperException {

    }

    
    public static void delete(Object object) throws DataMapperException {

    }

    public static Customer findByName(String name) {
        //TODO: throw UserNotFoundException here
        return null;
    }


}
