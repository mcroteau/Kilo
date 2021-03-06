import chico.Chico;
import io.Kilo;
import io.model.*;
import io.repo.*;
import io.service.*;
import org.junit.jupiter.api.*;
import qio.Qio;
import qio.model.web.ResponseData;
import qio.support.DbMediator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StoreTest {

    Qio Qio;

    Business savedIoc;
    Business savedIkes;

    @Test
    @Order(1)
    public void aValidateCategories() {
        CategoryRepo categoryRepo = (CategoryRepo) Qio.getElement("categoryrepo");
        List<Category> categories = categoryRepo.getListAll(savedIkes.getId());
        Assertions.assertEquals(4, categories.size());
    }

    @Test
    @Order(2)
    public void bValidateCategoryItems() {
        CategoryRepo categoryRepo = (CategoryRepo) Qio.getElement("categoryrepo");
        List<ItemCategory> itemCategories = categoryRepo.getItemsBusiness(savedIkes.getId());
        Assertions.assertEquals(12, itemCategories.size());
    }

    @Test
    @Order(3)
    public void cValidateItems() {
        ItemRepo itemRepo = (ItemRepo) Qio.getElement("itemrepo");
        List<Item> items = itemRepo.getList(savedIkes.getId());
        Assertions.assertEquals(3, items.size());
    }

    @Test
    @Order(4)
    public void dAddCart(){
        List<String> parameters = new ArrayList<>();
        parameters.add("quantity=1");
        MockHttpRequest req = new MockHttpRequest(parameters);

        CartService cartService = (CartService) Qio.getElement("cartservice");
        cartService.add(Long.valueOf(6), "ikes", new ResponseData(), req);

        CartRepo cartRepo = (CartRepo) Qio.getElement("cartRepo");

        Cart cart = cartRepo.getSaved();

        List<CartItem> cartItems = cartRepo.getListItems(cart.getId());
        Assertions.assertEquals(1, cartItems.size());
    }

    @Test
    @Order(5)
    public void gCheckout(){
        CartRepo cartRepo = (CartRepo) Qio.getElement("cartRepo");

        List<String> shipParameters = new ArrayList<>();
        shipParameters.add("name=Jacques Fresco");
        shipParameters.add("username=croteau.mike+jqa@gmail.com");
        shipParameters.add("shipStreet=21 Valley Lane");
        shipParameters.add("shipCity=Venus");
        shipParameters.add("shipState=Florida");
        shipParameters.add("shipZip=33960");
        shipParameters.add("shipCountry=US");

        MockHttpRequest shipReq = new MockHttpRequest(shipParameters);
        ShipmentService shipmentService = (ShipmentService) Qio.getElement("shipmentservice");
        shipmentService.save("ikes", new ResponseData(), shipReq);

        Cart cart = cartRepo.getSaved();

        List<String> cardParameters = new ArrayList<>();
        cardParameters.add("card=4242424242424242");
        cardParameters.add("expMonth=12");
        cardParameters.add("expYear=2029");
        cardParameters.add("cvc=421");
        cardParameters.add("chargeAmount=" + cart.getTotal());

        MockHttpRequest cardReq = new MockHttpRequest(cardParameters);

        SaleService saleService = (SaleService) Qio.getElement("saleservice");
        saleService.processSale(cart.getId(), "ikes", cardReq);

        SaleRepo saleRepo = (SaleRepo) Qio.getElement("salerepo");
        Sale sale = saleRepo.getSaved();

        Assertions.assertEquals(4836, sale.getPrimaryAmount());
        Assertions.assertEquals(723, sale.getAffiliateAmount());
    }


    @BeforeAll
    public void setup(){
        List<String> properties = Arrays.asList(new String[]{ "qio.props" });
        try {
            this.Qio = new Qio.Injector()
                    .setBasic(false)
                    .setCreateDb(true)
                    .setDropDb(true)
                    .withContext(new MockServletContext())
                    .withPropertyFiles(properties)
                    .withWebResources(new ArrayList<>())
                    .inject();
        }catch(Exception ex){
            ex.printStackTrace();
        }

        RoleRepo roleRepo = (RoleRepo) Qio.getElement("rolerepo");
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

        ItemRepo itemRepo = (ItemRepo) Qio.getElement("itemrepo");
        CategoryRepo categoryRepo = (CategoryRepo) Qio.getElement("categoryrepo");
        DesignRepo designRepo = (DesignRepo) Qio.getElement("designrepo");

        BusinessService businessService = (BusinessService) Qio.getElement("businessService");

        UserRepo userRepo = (UserRepo) Qio.getElement("userrepo");
        User iocUser = new User();
        iocUser.setUsername("croteau.mike+ioc@gmail.com");
        iocUser.setPassword(Chico.dirty("password"));
        userRepo.save(iocUser);
        User savedIocUser = userRepo.getSaved();

        BusinessRepo businessRepo = (BusinessRepo) Qio.getElement("businessrepo");
        Business ioc = new Business();
        ioc.setName("IOC");
        ioc.setUserId(savedIocUser.getId());
        businessRepo.save(ioc);

        savedIoc = businessRepo.getSaved();
        savedIoc.setName("IOC");
        savedIoc.setUri("ioc");
        savedIoc.setStripeId("acct_1K0gwt2HGj2rN0Zk");
        businessRepo.update(savedIoc);

        try{
            businessService.configure(savedIoc, true);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        Design iocDesign = designRepo.getSaved();

        Business ikes = new Business();
        ikes.setName("Ikes Cola");
        ikes.setUri("ikes");
        businessRepo.save(ikes);

        savedIkes = businessRepo.getSaved();

        Category shopCategory = new Category();
        shopCategory.setTopLevel(true);
        shopCategory.setBusinessId(savedIoc.getId());
        shopCategory.setName("Shop");
        shopCategory.setUri("shop");
        shopCategory.setDesignId(iocDesign.getId());
        categoryRepo.save(shopCategory);
        Category savedShopCategory = categoryRepo.getSaved();

        Category clothing = new Category();
        clothing.setName("Clothing");
        clothing.setUri("clothing");
        clothing.setBusinessId(ioc.getId());
        clothing.setDesignId(iocDesign.getId());
        clothing.setBusinessId(savedIoc.getId());
        categoryRepo.save(clothing);
        Category savedClothingCategory = categoryRepo.getSaved();


        Category shoes = new Category();
        shoes.setName("Shoes");
        shoes.setUri("shoes");
        shoes.setBusinessId(ioc.getId());
        shoes.setDesignId(iocDesign.getId());
        shoes.setBusinessId(savedIoc.getId());
        categoryRepo.save(shoes);
        Category savedShoesCategory = categoryRepo.getSaved();



        Category makeup = new Category();
        makeup.setName("Makeup");
        makeup.setUri("makeup");
        makeup.setBusinessId(ioc.getId());
        makeup.setDesignId(iocDesign.getId());
        makeup.setBusinessId(savedIoc.getId());
        categoryRepo.save(makeup);
        Category savedMakeupCategory = categoryRepo.getSaved();


        for(int z = 0; z < 3; z++){
            Item item = new Item();
            item.setDesignId(iocDesign.getId());
            item.setBusinessId(savedIoc.getId());
            item.setName("Kilo Item " + Kilo.getString(4));
            item.setImageUri(Kilo.OCEAN_ENDPOINT + Kilo.ITEM_IMAGE);
            item.setPrice(new BigDecimal(45));
            item.setAffiliatePrice(new BigDecimal(45));
            item.setQuantity(new BigDecimal(Kilo.getNumber(30)));
            item.setWeight(new BigDecimal(Kilo.getNumber(48)));
            item.setCost(new BigDecimal(50));
            itemRepo.save(item);

            Item savedItem = itemRepo.getSaved();

            ItemCategory itemCategory = new ItemCategory();
            itemCategory.setBusinessId(savedIoc.getId());
            itemCategory.setCategoryId(savedShopCategory.getId());
            itemCategory.setItemId(savedItem.getId());
            categoryRepo.saveItem(itemCategory);

            ItemCategory itemCategoryDos = new ItemCategory();
            itemCategoryDos.setBusinessId(savedIoc.getId());
            itemCategoryDos.setCategoryId(savedClothingCategory.getId());
            itemCategoryDos.setItemId(savedItem.getId());
            categoryRepo.saveItem(itemCategoryDos);

            ItemCategory itemCategoryTres = new ItemCategory();
            itemCategoryTres.setBusinessId(savedIoc.getId());
            itemCategoryTres.setCategoryId(savedShoesCategory.getId());
            itemCategoryTres.setItemId(savedItem.getId());
            categoryRepo.saveItem(itemCategoryTres);

            ItemCategory itemCategoryQuatro = new ItemCategory();
            itemCategoryQuatro.setBusinessId(savedIoc.getId());
            itemCategoryQuatro.setCategoryId(savedMakeupCategory.getId());
            itemCategoryQuatro.setItemId(savedItem.getId());
            categoryRepo.saveItem(itemCategoryQuatro);

        }


        List<String> parameters = new ArrayList<>();
        parameters.add("id=" + savedIkes.getId());
        parameters.add("name=Ikes Cola");
        parameters.add("uri=ikes");
        parameters.add("affiliate=true");
        parameters.add("phone=9079878652");
        parameters.add("email=croteau.mike+ikes@gmail.com");
        parameters.add("password=gigamarket");
        parameters.add("owner=Mike");
        parameters.add("shipping=13");
        parameters.add("flatShipping=true");
        parameters.add("street=21 Valley Lane");
        parameters.add("city=Venus");
        parameters.add("state=Florida");
        parameters.add("zip=33960");
        parameters.add("country=US");
        parameters.add("primaryId=" + savedIoc.getId());

        MockHttpRequest req = new MockHttpRequest(parameters);

        AffiliateService affiliateService = (AffiliateService) Qio.getElement("affiliateservice");
        affiliateService.finalizeOnboarding(new ResponseData(), req);
    }

    @AfterAll
    public void tearDown(){
        DbMediator mediator = (DbMediator) Qio.getElement("dbmediator");
        mediator.dropDb();
    }

}
