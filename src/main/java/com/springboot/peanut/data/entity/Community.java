package com.springboot.peanut.data.entity;

import com.springboot.peanut.data.dto.community.CommunityRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private int communityLike = 0;

    private LocalDateTime create_At;

    private LocalDateTime update_At;

    @OneToMany(mappedBy = "community",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    public static Community createCommunity(CommunityRequestDto communityRequestDto,User user){
     Community community = new Community();
     community.title = communityRequestDto.getTitle();
     community.content = communityRequestDto.getContent();
     community.user = user;
     community.create_At = LocalDateTime.now();
     return community;
    }
}
