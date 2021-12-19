package io.service;

import io.model.*;
import io.repo.*;
import qio.annotate.Inject;
import qio.annotate.Service;
import qio.model.web.ResponseData;

import java.util.List;

@Service
public class GroupService {

    @Inject
    GroupRepo groupRepo;

    @Inject
    AssetRepo assetRepo;

    @Inject
    UserRepo userRepo;

    @Inject
    CategoryRepo categoryRepo;

    @Inject
    DesignRepo designRepo;

    @Inject
    BusinessRepo businessRepo;

    @Inject
    SiteService siteService;

    @Inject
    BusinessService businessService;

    @Inject
    SeaService seaService;

    @Inject
    AuthService authService;


    public String create(Long businessId, ResponseData data){
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        businessService.setData(businessId, data);

        List<Category> categories = categoryRepo.getListAll(businessId);
        if(categories.size() == 0){
            data.set("message", "You have to walk before you can run... you need to create a category before you can continue.");
            return "[redirect]/categories/new/" + businessId;
        }
        data.set("categories", categories);

        List<Design> designs = designRepo.getList(businessId);
        data.set("designs", designs);

        List<Asset> assets = assetRepo.getList(businessId);
        data.set("assets", assets);

        data.set("page", "/pages/group/create.jsp");
        return "/designs/auth.jsp";
    }

}
