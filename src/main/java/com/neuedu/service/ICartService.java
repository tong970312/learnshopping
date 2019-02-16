package com.neuedu.service;

import com.neuedu.common.ServerResponse;

public interface ICartService {

    public ServerResponse add(Integer userId,Integer productId,Integer count);

}
