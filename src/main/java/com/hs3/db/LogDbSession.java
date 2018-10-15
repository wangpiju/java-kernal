package com.hs3.db;

import com.hs3.exceptions.BaseDaoException;
import com.hs3.utils.ListUtils;
import com.hs3.utils.StrUtils;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("logDbSession")
public class LogDbSession {
    private static final Logger logger = LoggerFactory.getLogger(LogDbSession.class);
    private static final int MAX_LIMIT = 5000;
    @Resource(name = "logJdbcTemplate")
    private JdbcTemplate logJdbcTemplate;

    public JdbcTemplate getLogJdbcTemplate() {
        return logJdbcTemplate;
    }

    private String organizeSQL(String sql, Object[] args, Page page) {
        if (page != null) {
            String countSQL = getCountSQL(sql);
            Integer count = getInt(countSQL, args);
            page.setRowCount(count);
            sql = getLimit(sql, page);
        } else
            sql = getDefaultLimit(sql);

        return sql;
    }

    private String formatLog(Object... args) {
        StringBuilder sb = new StringBuilder();
        for (Object a : args) {
            sb.append(a).append("\r\n");
        }
        return sb.toString();
    }

    protected String getCountSQL(String sql) {
        return "SELECT count(*) FROM (" + sql + ") as v";
    }

    protected String getLimit(String sql, Page p) {
        sql = sql + " LIMIT " + p.getStartIndex() + "," + p.getPageSize();
        return sql;
    }

    protected String getDefaultLimit(String sql) {
        sql = sql + " LIMIT " + MAX_LIMIT;
        return sql;
    }

    public <T> T getObject(String sql, Class<T> cls) {
        return getObject(sql, null, cls);
    }

