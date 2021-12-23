package io.web;

import io.service.AssetService;
import io.service.DataService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import qio.annotate.*;
import qio.annotate.verbs.Get;
import qio.annotate.verbs.Post;
import qio.model.web.ResponseData;

@HttpHandler
public class DataHandler {

    @Inject
    DataService dataService;

    @Get("/import/media/{{businessId}}")
    public String viewImport(ResponseData data,
                             @Variable Long businessId) {
        return dataService.viewImportMedia(businessId, data);
    }

    @Post("/import/media/{{businessId}}")
    public String importMedia(HttpServletRequest req,
                              ResponseData data,
                              @Variable Long businessId) throws Exception {
        return dataService.importMedia(businessId, data, req);
    }

    @Get("/imports/media/{{businessId}}")
    public String viewImports(ResponseData data,
                             @Variable Long businessId) {
        return dataService.viewImportsMedia(businessId, data);
    }

    @Get("/imports/media/{{businessId}}/{{importId}}")
    public String viewImports(ResponseData data,
                              @Variable Long businessId,
                              @Variable Long importId) {
        return dataService.viewMedias(businessId, importId, data);
    }

    @Post("/import/media/update/{{businessId}}/{{importId}}")
    public String importMedia(HttpServletRequest req,
                              ResponseData data,
                              @Variable Long businessId,
                              @Variable Long importId) throws Exception {
        return dataService.updateMedia(businessId, importId, data, req);
    }


    @Post("/import/media/convert/{{businessId}}/{{importId}}")
    public String convertItems(HttpServletRequest req,
                               ResponseData data,
                               @Variable Long businessId,
                               @Variable Long importId){
        return dataService.convertItems(businessId, importId, data, req);
    }

    @Post("/import/media/delete/{{businessId}}/{{importId}}")
    public String deleteImport(HttpServletRequest req,
                              ResponseData data,
                              @Variable Long businessId,
                              @Variable Long importId){
        return dataService.deleteImport(businessId, importId, data, req);
    }

    @Get("/imports/item_groups/new/{{businessId}}")
    public String viewItemGroupImport(ResponseData data,
                             @Variable Long businessId) {
        return dataService.viewItemGroupImport(businessId, data);
    }

    @Get("/imports/item_groups/{{businessId}}")
    public String itemGroups(ResponseData data,
                              @Variable Long businessId) {
        return dataService.viewItemGroups(businessId, data);
    }

    @Post("/imports/item_groups/{{businessId}}")
    public String ingest(HttpServletRequest req,
                         ResponseData data,
                         @Variable Long businessId) {
        return dataService.ingest(businessId, data, req);
    }

    @Post("/imports/item_groups/delete/{{businessId}}/{{id}}")
    public String deleteGroupImport(HttpServletRequest req,
                         ResponseData data,
                         @Variable Long businessId,
                         @Variable Long ingestId) {
        return dataService.deleteGroupImport(ingestId, businessId, data, req);
    }

    @Post("/imports/item_groups/group/delete/{{businessId}}/{{id}}")
    public String deleteGroups(HttpServletRequest req,
                               ResponseData data,
                               @Variable Long businessId,
                               @Variable Long groupId) {
        return dataService.deleteImportedGroup(groupId, businessId, data, req);
    }
}
