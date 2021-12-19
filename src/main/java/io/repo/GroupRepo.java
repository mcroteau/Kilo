package io.repo;

import io.model.Group;
import qio.Qio;
import qio.annotate.DataStore;
import qio.annotate.Inject;

import java.util.ArrayList;
import java.util.List;

@DataStore
public class GroupRepo {

    @Inject
    Qio qio;

    public Group getSaved() {
        String idSql = "select max(id) from item_groups";
        long id = qio.getLong(idSql, new Object[]{});
        return get(id);
    }

    public long getCount() {
        String sql = "select count(*) from item_groups";
        Long count = (Long) qio.getLong(sql, new Object[] { });
        return count;
    }

    public Group get(long id){
        String sql = "select * from item_groups where id = [+]";
        Group group = (Group) qio.get(sql, new Object[] { id }, Group.class);
        return group;
    }

    public Group get(long id, long businessId){
        String sql = "select * from item_groups where id = [+] and business_id = [+]";
        Group group = (Group) qio.get(sql, new Object[] { id, businessId }, Group.class);
        return group;
    }

    public List<Group> getList(){
        String sql = "select * from item_groups order by id desc";
        List<Group> itemGroups = (ArrayList) qio.getList(sql, new Object[]{}, Group.class);
        return itemGroups;
    }

    public List<Group> getList(long id){
        String sql = "select * from item_groups where business_id = [+] and active = true order by id desc";
        List<Group> itemGroups = (ArrayList) qio.getList(sql, new Object[]{ id }, Group.class);
        return itemGroups;
    }

    public Boolean save(Group group){
        String sql = "insert into item_groups (name, business_id, design_id) values ('[+]',[+],[+])";
        qio.save(sql, new Object[] {
                group.getName(),
                group.getBusinessId(),
                group.getDesignId()

        });
        return true;
    }

    public Boolean update(Group item){
        String sql = "update item_groups set name = '[+]', design_id = [+] where id = [+]";
        qio.update(sql, new Object[] {
                item.getName(),
                item.getDesignId(),
                item.getId()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete from item_groups where id = [+]";
        qio.delete(sql, new Object[] { id });
        return true;
    }

}
