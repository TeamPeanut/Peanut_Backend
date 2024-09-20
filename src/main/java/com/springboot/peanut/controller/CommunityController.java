package com.springboot.peanut.controller;

import com.springboot.peanut.data.dto.community.CommunityRequestDto;
import com.springboot.peanut.data.dto.community.CommunityResponseDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.service.Community.CommunityService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;

    @PostMapping("/create")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> createCommunity(@RequestBody CommunityRequestDto communityRequestDto, HttpServletRequest request) {
        ResultDto resultDto = communityService.createCommunity(communityRequestDto, request);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }

    @GetMapping("/detail")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<CommunityResponseDto> detailsCommunity(@RequestParam Long id, HttpServletRequest request) {
        CommunityResponseDto communityResponseDto = communityService.detailsCommunity(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(communityResponseDto);
    }
    @GetMapping("/all/details")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<List<CommunityResponseDto>>getAllCommunity(HttpServletRequest request) {
        List<CommunityResponseDto> communityResponseDtos = communityService.getAllCommunity(request);
        return ResponseEntity.status(HttpStatus.OK).body(communityResponseDtos);
        }
    }
