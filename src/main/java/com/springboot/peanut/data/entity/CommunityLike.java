package com.springboot.peanut.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommunityLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean liked;

    private LocalDateTime create_At;

    private LocalDateTime update_At;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="community_id")
    private Community community;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 좋아요를 누른 사용자

    public static CommunityLike createLike(boolean liked, Community community, User user) {
        CommunityLike communityLike = new CommunityLike();
        communityLike.liked = liked;
        communityLike.community = community;
        communityLike.user = user;
        communityLike.create_At = LocalDateTime.now();
        communityLike.update_At = LocalDateTime.now();
        return communityLike;
    }
}
