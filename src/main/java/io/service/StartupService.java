package io.service;

import io.Kilo;
import io.model.*;
import io.repo.*;
import chico.Chico;
import io.support.DbAccess;
import qio.annotate.Inject;
import qio.annotate.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
public class StartupService {

    @Inject
    CartRepo cartRepo;

    @Inject
    SaleRepo saleRepo;

    @Inject
    StatusRepo statusRepo;

    @Inject
    ItemRepo itemRepo;

    @Inject
    UserRepo userRepo;

    @Inject
    RoleRepo roleRepo;

    @Inject
    BusinessRepo businessRepo;

    @Inject
    CategoryRepo categoryRepo;

    @Inject
    DesignRepo designRepo;

    @Inject
    DbAccess dbAccess;

    @Inject
    BusinessService businessService;


    public void start() throws Exception {

        Chico.configure(dbAccess);
        String password = Chico.dirty(Kilo.SUPER_PASSWORD);

        Role superRole = roleRepo.find(Kilo.SUPER_ROLE);
        Role businessRole = roleRepo.find(Kilo.BUSINESS_ROLE);
        Role customerRole = roleRepo.find(Kilo.CUSTOMER_ROLE);

        if(superRole == null){
            superRole = new Role();
            superRole.setName(Kilo.SUPER_ROLE);
            roleRepo.save(superRole);
        }

        if(businessRole == null){
            businessRole = new Role();
            businessRole.setName(Kilo.BUSINESS_ROLE);
            roleRepo.save(businessRole);
        }

        if(customerRole == null){
            customerRole = new Role();
            customerRole.setName(Kilo.CUSTOMER_ROLE);
            roleRepo.save(customerRole);
        }


        Role savedBusinessRole = roleRepo.get(Kilo.BUSINESS_ROLE);
        Role savedCustomerRole = roleRepo.get(Kilo.CUSTOMER_ROLE);

        mock(password, savedCustomerRole,  savedBusinessRole);
    }

