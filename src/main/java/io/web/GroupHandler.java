package io.web;

import io.service.GroupService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import qio.annotate.HttpHandler;
import qio.annotate.Inject;
import qio.annotate.Variable;
import qio.annotate.verbs.Get;
import qio.annotate.verbs.Post;
import qio.model.web.ResponseData;

import java.io.IOException;

@HttpHandler
public class GroupHandler {

    @Inject
    GroupService groupService;

    @Get("/{{businessId}}/groups/options/create")
    public String createOptions(ResponseData data,
                            @Variable Long businessId){
        return groupService.createOptions(businessId, data);
    }

    @Post("/{{businessId}}/groups/options/save")
    public String saveOption(HttpServletRequest req,
                             ResponseData data,
                             @Variable Long businessId){
        return groupService.saveOption(businessId, data, req);
    }

}
