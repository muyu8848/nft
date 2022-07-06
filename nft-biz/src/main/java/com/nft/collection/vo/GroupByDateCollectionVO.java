package com.nft.collection.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GroupByDateCollectionVO {

	private String date;

	private List<GroupByTimeCollectionVO> timeCollections = new ArrayList<>();

}
