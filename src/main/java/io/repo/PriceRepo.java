package io.repo;

import io.model.PricingOption;
import io.model.GroupOption;
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

    public PricingOption get(String modelNumber){
        String sql = "select * from group_models where model_number = '[+]'";
        PricingOption pricingOption = (PricingOption) qio.get(sql, new Object[] { modelNumber }, PricingOption.class);
        return pricingOption;
    }

    public List<PricingOption> getList(long id){
        String sql = "select * from group_models where business_id = [+] order by id desc";
        List<PricingOption> pricingOptions = (ArrayList) qio.getList(sql, new Object[]{ id }, PricingOption.class);
        return pricingOptions;
    }

    public Boolean save(PricingOption pricingOption){
        String sql = "insert into group_models (model_number, group_id, weight) values ('[+]',[+],[+])";
        qio.save(sql, new Object[] {

        });
        return true;
    }

    public boolean update(PricingOption storedPricingOption) {
        String sql = "update group_models set quantity = [+] where id = [+]";
        qio.update(sql, new Object[] {

        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete from group_models where id = [+]";
        qio.delete(sql, new Object[] { id });
        return true;
    }

}
