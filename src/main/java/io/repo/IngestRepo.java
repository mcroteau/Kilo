package io.repo;

import io.model.Ingest;
import qio.Qio;
import qio.annotate.DataStore;
import qio.annotate.Inject;

import java.util.ArrayList;
import java.util.List;

@DataStore
public class IngestRepo {

    @Inject
    Qio qio;

    public Ingest getSaved() {
        String idSql = "select max(id) from group_models";
        long id = qio.getLong(idSql, new Object[]{});
        return get(id);
    }

    public long getCount() {
        String sql = "select count(*) from group_models";
        Long count = (Long) qio.get(sql, new Object[] { }, Long.class);
        return count;
    }

    public Ingest get(long id){
        String sql = "select * from group_models where id = [+]";
        Ingest groupIngest = (Ingest) qio.get(sql, new Object[] { id }, Ingest.class);
        return groupIngest;
    }

    public Ingest get(String modelNumber){
        String sql = "select * from group_models where model_number = '[+]'";
        Ingest groupIngest = (Ingest) qio.get(sql, new Object[] { modelNumber }, Ingest.class);
        return groupIngest;
    }

    public List<Ingest> getList(long id){
        String sql = "select * from group_models where business_id = [+] order by id desc";
        List<Ingest> groupIngests = (ArrayList) qio.getList(sql, new Object[]{ id }, Ingest.class);
        return groupIngests;
    }

    public Boolean save(Ingest ingest){
        String sql = "insert into group_models (model_number, group_id, weight) values ('[+]',[+],[+])";
        qio.save(sql, new Object[] {

        });
        return true;
    }

    public boolean update(Ingest ingest) {
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
