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

    @Get("/{{businessId}}/groups/create.qzo")
    public String create(ResponseData data,
                            @Variable Long businessId){
        return groupService.create(businessId, data);
    }

    @Post("/{{businessId}}/groups/save")
    public String save(HttpServletRequest req,
                       ResponseData data,
                       @Variable Long businessId){
        return groupService.save(businessId, data, req);
    }

    @Get("/{{businessId}}/groups/edit/{{id}}")
    public String edit(HttpServletRequest req,
                       ResponseData data,
                       @Variable Long businessId,
                       @Variable Long id){
        return groupService.edit(id, businessId, data, req);
    }


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

    @Post("/{{businessId}}/groups/options/delete/{{id}}")
    public String deleteOption(HttpServletRequest req,
                             ResponseData data,
                             @Variable Long businessId,
                             @Variable Long id){
        return groupService.deleteOption(id, businessId, data, req);
    }
}
