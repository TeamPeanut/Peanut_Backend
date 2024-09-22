package com.springboot.peanut.service.Community.Impl;

import com.springboot.peanut.data.dao.CommentDao;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.entity.Comment;
import com.springboot.peanut.data.entity.Community;
import com.springboot.peanut.data.entity.User;
import com.springboot.peanut.data.repository.CommunityRepository;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.service.Community.CommentService;
import com.springboot.peanut.service.Result.ResultStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;
    private final ResultStatusService resultStatusService;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final CommunityRepository communityRepository;

    @Override
    public ResultDto createComment(String comment, Long id,HttpServletRequest request) {
        ResultDto resultDto = new ResultDto();
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);

        Comment commentInfo = new Comment();
        Community community = communityRepository.findById(id).get();

        commentInfo.setContent(comment);
        commentInfo.setUser(user.get());
        commentInfo.setCommunity(community);
        commentInfo.setCreate_At(LocalDateTime.now());
        commentDao.saveComment(commentInfo);

        resultDto.setDetailMessage("댓글 작성 완료");
        resultStatusService.setSuccess(resultDto);

        return resultDto;
    }
}
