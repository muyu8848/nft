package com.nft.collection.vo;

import java.util.List;

import lombok.Data;

@Data
public class GroupByTimeCollectionVO {

	private String time;

	private List<ForSaleCollectionVO> collections;

}
