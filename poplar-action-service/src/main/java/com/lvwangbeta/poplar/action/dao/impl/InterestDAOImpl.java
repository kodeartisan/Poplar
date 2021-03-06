package com.lvwangbeta.poplar.action.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import com.lvwangbeta.poplar.action.dao.InterestDAO;
import com.lvwangbeta.poplar.action.dao.InterestMapper;
import com.lvwangbeta.poplar.common.model.Interest;
import com.lvwangbeta.poplar.common.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

@Repository("interestDao")
public class InterestDAOImpl implements InterestDAO {

	public static String TABLE_INTEREST = "poplar_interests";
	public static String TABLE_TAG 		= "poplar_tags";
	
	private static final String INTEREST_KEY = "tag:interest:user:"; //用户关注的tag
		
	@Autowired
	private InterestMapper interestMapper;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate; 
	
	@Resource(name="redisTemplate")
	private SetOperations<String, Integer> setOps;	

	public int saveInterest(Interest interest) {
		interestMapper.saveInterest(interest);
		setOps.add(INTEREST_KEY+interest.getUser_id(), interest.getTag_id());
		return interest.getId();
	}

	public int delInterest(Interest interest) {
		int effrows = interestMapper.delInterest(interest);
		setOps.remove(INTEREST_KEY+interest.getUser_id(), interest.getTag_id());
		return effrows;
	}

	public List<Integer> getUsersInterestInTag(int tag_id) {
		return interestMapper.getUsersInterestInTag(tag_id);
	}

	public Boolean hasInterestInTag(int user_id, int tag_id) {
		return setOps.isMember(INTEREST_KEY+user_id, tag_id);
	}
	
	
	public List<Integer> hasInterestInTags(int user_id, List<Integer> tag_ids){
		List<Integer> result = new ArrayList<Integer>();
		Iterator<Integer> ids_it = tag_ids.iterator();
		while(ids_it.hasNext()) {
			int id = ids_it.next();
			if(hasInterestInTag(user_id, id )) {
				result.add(id);
			}
		}
		return result;
	}

	public List<Tag> getTagsUserInterestedIn(int user_id) {
		return interestMapper.getTagsUserInterestedIn(user_id);
	}
	
}
