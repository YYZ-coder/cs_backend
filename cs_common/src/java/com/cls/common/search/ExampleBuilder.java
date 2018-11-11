package com.cls.common.search;

import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * Project: cs_backend
 * @author Yue
 * @author stephen.ni
 * @create 2018/4/22-12:57
 * Description：
 *  高级查询对象example构造器
 */
public class ExampleBuilder
{
    private static final String separator = "_";

    /**
     * in 或 not in 最大元素个数
     */
    private static final int IN_SIZE = 1000;

    private Example example;

    /**
     * 列筛选
     */
    private List<String> fields;

    /**
     * 排序
     */
    private List<String> sort;

    /**
     * 查询列优先级
     */
    private List<String> priorities;

    /**
     * 查询条件
     */
    private Map<String, Object> search;

    public ExampleBuilder(Class<?> entityClass)
    {
        this.example = new Example(entityClass);
    }

    public ExampleBuilder fields(List<String> fields)
    {
        this.fields = fields;
        return this;
    }

    public ExampleBuilder fields(String... fields)
    {
        this.fields = Arrays.asList(fields);
        return this;
    }

    public ExampleBuilder search(Map<String, Object> search)
    {
        this.search = search;
        return this;
    }

    public ExampleBuilder sort(List<String> sort)
    {
        this.sort = sort;
        return this;
    }

    public ExampleBuilder sort(String... sort)
    {
        this.sort = Arrays.asList(sort);
        return this;
    }

    public ExampleBuilder priorities(String... priorities)
    {
        this.priorities = Arrays.asList(priorities);
        return this;
    }

    public Example build()
    {
        buildFields();
        buildSearch();
        buildSort();
        return this.example;
    }

    private void buildFields()
    {
        if (fields != null && fields.size() > 0)
        {
            String[] properties = fields.toArray(new String[fields.size()]);
            this.example.selectProperties(properties);
        }
    }

    private void buildSearch()
    {
        if (search != null && search.size() > 0)
        {
            Example.Criteria criteria = this.example.createCriteria();

            for (String key : priorKeys())
            {
                String property = key;
                SearchOperator operator = SearchOperator.eq;
                Object value = search.get(key);

                int index = key.lastIndexOf(separator);
                if (index > 0)
                {
                    property = key.substring(0, index);
                    operator = SearchOperator.valueOf(key.substring(index + 1));
                }

                switch (operator)
                {
                    case eq:
                        criteria.andEqualTo(property, value);
                        break;
                    case ne:
                        criteria.andNotEqualTo(property, value);
                        break;
                    case gt:
                        criteria.andGreaterThan(property, value);
                        break;
                    case gte:
                        criteria.andGreaterThanOrEqualTo(property, value);
                        break;
                    case lt:
                        criteria.andLessThan(property, value);
                        break;
                    case lte:
                        criteria.andLessThanOrEqualTo(property, value);
                        break;
                    case in:
                        criteria.andIn(property, getInValue(value));
                        break;
                    case notIn:
                        criteria.andNotIn(property, getInValue(value));
                        break;
                    case like:
                        criteria.andLike(property, getLikeValue(value));
                        break;
                    case notLike:
                        criteria.andNotLike(property, getLikeValue(value));
                        break;
                    default:
                        throw new IllegalArgumentException("search unsupport operator [" + operator + "]");
                }
            }
        }
    }

    private String getLikeValue(Object value)
    {
        String strValue = (String) value;
        if (strValue != null)
        {
            String likeStr = strValue.replace("%", "\\%").replace("_", "\\_");
            return "%" + likeStr + "%";
        }
        return strValue;
    }

    private List getInValue(Object value)
    {
        List listValue = (List) value;
        if (CollectionUtils.isEmpty(listValue))
        {
            return null;
        }
        else
        {
            Preconditions.checkArgument(listValue.size() <= IN_SIZE, "search unsupprot [in] size > %s", IN_SIZE);
            return listValue;
        }
    }

    /**
     * 优先级排序算法
     *
     * @return
     */
    private Collection<String> priorKeys()
    {
        if (priorities != null && !priorities.isEmpty())
        {
            Set<String> keys = new HashSet<>(search.keySet());
            ArrayList<String> priorKeys = new ArrayList<>(keys.size());

            if (keys != null && !keys.isEmpty())
            {
                for (String priority : priorities)
                {
                    //TODO 待优化
                    for (String key : search.keySet())
                    {
                        String property = key;
                        int index = key.lastIndexOf(separator);
                        if (index > 0)
                        {
                            property = key.substring(0, index);
                        }

                        if (property.equals(priority))
                        {
                            priorKeys.add(key);
                            keys.remove(key);
                            break;
                        }
                    }
                }

                priorKeys.addAll(keys);
            }
            return priorKeys;
        }
        else
        {
            return search.keySet();
        }
    }

    private static final String ORDER_ACS = "asc";
    private static final String ORDER_DESC = "desc";

    private void buildSort()
    {
        if (sort != null && sort.size() > 0)
        {
            for (String groupBy : sort)
            {
                String property = groupBy;
                String order = ORDER_ACS;
                int index = groupBy.lastIndexOf(separator);
                if (index > 0)
                {
                    property = groupBy.substring(0, index);
                    order = groupBy.substring(index + 1);
                }

                if (ORDER_ACS.equals(order))
                {
                    this.example.orderBy(property).asc();
                }
                else if (ORDER_DESC.equals(order))
                {
                    this.example.orderBy(property).desc();
                }
                else
                {
                    throw new IllegalArgumentException("sort unsupport order [" + order + "]");
                }
            }
        }
    }

    /**
     * 查询操作符
     *
     * @author stephen.ni
     */
    private enum SearchOperator
    {
        eq,
        ne,
        gt,
        gte,
        lt,
        lte,
        in,
        notIn,
        like,
        notLike
    }

}
