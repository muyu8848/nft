package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nft.collection.domain.CollectionStory;
import com.nft.collection.domain.Creator;
import com.nft.collection.domain.MemberHoldCollection;

import lombok.Data;

@Data
public class MyHoldCollectionDetailVO {

	private String id;

	private String collectionName;

	private String collectionCover;

	private String creatorAvatar;

	private String creatorName;

	private String holderNickName;

	private String holderAvatar;

	private List<String> storyPicLinks = new ArrayList<>();

	public static MyHoldCollectionDetailVO convertFor(MemberHoldCollection po) {
		if (po == null) {
			return null;
		}
		MyHoldCollectionDetailVO vo = new MyHoldCollectionDetailVO();
		vo.setId(po.getId());
		if (po.getCollection() != null) {
			vo.setCollectionName(po.getCollection().getName());
			vo.setCollectionCover(po.getCollection().getCover());
			Creator creator = po.getCollection().getCreator();
			if (creator != null) {
				vo.setCreatorName(creator.getName());
				vo.setCreatorAvatar(creator.getAvatar());
			}
			Set<CollectionStory> collectionStorys = po.getCollection().getCollectionStorys();
			for (CollectionStory collectionStory : collectionStorys) {
				vo.getStoryPicLinks().add(collectionStory.getPicLink());
			}
		}
		if (po.getMember() != null) {
			vo.setHolderNickName(po.getMember().getNickName());
			vo.setHolderAvatar(po.getMember().getAvatar());
		}

		return vo;
	}

}
