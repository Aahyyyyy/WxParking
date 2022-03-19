package com.zjut.lsw;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.zjut.lsw.mapper.AdminMapper;
//import com.zjut.lsw.mapper.StudentInfoMapper;
//import com.zjut.lsw.mapper.TeacherMapper;
//import com.zjut.lsw.mapper.ViewGradeMapper;
//import com.zjut.lsw.pojo.ViewGrade;
import com.zjut.lsw.mapper.AdminMapper;
import com.zjut.lsw.mapper.SalerMapper;
import com.zjut.lsw.pojo.Saler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class AchievementManagementApplicationTests {

   //@Autowired的使用来消除 set ，get方法
    @Autowired
     private SalerMapper salerMapper;
//    @Autowired
//    private TeacherMapper teacherMapper;
    @Autowired
    private AdminMapper adminMapper;
//    @Autowired
//    private ViewGradeMapper viewGradeMapper;
    @Test
       void contextLoads() {
//        List<Saler> students = SalerMapper.selectList(null);
//        students.forEach(System.out::print);
//        List<Teacher> teachers = teacherMapper.selectList(null);
//        teachers.forEach(System.out::print);
//        Admin admin = adminMapper.selectOne(null);
//        QueryWrapper<ViewGrade> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("qll_Sno12","201906062501");
//        List<ViewGrade> viewGrades = viewGradeMapper.selectList(queryWrapper);
    }

}
