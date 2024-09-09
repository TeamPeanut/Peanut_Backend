package com.springboot.peanut.service;

import com.springboot.peanut.dto.foodPredict.FoodDetailInfoDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface FoodDetailService {
    List<FoodDetailInfoDto> getFoodDetailInfo(List<String> name , HttpServletRequest request);

}
