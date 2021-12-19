package io.service;

import io.Kilo;
import io.model.*;
import io.repo.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import qio.Qio;
import qio.annotate.Inject;
import qio.annotate.Service;
import qio.model.web.ResponseData;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    @Inject
    Qio qio;

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
        String permission = Kilo.BUSINESS_MAINTENANCE + businessId;
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            data.set("message", "Whoa! Not authorized to view this business.");
            return "[redirect]/";
        }

        businessService.setData(businessId, data);

        List<GroupOption> groupOptions = groupRepo.getListOptions(businessId);
        data.set("groupOptions", groupOptions);

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

    public String save(Long businessId, ResponseData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        String permission = Kilo.BUSINESS_MAINTENANCE + businessId;
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            data.set("message", "Whoa! Not authorized to view this business.");
            return "[redirect]/";
        }

        Group group = (Group) qio.set(req, Group.class);

        List<Part> fileParts = null;
        try {
            fileParts = req.getParts()
                    .stream()
                    .filter(part -> "media".equals(part.getName()) && part.getSize() > 0)
                    .collect(Collectors.toList());

            for (Part part : fileParts) {
                String original = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                InputStream is = part.getInputStream();
                String ext = Kilo.getExt(original);
                String name = Kilo.getString(9) + "." + ext;
                seaService.send(name, is);
                group.setImageUri(Kilo.OCEAN_ENDPOINT + name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }

        groupRepo.save(group);
        Group savedGroup = groupRepo.getSaved();

        String[] categories = req.getParameterValues("categories");
        for(String id : categories){
            GroupCategory groupCategory = new GroupCategory(savedGroup.getId(), Long.valueOf(id.trim()), businessId);
            groupRepo.saveCategory(groupCategory);
        }

        data.set("message", "Successfully started Item Group!");
        return "[redirect]/" + businessId + "/groups/edit/" + savedGroup.getId();
    }

    public String edit(Long id, Long businessId, ResponseData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        String permission = Kilo.BUSINESS_MAINTENANCE + businessId;
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            data.set("message", "Whoa! Not authorized to view this business.");
            return "[redirect]/";
        }

        List<GroupOption> groupOptions = groupRepo.getListOptions(businessId);
        data.set("groupOptions", groupOptions);

        Group group = groupRepo.get(id);
        data.set("group", group);

        if(req.getParameter("optionsCount") != null)data.set("optionsCount", req.getParameter("optionsCount"));
        if(req.getParameter("pricesCount") != null)data.set("pricesCount", req.getParameter("pricesCount"));

        data.set("page", "/pages/group/edit.jsp");
        return "/designs/auth.jsp";
    }

    public String createOptions(Long businessId, ResponseData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        String permission = Kilo.BUSINESS_MAINTENANCE + businessId;
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            data.set("message", "Whoa! Not authorized to view this business.");
            return "[redirect]/";
        }

        Business business = businessRepo.get(businessId);
        businessService.setData(businessId, data);

        List<GroupOption> groupOptions = groupRepo.getListOptions(businessId);
        data.set("groupOptions", groupOptions);

        data.set("page", "/pages/group/create_options.jsp");
        return "/designs/auth.jsp";
    }

    public String saveOption(Long businessId, ResponseData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        String permission = Kilo.BUSINESS_MAINTENANCE + businessId;
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            data.set("message", "Whoa! Not authorized to view this business.");
            return "[redirect]/";
        }

        Business business = businessRepo.get(businessId);

        GroupOption groupOption = (GroupOption) qio.set(req, GroupOption.class);
        groupRepo.saveOption(groupOption);

        data.set("message", "Successfully saved group option.");

        return "[redirect]/" + businessId + "/groups/options/create";

    }

    public String deleteOption(Long id, Long businessId, ResponseData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        String permission = Kilo.BUSINESS_MAINTENANCE + businessId;
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            data.set("message", "Whoa! Not authorized to view this business.");
            return "[redirect]/";
        }

        Business business = businessRepo.get(businessId);
        groupRepo.deleteOption(id);
        return "[redirect]/" + businessId + "/groups/options/create";
    }

}
