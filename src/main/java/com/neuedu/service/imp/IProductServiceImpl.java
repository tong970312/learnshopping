package com.neuedu.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.neuedu.bean.Category;
import com.neuedu.bean.Product;
import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.service.IProductService;
import com.neuedu.util.DateUtils;
import com.neuedu.util.PropertiesUtils;
import com.neuedu.vo.ProductDetailVO;
import com.neuedu.vo.ProductListVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class IProductServiceImpl implements IProductService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryMapper categoryMapper;

    /*未测试*/
    @Override
    public ServerResponse saveOrUpdate(Product product) {
        //非空校验
        if (product == null) {
            return ServerResponse.createServerResponseByError("参数为空");
        }
        //设置商品主图
        String subImages = product.getSubImages();
        if (!StringUtils.isBlank(subImages)) {
            String[] subImageArr = subImages.split(",");
            if (subImageArr.length > 0) {
                //设置商品的主图
                product.setMainImage(subImageArr[0]);
            }
        }
        //商品添加或者更新
        if (product.getId() == null) {
            int result = productMapper.insert(product);
            if (result > 0) {
                return ServerResponse.createServerResponseBySuccess("添加成功");
            }
        } else {
            int result = productMapper.updateByPrimaryKey(product);
            if (result > 0) {
                return ServerResponse.createServerResponseBySuccess("修改成功");
            }

        }
        return  ServerResponse.createServerResponseByError("操作失败");
    }

    @Override
    public ServerResponse set_sale_status(Integer productId, Integer status) {
        if (productId==null){
            return ServerResponse.createServerResponseByError("商品id不能为空");
        }
        if (status==null){
            return ServerResponse.createServerResponseByError("商品状态不能为空");
        }

        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int result = productMapper.updateProductBySelectActive(product);
        if (result > 0) {
            return ServerResponse.createServerResponseBySuccess("修改状态成功");
        }else {

            return ServerResponse.createServerResponseByError("修改状态失败");
        }
    }

    @Override
    public ServerResponse detail(Integer productId) {
        //参数校验
        if (productId==null){
            return ServerResponse.createServerResponseByError("商品id不能为空");
        }
        //查询product
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponse.createServerResponseByError("商品不存在");
        }
        //转换
        ProductDetailVO productDetailVO = assembleProductDetailVO(product);
        //返回结果
        return ServerResponse.createServerResponseBySuccess("查找成功",productDetailVO);
    }
    private ProductDetailVO assembleProductDetailVO(Product product){

        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setCreateTime(DateUtils.dateToString(product.getCreateTime()));
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
        productDetailVO.setName(product.getName());
        productDetailVO.setId(product.getId());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setUpdateTime(DateUtils.dateToString(product.getUpdateTime()));
        Category category = categoryMapper.findParentCategory(product.getCategoryId());
        if (category!=null){
            productDetailVO.setParentCategoryId(category.getParentId());
        }else{
            productDetailVO.setParentCategoryId(0);
        }

        return productDetailVO;
    }

    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum,pageSize);
        //查找数据

        List<Product> productList = productMapper.selectAll();
        List<ProductListVO> productListVOS = Lists.newArrayList();
        if (productList!=null&&productList.size()>0){
            for (Product product:productList) {
            ProductListVO productListVO = assembleProductListVO(product);
            productListVOS.add(productListVO);
            }
        }
        PageInfo pageInfo = new PageInfo(productListVOS);
        //返回结果
        return ServerResponse.createServerResponseBySuccess("",pageInfo);
    }

    private ProductListVO assembleProductListVO(Product product){
        ProductListVO productListVO = new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setName(product.getName());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setSubtitle(product.getSubtitle());
        return productListVO;
    }


    @Override
    public ServerResponse search(Integer productId, String productName,
                                 Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);

        if (!StringUtils.isBlank(productName)){
            productName="%"+productName+"%";
        }
        List<Product> productList = productMapper.findByProductIdAndProductName(productId,productName);
        List<ProductListVO>productListVOList = Lists.newArrayList();
        if (productList!=null&&productList.size()>0){
            for (Product product:productList) {
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo = new PageInfo(productListVOList);

        return ServerResponse.createServerResponseBySuccess("",pageInfo);
    }

    /**
     * 查看商品详情
     * @param productId
     * @return
     */
    @Override
    public ServerResponse detail_portal(Integer productId) {
        System.out.println(productId);
        // 参数校验
        if (productId==null){
            return ServerResponse.createServerResponseByError("商品id不能为空");
        }
        //查询product
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponse.createServerResponseByError("商品不存在");
        }
        //校验商品状态
        if (product.getStatus()!=Const.ProductStatusEnum.PRODUCT_ONLINE.getCode()){
            return ServerResponse.createServerResponseByError("商品已下架或删除");
        }
        //获取productdetail
        //返回
        return ServerResponse.createServerResponseBySuccess("商品信息",product);
    }

    /**
     * 图片上传
     * @param file
     * @param path
     * @return
     */
    @Override
    public ServerResponse upload(MultipartFile file, String path) {
        if (file==null){
            return ServerResponse.createServerResponseByError("文件错误");
        }
        //获取图片名称
        String originalFilename= file.getOriginalFilename();
        //获取扩展名
        String exName=originalFilename.substring(originalFilename.lastIndexOf("."));
        //生成新的唯一的名字
        String newFilename = UUID.randomUUID().toString()+exName;

        File pathFile = new File(path);
        if (!pathFile.exists()){
            //设置可写并生成
            pathFile.setWritable(true);
            pathFile.mkdir();
        }
        File file1 = new File(path,newFilename);

        try {
            file.transferTo(file1);
            //上传到图片服务器
            //...
            Map<String,String> map = Maps.newHashMap();
            map.put("uri",newFilename);
            map.put("url",PropertiesUtils.readByKey("imageHost")+"/"+newFilename);
            return ServerResponse.createServerResponseBySuccess("上传成功",map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}