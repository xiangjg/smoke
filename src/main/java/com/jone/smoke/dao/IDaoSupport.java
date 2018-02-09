package com.jone.smoke.dao;

import org.hibernate.SQLQuery;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Transactional
@Repository
public class IDaoSupport<T, ID extends Serializable> implements IDao<T, ID> {
    private Logger logger = LoggerFactory.getLogger(IDaoSupport.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public boolean save(T entity) {
        boolean flag = false;
        try {
            entityManager.persist(entity);
            flag = true;
        } catch (Exception e) {
            logger.error("保存出错:", e);
            throw e;
        } finally {
            entityManager.clear();
            entityManager.close();
        }
        return flag;
    }

    @Transactional
    @Override
    public boolean delete(T entity) {
        boolean flag = false;
        try {
            entityManager.remove(entityManager.merge(entity));
            flag = true;
        } catch (Exception e) {
            logger.error("删除出错", e);
        } finally {
            entityManager.clear();
            entityManager.close();
        }
        return flag;
    }

    @Transactional
    @Override
    public boolean update(T entity) {
        boolean flag = false;
        try {
            entityManager.merge(entity);
            flag = true;
        } catch (Exception e) {
            logger.error("更新出错", e);
        } finally {
            entityManager.clear();
            entityManager.close();
        }
        return flag;
    }

    @Override
    public List<T> findBySqlToMap(String sql) {
        List<T> list = null;
        try {
            Query query = entityManager.createNativeQuery(sql);
            list = query.unwrap(SQLQuery.class)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                    .list();
        } catch (Exception e) {
            logger.error("查询出错:", e);
            throw e;
        } finally {
            entityManager.clear();
            entityManager.close();
        }
        return list;
    }

    @Override
    public List<T> findBySql(Class<T> clazz, String sql) {
        List<T> list = null;
        try {
            Query query = entityManager.createNativeQuery(sql);
            ResultTransformer rt = Transformers.aliasToBean(clazz);
            query.unwrap(SQLQuery.class)
                    .setResultTransformer(
                            rt
                    );
            list = query.getResultList();
        } catch (Exception e) {
            logger.error("查询出错:", e);
            throw e;
        } finally {
            entityManager.clear();
            entityManager.close();
        }
        return list;
    }

    @Transactional
    @Override
    public List<T> findByMoreFiled(String tablename, LinkedHashMap<String, Object> map) {
        String sql = "from " + tablename + " u WHERE ";
        Set<String> set = null;
        set = map.keySet();
        List<String> list = new ArrayList<>(set);
        List<Object> filedlist = new ArrayList<>();
        for (String filed : list) {
            sql += "u." + filed + "=? and ";
            filedlist.add(filed);
        }
        sql = sql.substring(0, sql.length() - 4);
        logger.debug(sql + "----sql语句------");
        Query query = entityManager.createQuery(sql);
        for (int i = 0; i < filedlist.size(); i++) {
            query.setParameter(i + 1, map.get(filedlist.get(i)));
        }
        List<T> listRe = query.getResultList();
        entityManager.clear();
        entityManager.close();
        return listRe;
    }

    @Transactional
    @Override
    public List<T> findByMoreFiledpages(String tablename, LinkedHashMap<String, Object> map, int start, int pageNumber) {
        String sql = "from " + tablename + " u WHERE ";
        Set<String> set = null;
        set = map.keySet();
        List<String> list = new ArrayList<>(set);
        List<Object> filedlist = new ArrayList<>();
        for (String filed : list) {
            sql += "u." + filed + "=? and ";
            filedlist.add(filed);
        }
        sql = sql.substring(0, sql.length() - 4);
        logger.debug(sql + "----sql语句------");
        Query query = entityManager.createQuery(sql);
        for (int i = 0; i < filedlist.size(); i++) {
            query.setParameter(i + 1, map.get(filedlist.get(i)));
        }
        query.setFirstResult((start - 1) * pageNumber);
        query.setMaxResults(pageNumber);
        List<T> listRe = query.getResultList();
        entityManager.clear();
        entityManager.close();
        return listRe;
    }

    @Transactional
    @Override
    public List<T> findpages(String tablename, String filed, Object o, int start, int pageNumer) {
        String sql = "from " + tablename + " u WHERE u." + filed + "=?";
        logger.debug(sql + "----sql语句------");
        List<T> list = new ArrayList<>();
        try {
            Query query = entityManager.createQuery(sql);
            query.setParameter(1, o);
            query.setFirstResult((start - 1) * pageNumer);
            query.setMaxResults(pageNumer);
            list = query.getResultList();
            entityManager.clear();
            entityManager.close();
        } catch (Exception e) {
            logger.error("分页错误", e);
        } finally {
            entityManager.close();
        }

        return list;
    }
}
