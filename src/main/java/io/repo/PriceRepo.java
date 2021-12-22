package io.repo;

import io.model.PricingOption;
import io.model.GroupOption;
import io.model.PricingValue;
import qio.Qio;
import qio.annotate.DataStore;
import qio.annotate.Inject;

import java.util.ArrayList;
import java.util.List;

@DataStore
public class PriceRepo {

    @Inject
    Qio qio;

    public PricingOption getSaved() {
        String idSql = "select max(id) from group_models";
        long id = qio.getLong(idSql, new Object[]{});
        return get(id);
    }

    public long getCount() {
        String sql = "select count(*) from group_models";
        Long count = (Long) qio.get(sql, new Object[] { }, Long.class);
        return count;
    }

    public PricingOption get(long id){
        String sql = "select * from group_models where id = [+]";
        PricingOption pricingOption = (PricingOption) qio.get(sql, new Object[] { id }, PricingOption.class);
        return pricingOption;
    }

    public List<PricingOption> getListOptions(long id){
        String sql = "select * from pricing_options where group_id = [+] order by id asc";
        List<PricingOption> pricingOptions = (ArrayList) qio.getList(sql, new Object[]{ id }, PricingOption.class);
        return pricingOptions;
    }

    public List<PricingValue> getListValues(long id){
        String sql = "select * from pricing_values where model_id = [+] order by id asc";
        List<PricingValue> pricingValues = (ArrayList) qio.getList(sql, new Object[]{ id }, PricingValue.class);
        return pricingValues;
    }

    public Boolean saveOption(PricingOption pricingOption){
        String sql = "insert into pricing_options (description, ingest_id, business_id, group_id) values ('[+]',[+],[+],[+])";
        qio.save(sql, new Object[] {
                pricingOption.getDescription(),
                pricingOption.getIngestId(),
                pricingOption.getBusinessId(),
                pricingOption.getGroupId()
        });
        return true;
    }

    public Boolean saveValue(PricingValue pricingValue){
        String sql = "insert into pricing_values (price, ingest_id, model_id, business_id) values ([+],[+],[+],[+])";
        qio.save(sql, new Object[] {
                pricingValue.getPrice(),
                pricingValue.getIngestId(),
                pricingValue.getModelId(),
                pricingValue.getBusinessId()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete from group_models where id = [+]";
        qio.delete(sql, new Object[] { id });
        return true;
    }

}
