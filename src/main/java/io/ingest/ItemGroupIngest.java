package io.ingest;

import io.Kilo;
import io.model.*;
import io.repo.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ItemGroupIngest {

    final int MODEL_NUMBER = 0;
    final int QUANTITY     = 1;
    final int WEIGHT       = 2;

    final int PRICE_HEADER = 1;

    final int FIRST_ROW  = 1;
    final int SECOND_ROW = 2;
    final int THIRD_ROW  = 3;

    IngestStats ingestStats;

    Integer priceIdx = null;
    Integer weightIdx = null;
    Integer quantityIdx = null;

    Long designId;
    Long businessId;
    ModelRepo modelRepo;
    GroupRepo groupRepo;
    PriceRepo priceRepo;
    OptionRepo optionRepo;
    IngestRepo ingestRepo;
    HttpServletRequest req;
    List<GroupModel> groupModels;

    public ItemGroupIngest(Builder builder){
        this.req = builder.req;
        this.designId = builder.designId;
        this.ingestRepo = builder.ingestRepo;
        this.businessId = builder.businessId;
        this.modelRepo = builder.modelRepo;
        this.groupRepo = builder.groupRepo;
        this.groupModels = new ArrayList<>();
        this.ingestStats = new IngestStats();
    }

    public ItemGroupIngest ingest() throws IOException, ServletException {
        List<Part> parts = req.getParts()
                .stream()
                .filter(part -> "media".equals(part.getName()) && part.getSize() > 0)
                .collect(Collectors.toList());

        Integer processed = 0;
        Integer unprocessed = 0;

        for(int q = 0; q < parts.size(); q++){

            Ingest ingest = new Ingest();
            ingest.setBusinessId(businessId);
            ingest.setDateIngest(Kilo.getDate());
            ingestRepo.save(ingest);

            Ingest savedIngest = ingestRepo.getSaved();

            Part part = parts.get(q);
            InputStream in = part.getInputStream();
            Scanner scanner = new Scanner(in);

            ItemGroup savedGroup = null;

            Integer idx = 0;

            while(scanner.hasNext()) {
                idx++;
                String line = scanner.nextLine();
                String[] values = line.split(",");

                System.out.println(values.length);


                if (idx.equals(this.SECOND_ROW)) setIndexes(values);

                if (idx.equals(this.FIRST_ROW)){
                    savedGroup = createGroup(designId, businessId, values);
                    if (savedGroup == null) {
                        unprocessed++;
                        ingestStats.setUnprocessed(unprocessed);
                        continue;
                    }
                }

                if (idx.equals(this.THIRD_ROW)) {
                    for (int z = 0; z < values.length; z++) {
                        if (z >= this.quantityIdx && z < this.priceIdx) {
                            GroupOption groupOption = new GroupOption();
                            groupOption.setIngestId(savedIngest.getId());
                            groupOption.setBusinessId(businessId);
                            groupOption.setGroupId(savedGroup.getId());
                            optionRepo.saveOption(groupOption);
                        }
                        if(z >= this.priceIdx){
                            String description = values[z];
                            PricingOption pricingOption = new PricingOption();
                            pricingOption.setIngestId(savedIngest.getId());
                            pricingOption.setDescription(description);
                            pricingOption.setBusinessId(businessId);
                            pricingOption.setGroupId(savedGroup.getId());
                            priceRepo.saveOption(pricingOption);
                        }
                    }
                }

                if (idx.compareTo(this.THIRD_ROW) >= 0) {

                    for (int z = 0; z < values.length; z++) {
                        String modelNumber = values[this.MODEL_NUMBER];
                        String weight = values[this.WEIGHT];
                        String quantity = values[this.QUANTITY];

                        if (modelNumber.equals("")) continue;
                        if (quantity.equals("")) continue;
                        if (weight.equals("")) continue;

                        GroupModel storedModel = modelRepo.get(modelNumber);
                        if (storedModel != null) {
                            storedModel.setQuantity(new BigDecimal(quantity));
                            modelRepo.update(storedModel);
                        }

                        if (storedModel == null) {
                            GroupModel groupModel = new GroupModel();
                            groupModel.setModelNumber(modelNumber);
                            groupModel.setIngestId(savedIngest.getId());
                            groupModel.setGroupId(savedGroup.getId());
                            groupModel.setBusinessId(businessId);
                            groupModel.setWeight(new BigDecimal(weight));
                            groupModel.setQuantity(new BigDecimal(quantity));
                            modelRepo.save(groupModel);
                        }


                        GroupModel savedModel = modelRepo.get(modelNumber);

                        if (z >= quantityIdx && z < priceIdx) {
                            String value = values[z];
                            if (value.equals("")) {
                                modelRepo.delete(savedModel.getId());
                                unprocessed++;
                                ingestStats.setUnprocessed(unprocessed);
                                continue;
                            }
                            GroupOptionValue groupValue = new GroupOptionValue();
                            groupValue.setIngestId(savedIngest.getId());
                            groupValue.setBusinessId(businessId);
                            groupValue.setModelId(savedModel.getId());
                            groupValue.setValue(value);
                            optionRepo.saveValue(groupValue);
                        }
                        if (z >= priceIdx) {
                            String price = values[z];
                            if (price.equals("")) {
                                //Todo: delete model
                                optionRepo.deleteValues(savedModel.getId());
                                modelRepo.delete(savedModel.getId());
                                unprocessed++;
                                ingestStats.setUnprocessed(unprocessed);
                                continue;
                            }
                            try {
                                PricingValue pricingValue = new PricingValue();
                                pricingValue.setIngestId(savedIngest.getId());
                                pricingValue.setGroupId(savedGroup.getId());
                                pricingValue.setBusinessId(businessId);
                                pricingValue.setModelId(savedModel.getId());
                                pricingValue.setPrice(new BigDecimal(price));
                                priceRepo.saveValue(pricingValue);
                            } catch (Exception ex) {
                                //Todo: delete
                                //delete model options
                                //model
                                optionRepo.deleteValues(savedModel.getId());
                                modelRepo.delete(savedModel.getId());
                                unprocessed++;
                                ingestStats.setUnprocessed(unprocessed);

                            }

                        }

                    }

                    processed++;
                    ingestStats.setCount(idx);
                    ingestStats.setProcessed(processed);
                    ingestStats.setUnprocessed(unprocessed);
                }
            }
        }

        return this;
    }


    public IngestStats getStats(){
        return this.ingestStats;
    }

    protected ItemGroup createGroup(Long designId, Long businessId, String[] values){
        String name = values[0];
        if(name.equals("")){
            return null;
        }

        String priceHeader = "";
        if(values.length >= 2) {
            priceHeader = values[this.PRICE_HEADER];
        }
        if (priceHeader.equals("")) {
            return null;
        }

        String quantityHeader = "";
        if(values.length >= 3) {
            quantityHeader = values[2];
        }

        ItemGroup itemGroup = new ItemGroup();
        itemGroup.setBusinessId(businessId);
        itemGroup.setDesignId(designId);
        itemGroup.setName(name);
        itemGroup.setPricingHeader(priceHeader);
        itemGroup.setqHeader(quantityHeader);
        groupRepo.save(itemGroup);
        ItemGroup savedItemGroup = groupRepo.getSaved();
        return savedItemGroup;
    }


    protected void setIndexes(String[] values){
        for(int idx = 0; idx < values.length; idx++){
            if(this.priceIdx == null &&
                    values[idx].equals("::prices::")){
                this.priceIdx = idx;
            }
            if(values[idx].equals("::weight::")){
                this.weightIdx = idx;
            }
            if(values[idx].equals("::quantity::")){
                this.quantityIdx = idx;
            }
        }
    }

    public static class Builder{
        Long designId;
        Long businessId;
        IngestRepo ingestRepo;
        ModelRepo modelRepo;
        GroupRepo groupRepo;
        PriceRepo priceRepo;
        OptionRepo optionRepo;
        HttpServletRequest req;

        public Builder withDesignId(Long designId){
            this.designId = designId;
            return this;
        }
        public Builder withBusinessId(Long businessId){
            this.businessId = businessId;
            return this;
        }
        public Builder withModelRepo(ModelRepo modelRepo){
            this.modelRepo = modelRepo;
            return this;
        }
        public Builder withGroupRepo(GroupRepo groupRepo){
            this.groupRepo = groupRepo;
            return this;
        }
        public Builder withOptionRepo(OptionRepo optionRepo){
            this.optionRepo = optionRepo;
            return this;
        }
        public Builder withPriceRepo(PriceRepo priceRepo){
            this.priceRepo = priceRepo;
            return this;
        }
        public Builder withIngestRepo(IngestRepo ingestRepo){
            this.ingestRepo = ingestRepo;
            return this;
        }
        public Builder withRequest(HttpServletRequest req){
            this.req = req;
            return this;
        }
        public ItemGroupIngest build(){
            return new ItemGroupIngest(this);
        }
    }
}
