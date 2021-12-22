package io.model;

public class Ingest {
    Long id;
    Long businessId;
    Long dateIngest;

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

    public Long getDateIngest() {
        return dateIngest;
    }

    public void setDateIngest(Long dateIngest) {
        this.dateIngest = dateIngest;
    }
}
