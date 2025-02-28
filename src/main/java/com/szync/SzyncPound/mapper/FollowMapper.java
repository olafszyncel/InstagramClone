package com.szync.SzyncPound.mapper;

import com.szync.SzyncPound.dto.FollowDto;
import com.szync.SzyncPound.models.Follow;

public class FollowMapper {
    public static Follow mapToFollow(FollowDto followDto) {
        return Follow.builder()
                .id(followDto.getId())
                .follower(followDto.getFollower())
                .following(followDto.getFollowing())
                .createdOn(followDto.getCreatedOn())
                .build();
    }

    public static FollowDto mapToFollowDto(Follow follow) {
        return FollowDto.builder()
                .id(follow.getId())
                .follower(follow.getFollower())
                .following(follow.getFollowing())
                .createdOn(follow.getCreatedOn())
                .build();
    }
}
