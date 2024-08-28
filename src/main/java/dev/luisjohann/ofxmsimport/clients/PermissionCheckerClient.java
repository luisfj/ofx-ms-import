package dev.luisjohann.ofxmsimport.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "permission-checker")
public interface PermissionCheckerClient {

    @RequestMapping(method = RequestMethod.GET, value = "/check-import-ofx/{ue_id}")
    void checkImportOfx(@PathVariable("ue_id") Long ueId);
}
