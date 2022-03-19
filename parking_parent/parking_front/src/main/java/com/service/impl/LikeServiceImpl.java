package com.service.impl;

import com.dao.LikeDao;
import com.pojo.Like;
import com.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private LikeDao likeDao;

    @Override
    public List<Like> findLike(String id) {
        return likeDao.findLike(id);
    }

    @Override
    public List<Like> addLike(String pno, Integer ano, Integer cno, String oacconut) {
        return likeDao.addLike(pno, ano, cno, oacconut);
    }

    @Override
    public List<Like> queryLike(String pno, Integer ano, Integer cno, String oacconut) {
        return likeDao.queryLike(pno, ano, cno, oacconut);
    }

    @Override
    public void deleteLike(String pno, Integer ano, Integer cno, String oacconut) {
        likeDao.deleteLike(pno, ano, cno, oacconut);
    }
}
