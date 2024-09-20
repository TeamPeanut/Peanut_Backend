package com.springboot.peanut.service.Community;

import com.springboot.peanut.data.dto.community.CommentResponseDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;

import javax.servlet.http.HttpServletRequest;

public interface CommentService {
    ResultDto createComment(String content, Long id,HttpServletRequest request);
}
