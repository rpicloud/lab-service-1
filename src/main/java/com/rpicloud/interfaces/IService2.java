package com.rpicloud.interfaces;

import com.rpicloud.models.Resource;
import feign.RequestLine;

import java.util.List;

/**
 * Created by mixmox on 18/04/16.
 */

public interface IService2 {
    @RequestLine("GET /resources")
    List<Resource> resources();
}
