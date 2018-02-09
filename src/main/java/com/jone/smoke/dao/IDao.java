package com.jone.smoke.dao;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

public interface IDao<T,ID extends Serializable> {
    /**
     * 保存数据对象
     * @param entity
     * @return
     */
    boolean save(T entity);
    boolean delete(T entity);
    boolean update(T e);
    List<T> findBySqlToMap(String sql);
    List<T> findBySql(Class<T> clzz, String sql);
    /**
     * 多个字段的查询
     * @param tablename 表名
     * @param map 将你的字段传入map中
     * @return
     */
    List<T> findByMoreFiled(String tablename, LinkedHashMap<String, Object> map);
    /**
     * 多字段查询分页
     * @param tablename 表名
     * @param map 以map存储key,value
     * @param start 第几页
     * @param pageNumer 一个页面的条数
     * @return
     */
    List<T> findByMoreFiledpages(String tablename, LinkedHashMap<String, Object> map, int start, int pageNumer);
    /**
     * 一个字段的分页
     * @param  tablename 表名
     * @param filed 字段名
     * @param o 字段参数
     * @param start 第几页
     * @param pageNumer 一个页面多少条数据
     * @return
     */
    List<T> findpages(String tablename, String filed, Object o, int start, int pageNumer);
}
