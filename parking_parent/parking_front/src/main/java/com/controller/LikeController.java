package com.controller;

import com.pojo.Like;
import com.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;


@Controller
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @RequestMapping("/findLike")
    private @ResponseBody List<Like> findLike(String id) {
        return likeService.findLike(id);
    }

    @RequestMapping("/addLike")
    public @ResponseBody List addLike(String pno, Integer ano, Integer cno, String oacconut) {
        return likeService.addLike(pno, ano, cno, oacconut);
    }

    @RequestMapping("/queryLike")
    public @ResponseBody List queryLike(String pno, Integer ano, Integer cno, String oacconut) {
        return likeService.queryLike(pno, ano, cno, oacconut);
    }

    @RequestMapping("/deleteLike")
    public @ResponseBody void deleteLike(String pno, Integer ano, Integer cno, String oacconut) {
        likeService.deleteLike(pno, ano, cno, oacconut);
    }
}
