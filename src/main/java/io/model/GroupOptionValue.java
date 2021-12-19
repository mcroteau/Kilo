package io.model;

public class GroupOptionValue {
    Long id;
    Long groupItemId;
    Long groupOptionId;
    Long groupPricingId;
    String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupItemId() {
        return groupItemId;
    }

    public void setGroupItemId(Long groupItemId) {
        this.groupItemId = groupItemId;
    }

    public Long getGroupOptionId() {
        return groupOptionId;
    }

    public void setGroupOptionId(Long groupOptionId) {
        this.groupOptionId = groupOptionId;
    }

    public Long getGroupPricingId() {
        return groupPricingId;
    }

    public void setGroupPricingId(Long groupPricingId) {
        this.groupPricingId = groupPricingId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
