package io.repo;

import io.model.*;
import io.model.GroupOption;
import qio.Qio;
import qio.annotate.DataStore;
import qio.annotate.Inject;

import java.util.ArrayList;
import java.util.List;

@DataStore
public class OptionRepo {

    @Inject
    Qio qio;

    public GroupOption getSaved() {
        String idSql = "select max(id) from group_options";
        long id = qio.getLong(idSql, new Object[]{});
        return get(id);
    }

    public long getCount() {
        String sql = "select count(*) from group_options";
        Long count = (Long) qio.getLong(sql, new Object[] { });
        return count;
    }

    public GroupOption get(long id){
        String sql = "select * from group_options where id = [+]";
        GroupOption itemGroup = (GroupOption) qio.get(sql, new Object[] { id }, GroupOption.class);
        return itemGroup;
    }

    public GroupOption get(String name){
        String sql = "select * from group_options where name = '[+]'";
        GroupOption itemGroup = (GroupOption) qio.get(sql, new Object[] { name }, GroupOption.class);
        return itemGroup;
    }

    public GroupOption get(long id, long businessId){
        String sql = "select * from group_options where id = [+] and business_id = [+]";
        GroupOption itemGroup = (GroupOption) qio.get(sql, new Object[] { id, businessId }, GroupOption.class);
        return itemGroup;
    }

    public List<GroupOption> getList(){
        String sql = "select * from group_options order by id desc";
        List<GroupOption> itemGroupOptions = (ArrayList) qio.getList(sql, new Object[]{}, GroupOption.class);
        return itemGroupOptions;
    }

    public List<GroupOption> getList(long id){
        String sql = "select * from group_options where business_id = [+] and active = true order by id desc";
        List<GroupOption> itemGroupOptions = (ArrayList) qio.getList(sql, new Object[]{ id }, GroupOption.class);
        return itemGroupOptions;
    }

    public Boolean saveOption(GroupOption groupOption){
        String sql = "insert into group_options (group_id, ingest_id, business_id, title) values ([+],[+],[+],'[+]')";
        qio.save(sql, new Object[] {
                groupOption.getGroupId(),
                groupOption.getIngestId(),
                groupOption.getBusinessId(),
                groupOption.getTitle()
        });
        return true;
    }

    public Boolean saveValue(GroupOptionValue groupValue){
        String sql = "insert into group_option_values (ingest_id, model_id, business_id, value) values ([+],[+],[+],'[+]')";
        qio.save(sql, new Object[] {
                groupValue.getIngestId(),
                groupValue.getModelId(),
                groupValue.getBusinessId(),
                groupValue.getValue()
        });
        return true;
    }

    public boolean updateValue(GroupModel storedModel) {
        String sql = "update group_option_values set quantity = [+] where id = [+]";
        qio.update(sql, new Object[] {
                storedModel.getWeight(),
                storedModel.getQuantity(),
                storedModel.getId()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete from group_options where id = [+]";
        qio.delete(sql, new Object[] { id });
        return true;
    }


}
