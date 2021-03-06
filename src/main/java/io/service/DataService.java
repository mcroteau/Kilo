package io.service;

import com.google.gson.Gson;
import io.Kilo;
import io.ingest.ItemGroupIngest;
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
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataService {

    public DataService(){
        this.ingests = new HashMap<>();
    }

    Map<String, ItemGroupIngest> ingests;

    Gson gson = new Gson();

    @Inject
    Qio qio;

    @Inject
    ItemRepo itemRepo;

    @Inject
    DesignRepo designRepo;

    @Inject
    UserRepo userRepo;

    @Inject
    CategoryRepo categoryRepo;

    @Inject
    DataRepo dataRepo;

    @Inject
    ModelRepo modelRepo;

    @Inject
    GroupRepo groupRepo;

    @Inject
    OptionRepo optionRepo;

    @Inject
    IngestRepo ingestRepo;

    @Inject
    PriceRepo priceRepo;

    @Inject
    BusinessRepo businessRepo;

    @Inject
    AuthService authService;

    @Inject
    SeaService seaService;

    @Inject
    BusinessService businessService;


    public String viewImportMedia(Long businessId, ResponseData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        String permission = Kilo.BUSINESS_MAINTENANCE + businessId;
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            data.set("message", "You don't have access to import for this business.");
            return "[redirect]/";
        }

        businessService.setData(businessId, data);
        data.set("page", "/pages/data/media_import.jsp");
        return "/designs/auth.jsp";
    }

    public String importMedia(Long businessId, ResponseData data, HttpServletRequest req) throws Exception{
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        String businessPermission = Kilo.BUSINESS_MAINTENANCE + businessId;
        if(!authService.isAdministrator() &&
                !authService.hasPermission(businessPermission)){
            data.set("message", "You don't have access to import for this business.");
            return "[redirect]/";
        }

        businessService.setData(businessId, data);

        List<Part> fileParts = req.getParts()
                .stream()
                .filter(part -> "media".equals(part.getName()) && part.getSize() > 0)
                .collect(Collectors.toList());

        User authUser = authService.getUser();

        DataImport dataImport = new DataImport();
        dataImport.setBusinessId(businessId);
        dataImport.setUserId(authUser.getId());
        dataImport.setDateImport(Kilo.getDate());
        dataImport.setType("media");
        dataRepo.save(dataImport);

        DataImport savedImport = dataRepo.getSaved();
        String permission = Kilo.DATA_IMPORT_MAINTENANCE + savedImport.getId();
        userRepo.savePermission(authUser.getId(), permission);

        String activeMedia = "";
        String issue = "";

        try {
            MediaImport mediaImport = new MediaImport();
            mediaImport.setImportId(savedImport.getId());

            for (Part part : fileParts) {
                String original = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                activeMedia = original;

                List<String> mediaBits = Arrays.asList(original.split("\\."));

                if (mediaBits.size() > 1) {
                    issue = "Unable to parse name from " + original + ". first bit from name";
                    String itemName = mediaBits.get(0).replace("-", " ");
                    mediaImport.setName(itemName);
                }

                if (mediaBits.size() > 2) {
                    String itemPrice = mediaBits.get(1).replace("-", ".");
                    issue = "Unable to parse price, second bit after first dot. " + itemPrice;
                    mediaImport.setPrice(new BigDecimal(itemPrice));
                }

                if (mediaBits.size() > 3) {
                    String itemQuantity = mediaBits.get(2);
                    issue = "Unable to parse quantity, third bit after second dot. " + itemQuantity;
                    mediaImport.setQuantity(new BigDecimal(itemQuantity));
                }

                if (mediaBits.size() > 4) {
                    String itemWeight = mediaBits.get(3);
                    issue = "Unable to parse weight, fourth bit after third dot. " + itemWeight;
                    mediaImport.setWeight(new BigDecimal(itemWeight));
                }

                InputStream is = part.getInputStream();
                String ext = Kilo.getExt(original);
                String name = Kilo.getString(9) + "." + ext;
                seaService.send(name, is);

                mediaImport.setMeta(name);
                mediaImport.setUri(Kilo.OCEAN_ENDPOINT + name);
                dataRepo.saveMedia(mediaImport);

                MediaImport savedMediaImport = dataRepo.getSavedMedia();
                String mediaImportPermission = Kilo.MEDIA_IMPORT_MAINTENANCE + savedMediaImport.getId();
                userRepo.savePermission(authUser.getId(), mediaImportPermission);
            }

        }catch(Exception ex){
            ex.printStackTrace();
            data.set("message", "Sorry, had issue with " + activeMedia + " please try again. " + issue);
            return "[redirect]/imports/media/"+ businessId;
        }

        data.set("message", "All set! Everything imported. Check out the grid by clicking Power Grid.");
        return "[redirect]/imports/media/" + businessId;
    }

    public String viewImportsMedia(Long businessId, ResponseData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        String permission = Kilo.BUSINESS_MAINTENANCE + businessId;
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            data.set("message", "You don't have access to import for this business.");
            return "[redirect]/";
        }

        businessService.setData(businessId, data);

        List<DataImport> dataImports = dataRepo.getList(businessId, "media");
        data.set("dataImports", dataImports);
        data.set("page", "/pages/data/data_imports_list.jsp");
        return "/designs/auth.jsp";
    }

    public String viewMedias(Long businessId, Long importId, ResponseData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        String permission = Kilo.BUSINESS_MAINTENANCE + businessId;
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            data.set("message", "You don't have access to import for this business.");
            return "[redirect]/";
        }
        businessService.setData(businessId, data);

        DataImport dataImport = dataRepo.get(importId);
        data.set("dataImport", dataImport);

        List<MediaImport> mediaImports = dataRepo.getListMedia(importId);
        data.set("mediaImports", mediaImports);

        List<Category> categories = categoryRepo.getListAll(businessId);
        data.set("categories", categories);

        data.set("page", "/pages/data/media_imports_list.jsp");
        return "/designs/auth.jsp";
    }

    public String updateMedia(Long businessId, Long importId, ResponseData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        String businessPermission = Kilo.BUSINESS_MAINTENANCE + businessId;
        if(!authService.isAdministrator() &&
                !authService.hasPermission(businessPermission)){
            data.set("message", "You don't have access to import for this business.");
            return "[redirect]/";
        }

        MediaImport mediaImport = (MediaImport) Qio.get(req, MediaImport.class);
        String permission = Kilo.MEDIA_IMPORT_MAINTENANCE + mediaImport.getId();
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            data.set("message", "Unauthorized to edit this category.");
            return "[redirect]/";
        }

        dataRepo.updateMedia(mediaImport);
        data.set("message", "Successfully updated " + mediaImport.getName() + "!");
        return "[redirect]/imports/media/" + businessId + "/" + importId;
    }

    public String deleteImport(Long businessId, Long importId, ResponseData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        String permission = Kilo.DATA_IMPORT_MAINTENANCE + importId;
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            data.set("message", "You don't have access to delete this import.");
            return "[redirect]/";
        }

        itemRepo.deleteItemImports(importId);
        dataRepo.deleteMediaImports(importId);
        dataRepo.delete(importId);

        data.set("message", "Successfully deleted the import and removed all items");
        return "[redirect]/imports/media/" + businessId;
    }

    public String convertItems(Long businessId, Long importId, ResponseData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        String permission = Kilo.DATA_IMPORT_MAINTENANCE + importId;
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            data.set("message", "You don't have access to convert this import.");
            return "[redirect]/";
        }

        Design design = designRepo.getBase(businessId);
        DataImport dataImport = dataRepo.get(importId);

        List<MediaImport> mediaImports = dataRepo.getListMedia(importId);
        for(MediaImport mediaImport : mediaImports){
            Item item = new Item();
            item.setName(mediaImport.getName());
            item.setPrice(mediaImport.getPrice());
            item.setQuantity(mediaImport.getQuantity());
            item.setWeight(mediaImport.getWeight());
            item.setImageUri(mediaImport.getUri());
            item.setBusinessId(dataImport.getBusinessId());
            item.setDesignId(design.getId());
            itemRepo.save(item);

            Item savedItem = itemRepo.getSaved();
            User authUser = authService.getUser();

            String itemPermission = Kilo.ITEM_MAINTENANCE + savedItem.getId();
            userRepo.savePermission(authUser.getId(), itemPermission);

            Category category = categoryRepo.get(mediaImport.getCategoryId());
            if(category == null) category = categoryRepo.getOne(businessId);

            ItemCategory itemCategory = new ItemCategory();
            itemCategory.setItemId(savedItem.getId());
            itemCategory.setCategoryId(category.getId());

            categoryRepo.saveItem(itemCategory);

            if(category.getCategoryId() != null) {
                saveGuidanceCategory(savedItem, category);
            }
        }

        data.set("message", "Successfully converted all media imports into items!");
        return "[redirect]/imports/media/" + businessId + "/" + importId;
    }

    private void saveGuidanceCategory(Item savedItem, Category category){
        Category guidanceCategory = categoryRepo.get(category.getCategoryId());

        if(guidanceCategory != null) {
            ItemCategory itemCategory = new ItemCategory();
            itemCategory.setItemId(savedItem.getId());
            itemCategory.setCategoryId(guidanceCategory.getId());

            if (category.getCategoryId() != null) {
                saveGuidanceCategory(savedItem, guidanceCategory);
            }
        }
    }

    public String viewItemGroupImport(Long businessId, ResponseData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        String permission = Kilo.BUSINESS_MAINTENANCE + businessId;
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            data.set("message", "You don't have access to import for this business.");
            return "[redirect]/";
        }
        businessService.setData(businessId, data);
        data.set("page", "/pages/data/item_group_import.jsp");
        return "/designs/auth.jsp";
    }

    public String viewItemGroups(Long businessId, ResponseData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        String permission = Kilo.BUSINESS_MAINTENANCE + businessId;
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            data.set("message", "You don't have access to import for this business.");
            return "[redirect]/";
        }

        businessService.setData(businessId, data);
        List<Ingest> ingests = ingestRepo.getList(businessId);
        for(Ingest ingest: ingests){
            List<ItemGroup> itemGroups = groupRepo.getList(ingest.getId());
            for(ItemGroup itemGroup: itemGroups){
                List<GroupOption> groupOptions = optionRepo.getListOptions(itemGroup.getId());
                itemGroup.setGroupOptions(groupOptions);

                List<GroupModel> groupModels = modelRepo.getList(itemGroup.getId());
                for(GroupModel groupModel : groupModels){
                    List<GroupOptionValue> groupValues = optionRepo.getListValues(groupModel.getId());
                    List<PricingValue> pricingValues = priceRepo.getListValues(groupModel.getId());
                    groupModel.setGroupValues(groupValues);
                    groupModel.setPricingValues(pricingValues);
                }
                itemGroup.setGroupModels(groupModels);

                List<PricingOption> pricingOptions = priceRepo.getListOptions(itemGroup.getId());
                itemGroup.setPricingOptions(pricingOptions);
            }
            ingest.setItemGroups(itemGroups);
        }

        data.set("ingests", ingests);

        data.set("page", "/pages/data/item_group_import_list.jsp");
        return "/designs/auth.jsp";
    }


    public String viewIngest(Long businessId, ResponseData data){
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        String permission = Kilo.BUSINESS_MAINTENANCE + businessId;
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            data.set("message", "You don't have access to import for this business.");
            return "[redirect]/";
        }

        data.set("page", "/pages/data/item_group_import.jsp");
        return "/designs/auth.jsp";
    }



    public String ingest(Long businessId, ResponseData data, HttpServletRequest req){
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        String permission = Kilo.BUSINESS_MAINTENANCE + businessId;
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            data.set("message", "You don't have access to import for this business.");
            return "[redirect]/";
        }

        Business business = businessRepo.get(businessId);
        String key = business.getUri();
        if(!ingests.containsKey(key)){
            ingests.remove(key);
        }

        ItemGroupIngest ingest = null;
        try{

            Design design = designRepo.getBase(businessId);

            ingest = new ItemGroupIngest.Builder()
                    .withDesignId(design.getId())
                    .withBusinessId(businessId)
                    .withModelRepo(modelRepo)
                    .withGroupRepo(groupRepo)
                    .withOptionRepo(optionRepo)
                    .withPriceRepo(priceRepo)
                    .withIngestRepo(ingestRepo)
                    .withRequest(req)
                    .build()
                    .ingest();

            ingests.put(key, ingest);

        }catch (IOException | ServletException ex){
            ex.printStackTrace();
            data.set("message", "Oh no! Will you try again, it may be something small with your data.");
            return "[redirect]/";
        }

        IngestStats stats = getStats(businessId);
        Integer count = 0;
        if(stats != null)count = stats.getProcessed();

        if(count != null){
            data.set("message", "Successfully imported " + count + " item models");
        }else{
            data.set("message", "Nothing was imported because the Item Group name exists");
        }

        return "[redirect]/imports/item_groups/" + businessId;
    }


    public IngestStats getStats(Long businessId){
        Business business = businessRepo.get(businessId);
        if(business == null){
            return null;
        }
        String key = business.getUri();
        if(!ingests.containsKey(key)){
            return null;
        }
        ItemGroupIngest ingest = ingests.get(key);
        IngestStats ingestStats = ingest.getStats();
        return ingestStats;
    }

    public String deleteImportedGroup(Long groupId, Long businessId, ResponseData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        String permission = Kilo.BUSINESS_MAINTENANCE + businessId;
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            data.set("message", "You don't have access to import for this business.");
            return "[redirect]/";
        }
        /*
            delete pricing options
            delete pricing values
            delete group option values
            delete group options
            delete models
            delete item groups
            delete ingest
         */
        optionRepo.deleteOptionsGroup(groupId);
        optionRepo.deleteValuesGroup(groupId);
        priceRepo.deleteOptionsGroup(groupId);
        priceRepo.deleteValuesGroup(groupId);
        modelRepo.deleteGroup(groupId);
        groupRepo.delete(groupId);

        data.set("message", "Successfully removed item group");
        return "[redirect]/imports/item_groups/" + businessId;
    }

    public String deleteGroupImport(Long ingestId, Long businessId, ResponseData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        String permission = Kilo.BUSINESS_MAINTENANCE + businessId;
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            data.set("message", "You don't have access to import for this business.");
            return "[redirect]/";
        }
        /*
            delete pricing options
            delete pricing values
            delete group option values
            delete group options
            delete models
            delete item groups
            delete ingest
         */
        optionRepo.deleteOptions(ingestId);
        optionRepo.deleteValues(ingestId);
        priceRepo.deleteOptions(ingestId);
        priceRepo.deleteValues(ingestId);
        modelRepo.deleteIngest(ingestId);
        groupRepo.deleteIngest(ingestId);
        ingestRepo.delete(ingestId);

        data.set("message", "Successfully removed item group");
        return "[redirect]/imports/item_groups/" + businessId;
    }


    /**
     *
     *
     * Corina
     * Heidi
     * Holly
     * Jamie
     * Beautiful Indian woman
     * Seeyaa
     *
     * Started with
     * 2 others
     *
     */