    public <T> T getObject(String sql, Object[] args, Class<T> cls) {
        try {
            T t = (T) this.logJdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper(cls));
            if (logger.isDebugEnabled()) {
                logger.debug(formatLog(sql, ListUtils.toString(args), cls.getName()));
            }
            return t;
        } catch (Exception e) {
            if ((e instanceof EmptyResultDataAccessException)) {
                return null;
            }
            logger.error(formatLog(sql, ListUtils.toString(args), cls.getName(), e.getMessage()), e);
            throw new BaseDaoException(e);
        }
    }

    public Integer getInt(String sql) {
        return getInt(sql, null);
    }

    public Integer getInt(String sql, Object[] args) {
        try {
            Integer rel = this.logJdbcTemplate.queryForObject(sql, args, Integer.class);
            if (logger.isDebugEnabled()) {
                logger.debug(formatLog(sql, ListUtils.toString(args)));
            }
            return rel;
        } catch (Exception e) {
            logger.error(formatLog(sql, ListUtils.toString(args), e.getMessage()), e);
            if (((e instanceof EmptyResultDataAccessException)) || ((e instanceof IncorrectResultSizeDataAccessException))) {
                return null;
            }
            throw new BaseDaoException(e);
        }
    }

    public String getString(String sql) {
        return getString(sql, null);
    }

    public String getString(String sql, Object[] args) {
        try {
            String rel = this.logJdbcTemplate.queryForObject(sql, args, String.class);
            if (logger.isDebugEnabled()) {
                logger.debug(formatLog(sql, ListUtils.toString(args)));
            }
            return rel;
        } catch (Exception e) {
            if (((e instanceof EmptyResultDataAccessException)) || ((e instanceof IncorrectResultSizeDataAccessException))) {
                return null;
            }
            logger.error(formatLog(sql, ListUtils.toString(args), e.getMessage()), e);
            throw new BaseDaoException(e);
        }
    }

    public Map<String, Object> getMap(String sql) {
        return getMap(sql, null, null);
    }

    public Map<String, Object> getMap(String sql, Object[] args) {
        return getMap(sql, args, null);
    }

    public Map<String, Object> getMap(String sql, Object[] args, Page page) {
        try {
            sql = organizeSQL(sql, args, page);
            Map<String, Object> rel = this.logJdbcTemplate.queryForMap(sql, args);
            if (logger.isDebugEnabled()) {
                logger.debug(formatLog(sql, ListUtils.toString(args)));
            }
            return rel;
        } catch (Exception e) {
            logger.error(formatLog(sql, ListUtils.toString(args), e.getMessage()), e);
            if ((e instanceof EmptyResultDataAccessException)) {
                return null;
            }
            throw new BaseDaoException(e);
        }
    }

    public List<Map<String, Object>> listMap(String sql) {
        return listMap(sql, null, null);
    }

    public List<Map<String, Object>> listMap(String sql, Page page) {
        return listMap(sql, null, page);
    }

    public List<Map<String, Object>> listMap(String sql, Object[] args, Page page) {
        try {
            sql = organizeSQL(sql, args, page);
            List<Map<String, Object>> rel = this.logJdbcTemplate.queryForList(sql, args);
            if (logger.isDebugEnabled()) {
                logger.debug(formatLog(sql, ListUtils.toString(args)));
            }
            return rel;
        } catch (Exception e) {
            logger.error(formatLog(sql, ListUtils.toString(args), e.getMessage()), e);
            if ((e instanceof EmptyResultDataAccessException)) {
                return new ArrayList<>();
            }
            throw new BaseDaoException(e);
        }
    }

    public <T> List<T> listSerializable(String sql, Class<T> cls) {
        return listSerializable(sql, null, cls, null);
    }

    public <T> List<T> listSerializable(String sql, Class<T> cls, Page page) {
        return listSerializable(sql, null, cls, page);
    }

    public <T> List<T> listSerializable(String sql, Object[] args, Class<T> cls) {
        return listSerializable(sql, args, cls, null);
    }

    public <T> List<T> listSerializable(String sql, Object[] args, Class<T> cls, Page page) {
        try {
            sql = organizeSQL(sql, args, page);
            List<T> rel = this.logJdbcTemplate.queryForList(sql, args, cls);
            if (logger.isDebugEnabled()) {
                logger.debug(formatLog(sql, ListUtils.toString(args), cls.getName()));
            }
            return rel;
        } catch (Exception e) {
            logger.error(formatLog(sql, ListUtils.toString(args), cls.getName(), e.getMessage()), e);
            if ((e instanceof EmptyResultDataAccessException)) {
                return new ArrayList<>();
            }
            throw new BaseDaoException(e);
        }
    }

    public <T> List<T> list(String sql, Class<T> cls) {
        return list(sql, null, cls, null);
    }

    public <T> List<T> list(String sql, Object[] args, Class<T> cls) {
        return list(sql, args, cls, null);
    }

    public <T> List<T> list(String sql, Class<T> cls, Page page) {
        return list(sql, null, cls, page);
    }

    public <T> List<T> list(String sql, Object[] args, Class<T> cls, Page page) {
        String countSQL = null;
        if (page != null) {
            countSQL = getCountSQL(sql);
        }
        return listAndPage(sql, args, countSQL, args, cls, page);
    }

    public <T> List<T> listAndPage(String sql, Object[] args, String pageSQL, Object[] pageArgs, Class<T> cls, Page page) {
        try {
            if (page != null) {
                Integer count = getInt(pageSQL, pageArgs);
                page.setRowCount(count);
                sql = getLimit(sql, page);
            }
            List<T> list = this.logJdbcTemplate.query(sql, args, new BeanPropertyRowMapper(cls));
            if (logger.isDebugEnabled()) {
                logger.debug(formatLog(sql, ListUtils.toString(args), cls.getName()));
            }
            return list;
        } catch (Exception e) {
            logger.error(formatLog(sql, ListUtils.toString(args), cls.getName(), e.getMessage()), e);
            if ((e instanceof EmptyResultDataAccessException)) {
                return new ArrayList<>();
            }
            throw new BaseDaoException(e);
        }
    }

    public void excute(String sql) {
        try {
            this.logJdbcTemplate.execute(sql);
            if (logger.isDebugEnabled()) {
                logger.debug(formatLog(sql));
            }
        } catch (Exception e) {
            logger.error(formatLog(sql, e.getMessage()), e);
            throw new BaseDaoException(e);
        }
    }

    public int update(String sql) {
        try {
            int row = this.logJdbcTemplate.update(sql);
            if (logger.isDebugEnabled()) {
                logger.debug(formatLog(sql));
            }
            return row;
        } catch (Exception e) {
            logger.error(formatLog(sql, e.getMessage()), e);
            throw new BaseDaoException(e);
        }
    }

    public int update(String sql, Object[] args) {
        try {
            int row = this.logJdbcTemplate.update(sql, args);
            if (logger.isDebugEnabled()) {
                logger.debug(formatLog(sql, ListUtils.toString(args)));
            }
            return row;
        } catch (Exception e) {
            logger.error(formatLog(sql, ListUtils.toString(args), e.getMessage()), e);
            throw new BaseDaoException(e);
        }
    }

    public int update(String sql, Object[] args, Integer[] argTypes) {
        try {
            int rel = this.logJdbcTemplate.update(sql, args, argTypes);
            if (logger.isDebugEnabled()) {
                logger.debug(formatLog(sql, ListUtils.toString(args), ListUtils.toString(argTypes)));
            }
            return rel;
        } catch (Exception e) {
            logger.error(formatLog(sql, ListUtils.toString(args), e.getMessage()), e);
            throw new BaseDaoException(e);
        }
    }

    public int updateKeyHolder(final String sql, final Object[] args) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            this.logJdbcTemplate.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection conn)
                        throws SQLException {
                    PreparedStatement ps = conn.prepareStatement(sql, 1);
                    for (int i = 0; i < args.length; i++) {
                        ps.setObject(i + 1, args[i]);
                    }
                    return ps;
                }
            }, keyHolder);
            if (logger.isDebugEnabled()) {
                logger.debug(formatLog(sql, ListUtils.toString(args)));
            }
            return keyHolder.getKey().intValue();
        } catch (Exception e) {
            logger.error(formatLog(sql, ListUtils.toString(args), e.getMessage()), e);
            throw new BaseDaoException(e);
        }
    }

    public BigDecimal sum(String sql, List<Object> cond) {
        String n = getString(sql, cond.toArray(new Object[cond.size()]));
        return StrUtils.hasEmpty(n) ? BigDecimal.ZERO : new BigDecimal(n);
    }
}
