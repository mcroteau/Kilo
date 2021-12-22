package io.model;

public class ItemGroup {

    Long id;
    String name;
    Long businessId;
    Long designId;
    String imageUri;
    String pricingHeader;
    String qHeader;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Long getDesignId() {
        return designId;
    }

    public void setDesignId(Long designId) {
        this.designId = designId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getPricingHeader() {
        return pricingHeader;
    }

    public void setPricingHeader(String pricingHeader) {
        this.pricingHeader = pricingHeader;
    }

    public String getqHeader() {
        return qHeader;
    }

    public void setqHeader(String qHeader) {
        this.qHeader = qHeader;
    }
}
