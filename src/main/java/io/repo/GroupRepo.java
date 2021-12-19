package io.repo;

import io.model.Group;
import io.model.GroupCategory;
import io.model.GroupOption;
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
        String idSql = "select max(id) from groups";
        long id = qio.getLong(idSql, new Object[]{});
        return get(id);
    }

    public long getCount() {
        String sql = "select count(*) from groups";
        Long count = (Long) qio.getLong(sql, new Object[] { });
        return count;
    }

    public Group get(long id){
        String sql = "select * from groups where id = [+]";
        Group group = (Group) qio.get(sql, new Object[] { id }, Group.class);
        return group;
    }

    public Group get(long id, long businessId){
        String sql = "select * from groups where id = [+] and business_id = [+]";
        Group group = (Group) qio.get(sql, new Object[] { id, businessId }, Group.class);
        return group;
    }

    public List<Group> getList(){
        String sql = "select * from groups order by id desc";
        List<Group> itemGroups = (ArrayList) qio.getList(sql, new Object[]{}, Group.class);
        return itemGroups;
    }

    public List<Group> getList(long id){
        String sql = "select * from groups where business_id = [+] and active = true order by id desc";
        List<Group> itemGroups = (ArrayList) qio.getList(sql, new Object[]{ id }, Group.class);
        return itemGroups;
    }

    public Boolean save(Group group){
        String sql = "insert into groups (name, image_uri, business_id, design_id) values ('[+]','[+]',[+],[+])";
        qio.save(sql, new Object[] {
                group.getName(),
                group.getImageUri(),
                group.getBusinessId(),
                group.getDesignId()

        });
        return true;
    }

    public Boolean update(Group item){
        String sql = "update groups set name = '[+]', design_id = [+] where id = [+]";
        qio.update(sql, new Object[] {
                item.getName(),
                item.getDesignId(),
                item.getId()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete from groups where id = [+]";
        qio.delete(sql, new Object[] { id });
        return true;
    }

    public boolean saveOption(GroupOption groupOption) {
        String sql = "insert into group_options (title, business_id) values ('[+]',[+])";
        qio.save(sql, new Object[] {
                groupOption.getTitle(),
                groupOption.getBusinessId()
        });
        return true;
    }

    public List<GroupOption> getListOptions(Long id){
        String sql = "select * from group_options where business_id = [+] order by id desc";
        List<GroupOption> groupOptions = (ArrayList) qio.getList(sql, new Object[]{ id }, GroupOption.class);
        return groupOptions;
    }

    public boolean deleteOption(Long id) {
        String sql = "delete from group_options where id = [+]";
        qio.delete(sql, new Object[] { id });
        return true;
    }

    public boolean saveCategory(GroupCategory groupCategory) {
        String sql = "insert into group_categories (group_id, category_id, business_id) values ([+],[+],[+])";
        qio.save(sql, new Object[] {
                groupCategory.getGroupId(),
                groupCategory.getCategoryId(),
                groupCategory.getBusinessId()
        });
        return true;
    }
}
