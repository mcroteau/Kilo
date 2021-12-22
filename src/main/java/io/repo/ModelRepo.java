package io.repo;

import io.model.GroupModel;
import io.model.GroupOption;
import qio.Qio;
import qio.annotate.DataStore;
import qio.annotate.Inject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@DataStore
public class ModelRepo {

    @Inject
    Qio qio;

    public GroupModel getSaved() {
        String idSql = "select max(id) from group_models";
        long id = qio.getLong(idSql, new Object[]{});
        return get(id);
    }

    public long getCount() {
        String sql = "select count(*) from group_models";
        Long count = (Long) qio.get(sql, new Object[] { }, Long.class);
        return count;
    }

    public GroupModel get(long id){
        String sql = "select * from group_models where id = [+]";
        GroupModel groupModel = (GroupModel) qio.get(sql, new Object[] { id }, GroupModel.class);
        return groupModel;
    }

    public GroupModel get(String modelNumber){
        String sql = "select * from group_models where model_number = '[+]'";
        GroupModel groupModel = (GroupModel) qio.get(sql, new Object[] { modelNumber }, GroupModel.class);
        return groupModel;
    }

    public List<GroupModel> getList(long id){
        String sql = "select * from group_models where group_id = [+] order by id desc";
        List<GroupModel> groupModels = (ArrayList) qio.getList(sql, new Object[]{ id }, GroupModel.class);
        return groupModels;
    }

    public Boolean save(GroupModel groupModel){
        String sql = "insert into group_models (model_number, ingest_id, group_id, business_id, weight, quantity) values ('[+]',[+],[+],[+],[+],[+])";
        qio.save(sql, new Object[] {
                groupModel.getModelNumber(),
                groupModel.getIngestId(),
                groupModel.getGroupId(),
                groupModel.getBusinessId(),
                groupModel.getWeight(),
                groupModel.getQuantity(),
        });
        return true;
    }

    public boolean update(GroupModel storedModel) {
        String sql = "update group_models set quantity = [+] where id = [+]";
        qio.update(sql, new Object[] {
                storedModel.getWeight(),
                storedModel.getQuantity(),
                storedModel.getId()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete from group_models where id = [+]";
        qio.delete(sql, new Object[] { id });
        return true;
    }

}