//    public String ingestOld(Long businessId, ResponseData data, HttpServletRequest req){
//        if(!authService.isAuthenticated()){
//            return "[redirect]/";
//        }
//
//        String permission = Kilo.BUSINESS_MAINTENANCE + businessId;
//        if(!authService.isAdministrator() &&
//                !authService.hasPermission(permission)){
//            data.set("message", "You don't have access to import for this business.");
//            return "[redirect]/";
//        }
//
//        try {
//            ItemGroupIngest itemGroupImport = (ItemGroupIngest)qio.set(req, ItemGroupIngest.class);
//            if(itemGroupImport.getSpreadsheetId().equals("") ||
//                    itemGroupImport.getStartCell().equals("") ||
//                    itemGroupImport.getEndCell().equals("")){
//                data.set("itemGroupImport", itemGroupImport);
//                data.set("message", "Please make sure all fields are complete, we are sorry.");
//                return "[redirect]/" + businessId + "/imports/group_items";
//            }
//
//            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//            final String range = "Class Data!" + itemGroupImport.getStartCell() + ":" + itemGroupImport.getEndCell();
//
//            Sheets service = null;
//
//
//            service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//                    .setApplicationName(APPLICATION_NAME)
//                    .build();
//
//            ValueRange response = service.spreadsheets().values()
//                    .get(itemGroupImport.getSpreadsheetId(), range)
//                    .execute();
//
//            List<List<Object>> values = response.getValues();
//
//            if (values != null &&
//                    !values.isEmpty()) {
//                for (int z = 0; z < values.size(); z++) {
//                    List<Object> entries = values.get(z);
//                    for(Object entry : entries){
//                        System.out.print(entry);
//                    }
//                    System.out.println("");
//                }
//            }
//            if (values == null ||
//                    values.isEmpty()) {
//                data.set("itemGroupImport", itemGroupImport);
//                data.set("message", "Whao nelly, something aint right. Please make sure your data is correct.");
//                return "[redirect]/" + businessId + "/imports/group_items";
//            }
//
//            System.out.println("Name, Major");
//
//        } catch (IOException | GeneralSecurityException ex) {
//            ex.printStackTrace();
//            data.set("message", "Oh no! Unable to locate authentication credentials. Please contact someone!");
//            return "[redirect]/";
//        }
//
//        data.set("message", "Successfully retrieved and saved spreadsheet data.");
//        return "[redirect]/" + businessId + "/imports/group_items";
//    }


//    public Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException, GeneralSecurityException {
//        InputStream in = DataService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
//        if (in == null) {
//            throw new FileNotFoundException("creds not found : " + CREDENTIALS_FILE_PATH);
//        }
//        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
//
//
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
//                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
//                .setAccessType("offline")
//                .build();
//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
//        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
//
////        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);
////        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, clientSecrets, scopes).setDataStoreFactory(new MemoryDataStoreFactory())
////                .setAccessType("offline").build();
////        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("117778828736287500064");
//
//    }

}
