package com.neuedu.service.imp;

import com.google.common.collect.Sets;
import com.neuedu.bean.Category;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.service.ICategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    CategoryMapper categoryMapper;
    @Override
    public ServerResponse get_category(Integer categoryId) {
        //非空校验
        if (categoryId==null){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        //根据categoryid查询类别
           Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category==null){
            return ServerResponse.createServerResponseByError("查询类别不存在");
        }
        //查询子类别
        List<Category> categoryList =  categoryMapper.findChildCategory(categoryId);


        //返回结果

        return ServerResponse.createServerResponseBySuccess("",categoryList);
    }

    @Override
    public ServerResponse add_category(Integer parentId, String categoryName) {
        //1 参数校验
            if (StringUtils.isBlank(categoryName)){
                return ServerResponse.createServerResponseByError("类别名称不能为空");
            }
        //2 添加节点
        Category category = new Category();
            category.setName(categoryName);
            category.setParentId(parentId);
            category.setStatus(1);
        System.out.println("category = " + category);
            int result = categoryMapper.insert(category);
            if (result<=0){
                return ServerResponse.createServerResponseByError("添加失败");
            }
        //3 返回结果

        return ServerResponse.createServerResponseBySuccess("添加成功");
    }

    @Override
    public ServerResponse set_category_name(Integer categoryId, String categoryName) {
        //参数非空校验
        if(categoryId==null||categoryId.equals("")){
            return ServerResponse.createServerResponseByError("类别id不能为空");
        }
        if (StringUtils.isBlank(categoryName)){
            return  ServerResponse.createServerResponseByError("类别名称不能为空");
        }
        //根据categoryId查询

        Category category =  categoryMapper.selectByPrimaryKey(categoryId);
        if (category==null){
            return ServerResponse.createServerResponseByError("要修改的类别不存在");
        }
        //修改节点
        category.setName(categoryName);
        int result = categoryMapper.updateByPrimaryKey(category);
        //返回结果
        if (result<=0){
            return ServerResponse.createServerResponseByError("修改失败");
        }
        //3 返回结果

        return ServerResponse.createServerResponseBySuccess("修改成功");
    }

    @Override
    public ServerResponse get_deep_category(Integer categoryId) {
        //参数的非空校验
        if (categoryId==null){
            return ServerResponse.createServerResponseByError("类别名称不能为空");
        }
        //查询
        Set<Category> categorySet = Sets.newHashSet();
        categorySet = findAllChildCategory(categorySet,categoryId);

        Set<Integer> integerSet =  Sets.newHashSet();


        Iterator<Category> categoryIterator= categorySet.iterator();
        while (categoryIterator.hasNext()){
            Category category = categoryIterator.next();
            integerSet.add(category.getId());
        }
        return ServerResponse.createServerResponseBySuccess(null,integerSet);
    }

    private Set<Category> findAllChildCategory(Set<Category> categorySet, Integer categotyId){

            Category category= categoryMapper.selectByPrimaryKey(categotyId);
            if (category!=null){
                categorySet.add(category);
            }
            //查找categotyId下的子节点(平级)
            List<Category> categories = categoryMapper.findChildCategory(categotyId);
            if (categories!=null&&categories.size()>0){
                for (Category category1:categories) {
                    findAllChildCategory(categorySet,category1.getId());
                }
            }
        return categorySet;
    }
}
