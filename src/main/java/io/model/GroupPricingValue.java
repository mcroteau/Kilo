package io.model;

import java.math.BigDecimal;

public class GroupPricingValue {
    Long id;
    Long groupId;
    Long groupItemId;
    Long groupPricingOptionId;

    BigDecimal price;
    BigDecimal quantity;
    BigDecimal affiliatePrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getGroupItemId() {
        return groupItemId;
    }

    public void setGroupItemId(Long groupItemId) {
        this.groupItemId = groupItemId;
    }

    public Long getGroupPricingOptionId() {
        return groupPricingOptionId;
    }

    public void setGroupPricingOptionId(Long groupPricingOptionId) {
        this.groupPricingOptionId = groupPricingOptionId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAffiliatePrice() {
        return affiliatePrice;
    }

    public void setAffiliatePrice(BigDecimal affiliatePrice) {
        this.affiliatePrice = affiliatePrice;
    }
}