    protected void mock(String password, Role savedCustomerRole, Role savedBusinessRole){

        ///////// patron //////////
        User patron = new User();
        patron.setUsername("croteau.mike+patron@gmail.com");
        patron.setPassword(password);
        patron.setDateJoined(Kilo.getDate());
        userRepo.save(patron);
        User savedPatron = userRepo.getSaved();

        userRepo.saveUserRole(savedPatron.getId(), savedCustomerRole.getId());
        String patronPermission = Kilo.USER_MAINTENANCE + savedPatron.getId();
        userRepo.savePermission(savedPatron.getId(), patronPermission);

        //////// business user ////////
        User ibmUser = new User();
        ibmUser.setName("Ibm User");
        ibmUser.setPhone("9079878652");
        ibmUser.setUsername("croteau.mike+ibm@gmail.com");
        ibmUser.setPassword(password);
        ibmUser.setDateJoined(Kilo.getDate());
        userRepo.save(ibmUser);
        User savedIbmUser = userRepo.getSaved();

        System.out.println("saved ibm user " + savedIbmUser.getPassword());

        savedIbmUser.setPhone("9079878652");
        savedIbmUser.setShipStreet("21 Valley Lane");
        savedIbmUser.setShipStreetDos("Thank you!");
        savedIbmUser.setShipCity("Venus");
        savedIbmUser.setShipState("Florida");
        savedIbmUser.setShipZip("33960");
        savedIbmUser.setShipCountry("US");
        userRepo.update(savedIbmUser);

        userRepo.saveUserRole(savedIbmUser.getId(), savedBusinessRole.getId());
        String permission = Kilo.USER_MAINTENANCE + savedIbmUser.getId();
        userRepo.savePermission(savedIbmUser.getId(), permission);


        Business business = new Business();
        business.setName("Kopi Nuwak Distributors");
        business.setUri("kopi");
        business.setUserId(savedIbmUser.getId());
        businessRepo.save(business);
        Business savedKopi = businessRepo.getSaved();

        savedKopi.setPhone("9079878652");
        savedKopi.setStreet("1 New Orchard Road");
        savedKopi.setStreetDos("");
        savedKopi.setCity("Armonk");
        savedKopi.setState("New York");
        savedKopi.setZip("10504-1722");
        savedKopi.setCountry("US");
        savedKopi.setActivationComplete(true);
        savedKopi.setBaseCommission(new BigDecimal(14));
        savedKopi.setStripeId("acct_1Juk6N2HqO73ZqT6");
        businessRepo.update(savedKopi);

        String ibmPermission = Kilo.BUSINESS_MAINTENANCE + savedKopi.getId();
        userRepo.savePermission(savedIbmUser.getId(), ibmPermission);
        try {
            businessService.configure(savedKopi, true);
        } catch (Exception e) {
            e.printStackTrace();
        }


        UserBusiness userBusinessIbm = new UserBusiness(savedIbmUser.getId(), savedKopi.getId());
        businessRepo.saveUser(userBusinessIbm);

        UserBusiness userBusinessPatron = new UserBusiness(savedPatron.getId(), savedKopi.getId());
        businessRepo.saveUser(userBusinessPatron);

        userRepo.update(savedIbmUser);

        Category mensCategory = saveCategory("Shop", savedKopi.getId(), null, savedIbmUser);
        Category clothingCategory = saveCategory("Clothing", savedKopi.getId(), mensCategory.getId(), savedIbmUser);
        Category shoesCategory = saveCategory("Shoes", savedKopi.getId(), clothingCategory.getId(), savedIbmUser);

        for(int k = 0; k < 91; k++) {
            Item item = new Item();
            item.setDesignId(designRepo.getSaved().getId());
            item.setBusinessId(savedKopi.getId());
            item.setName("Kilo Item " + Kilo.getString(4));
            item.setImageUri(Kilo.OCEAN_ENDPOINT + Kilo.ITEM_IMAGE);
            item.setPrice(new BigDecimal(45));
            item.setQuantity(new BigDecimal(Kilo.getNumber(30)));
            item.setWeight(new BigDecimal(Kilo.getNumber(48)));
            item.setCost(new BigDecimal(50));
            itemRepo.save(item);

            Item savedItem = itemRepo.getSaved();

            String itemPermission = Kilo.ITEM_MAINTENANCE + savedItem.getId();
            userRepo.savePermission(savedIbmUser.getId(), itemPermission);

            ItemCategory itemCategory = new ItemCategory(savedItem.getId(), mensCategory.getId(), savedKopi.getId());
            categoryRepo.saveItem(itemCategory);

            ItemCategory itemCategoryDos = new ItemCategory(savedItem.getId(), clothingCategory.getId(), savedKopi.getId());
            categoryRepo.saveItem(itemCategoryDos);

            ItemCategory itemCategoryTres = new ItemCategory(savedItem.getId(), shoesCategory.getId(), savedKopi.getId());
            categoryRepo.saveItem(itemCategoryTres);

            ItemOption itemOption = new ItemOption();
            itemOption.setItemId(savedItem.getId());
            itemOption.setName("Size");
            itemRepo.saveOption(itemOption);

            ItemOption savedItemOption = itemRepo.getSavedOption();
            OptionValue optionValue = new OptionValue();
            optionValue.setItemOptionId(savedItemOption.getId());
            optionValue.setValue("Size 10");
            optionValue.setPrice(new BigDecimal(12));
            itemRepo.saveValue(optionValue);


            ItemOption itemOptionDos = new ItemOption();
            itemOptionDos.setItemId(savedItem.getId());
            itemOptionDos.setName("Color");
            itemRepo.saveOption(itemOptionDos);

            ItemOption savedItemOptionDos = itemRepo.getSavedOption();
            OptionValue optionValueDos = new OptionValue();
            optionValueDos.setItemOptionId(savedItemOptionDos.getId());
            optionValueDos.setValue("Blue + White");
            optionValueDos.setPrice(new BigDecimal(12));
            itemRepo.saveValue(optionValueDos);

            Cart cart = new Cart();
            cart.setUserId(savedIbmUser.getId());
            cart.setActive(true);
            cart.setBusinessId(savedKopi.getId());
            cartRepo.save(cart);

            Cart savedCart = cartRepo.getSaved();

            CartItem cartItem = new CartItem();
            cartItem.setItemId(savedItem.getId());
            cartItem.setPrice(savedItem.getPrice());
            cartItem.setQuantity(new BigDecimal(Kilo.getNumber(4)));
            cartItem.setCartId(savedCart.getId());
            cartItem.setBusinessId(savedKopi.getId());
            cartRepo.saveItem(cartItem);

            CartItem savedCartItem = cartRepo.getSavedItem();

            if(k % 3 == 0) {

                BigDecimal subtotal = savedCartItem.getPrice().multiply(cartItem.getQuantity());
                BigDecimal shipping = new BigDecimal(13.01);
                BigDecimal total = subtotal.add(shipping, new MathContext(3, RoundingMode.HALF_DOWN));
                savedCart.setSubtotal(subtotal);
                savedCart.setShipping(shipping);
                savedCart.setTotal(total);
                savedCart.setSale(true);
                savedCart.setActive(false);

                savedCart.setShipName(Kilo.getString(4) + " " + Kilo.getString(7));
                savedCart.setShipEmail("croteau.mike+" + Kilo.getString(3) + "@gmail.com");
                savedCart.setShipPhone("9079878652");
                savedCart.setShipStreet("1097 Park Dr");
                savedCart.setShipStreetDos("");
                savedCart.setShipCity("Fairbanks");
                savedCart.setShipState("Alaska");
                savedCart.setShipZip("99709");
                savedCart.setShipCountry("US");

                cartRepo.update(savedCart);

                Sale sale = new Sale();
                sale.setUserId(savedPatron.getId());
                sale.setCartId(savedCart.getId());
                sale.setAmount(savedCart.getTotal());
                sale.setSalesDate(Kilo.getDate());
                saleRepo.save(sale);

                Sale savedSale = saleRepo.getSaved();

                savedSale.setPrimaryId(savedKopi.getId());

                savedSale.setPrimaryAmount(savedSale.getAmount().movePointRight(2).longValueExact());
                savedSale.setStripePrimaryCustomerId("z_123");
                savedSale.setStripePrimaryChargeId("z_921");
                saleRepo.updatePrimary(savedSale);
            }

        }


        User user = new User();
        user.setUsername("croteau.mike+graphite@gmail.com");
        user.setPassword(Chico.dirty("password"));
        userRepo.save(user);

        User savedGraphiteUser = userRepo.getSaved();

        Business imDone = new Business();
        imDone.setUserId(savedGraphiteUser.getId());
        imDone.setName("Golden Graphite");
        imDone.setUri("golden");
        businessRepo.save(imDone);
        Business savedImDone= businessRepo.getSaved();


        savedImDone.setPhone("9079878652");
        savedImDone.setStreet("1097 Park Dr.");
        savedImDone.setStreetDos("");
        savedImDone.setCity("Fairbanks");
        savedImDone.setState("Alaska");
        savedImDone.setZip("99709");
        savedImDone.setCountry("US");
        savedImDone.setActivationComplete(true);
        savedImDone.setAffiliate(true);
        savedImDone.setOwner("Zink");
        savedImDone.setPrimaryId(savedKopi.getId());
        savedImDone.setBaseCommission(new BigDecimal(13));
        savedImDone.setStripeId("acct_1K0gwt2HGj2rN0Zk");

        businessRepo.update(savedImDone);
        String imDonePermission = Kilo.BUSINESS_MAINTENANCE + savedImDone.getId();
        userRepo.savePermission(savedGraphiteUser.getId(), imDonePermission);

        try {
            businessService.configure(savedImDone,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        UserBusiness userBusiness = new UserBusiness();
        userBusiness.setBusinessId(savedImDone.getId());
        userBusiness.setUserId(savedGraphiteUser.getId());
        businessRepo.saveUser(userBusiness);


        Category category = new Category();
        category.setTopLevel(true);
        category.setBusinessId(savedImDone.getId());
        category.setUri("shop");
        category.setName("Shop");
        category.setDesignId(designRepo.getSaved().getId());
        categoryRepo.save(category);

        Category savedCategory = categoryRepo.getSaved();

        String categoryPermission = Kilo.CATEGORY_MAINTENANCE + savedCategory.getId();
        userRepo.savePermission(savedIbmUser.getId(), categoryPermission);

        Item item = new Item();
        item.setDesignId(designRepo.getSaved().getId());
        item.setBusinessId(savedImDone.getId());
        item.setName("Kilo Item " + Kilo.getString(3));
        item.setImageUri(Kilo.OCEAN_ENDPOINT + Kilo.ITEM_IMAGE);
        item.setPrice(new BigDecimal(45));
        item.setQuantity(new BigDecimal(Kilo.getNumber(30)));
        item.setWeight(new BigDecimal(Kilo.getNumber(48)));
        item.setCost(new BigDecimal(20));
        itemRepo.save(item);

        Item savedItem = itemRepo.getSaved();

        String itemPermission = Kilo.ITEM_MAINTENANCE + savedItem.getId();
        userRepo.savePermission(savedIbmUser.getId(), itemPermission);

        ItemCategory shopItem = new ItemCategory(savedItem.getId(), savedCategory.getId(), savedImDone.getId());
        categoryRepo.saveItem(shopItem);



        BusinessRequest businessRequest = new BusinessRequest();
        businessRequest.setEmail("croteau.mike+ioc@gmail.com");
        businessRequest.setBusinessId(savedKopi.getId());
        businessRequest.setBusinessName("IOC");
        businessRequest.setGuid(Kilo.getString(6));
        businessRequest.setName("Mike");
        businessRequest.setPhone("9079878652");
        businessRequest.setNotes("Please let me sell your stuff beauty queen.");
        businessRepo.saveRequest(businessRequest);

        BusinessRequest savedBusinessRequest = businessRepo.getSavedRequest();

        savedBusinessRequest.setApproved(true);
        savedBusinessRequest.setDenied(false);
        savedBusinessRequest.setPending(false);
        businessRepo.updateRequest(savedBusinessRequest);
    }


    protected Category saveCategory(String name, Long businessId, Long categoryId, User user){
        Category category = new Category();
        category.setName(name);
        category.setUri(name.toLowerCase());
        category.setHeader(Kilo.getString(7));
        category.setBusinessId(businessId);
        category.setDesignId(designRepo.getSaved().getId());
        if(categoryId == null) category.setTopLevel(true);
        if(categoryId != null) category.setCategoryId(categoryId);
        categoryRepo.save(category);
        Category savedCategory = categoryRepo.getSaved();

        String permission = Kilo.CATEGORY_MAINTENANCE + savedCategory.getId();
        userRepo.savePermission(user.getId(), permission);

        return savedCategory;
    }

}
