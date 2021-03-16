package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class CategoryDaoImpl implements CategoryDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public List<Category> findAll() {
        String sql = "select * from tab_category";

        return template.query(sql,new BeanPropertyRowMapper<Category>(Category.class));
    }

    @Override
    public String findOne(int cid) {
        String cname = null;
        try {
            String sql = "select cname from tab_category where cid = ?";
            cname = template.queryForObject(sql, String.class, cid);
        } catch (DataAccessException e) {
            //e.printStackTrace();
        }
        return cname;
    }
}
